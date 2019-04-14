package dev.growi.passwordstore.server.userdata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration.JpaDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaUserRepository;
import dev.growi.passwordstore.server.userdata.domain.service.CryptographyService;
import dev.growi.passwordstore.server.userdata.domain.service.PasswordGeneratorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Conditional(JpaDatasourceCondition.class)
public class JpaUserDataProvider implements UserDataProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CryptographyService cryptographyService;

    @Autowired
    private PasswordGeneratorService passwordGenerator;

    @Override
    public void checkAdmin() throws CryptographyException {
        Optional<JpaUser> optUser = jpaUserRepository.findByUserName("Admin");

        if (!optUser.isPresent()) {
            String password = passwordGenerator.generate();
            createUser("Admin", password, (UserDAO) null);

            logger.info("\033[32;1;2mCreated user 'Admin' with password '" + password + "'\033[0m");
        }
    }

    @Override
    public List<UserDAO> findAll() {
        return jpaUserRepository.findAll().stream().map(user -> (UserDAO) user).collect(Collectors.toList());
    }

    @Override
    public UserDAO findUserById(IdWrapper<?> id) throws UserNotFoundException {
        if (!JpaUser.UserId.class.isAssignableFrom(id.getClass())) {
            throw new IllegalArgumentException("Id value is of the wrong type. " +
                    "Expected " + JpaUser.UserId.class.getCanonicalName() +
                    " got " + id.getClass().getCanonicalName() + ".");
        }
        Optional<JpaUser> optUser = jpaUserRepository.findById((Long) id.getValue());

        if (optUser.isPresent()) {
            return optUser.get();
        }

        throw new UserNotFoundException("id=" + ((Long) id.getValue()).toString());
    }

    @Override
    public UserDAO findUserByUserName(String userName) throws UserNotFoundException {

        Optional<JpaUser> optUser = jpaUserRepository.findByUserName(userName);

        if (optUser.isPresent()) {
            return optUser.get();
        }

        throw new UserNotFoundException("userName=" + userName);
    }

    @Override
    public UserDAO createUser(final String userName, final String password,
                              UserDetails activeUser) throws UserNotFoundException, CryptographyException {

        UserDAO activeUserDAO = findUserByUserName(activeUser.getUsername());

        return createUser(userName, password, activeUserDAO);

    }

    private UserDAO createUser(final String userName, final String password, UserDAO activeUserDAO) throws CryptographyException {
        Instant now = Instant.now();

        JpaUser user = new JpaUser(userName, passwordEncoder.encode(password));
        try {
            KeyPair keyPair = cryptographyService.generateRSAKeyPair();
            EncryptedPrivateKeyInfo privateKey = cryptographyService.pbeEncrypt(password, keyPair.getPrivate());

            user.setPublicKey(keyPair.getPublic().getEncoded());
            user.setPrivateKey(privateKey.getEncoded());
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException |
                IllegalBlockSizeException | NoSuchPaddingException | IOException | InvalidParameterSpecException |
                InvalidKeySpecException | InvalidAlgorithmParameterException e) {
            throw new CryptographyException(e);
        }

        user.setUserName(userName);
        user.isEnabled(true);
        user.isAccountExpired(false);
        user.isCredentialsExpired(false);
        user.isAccountLocked(false);
        user.setCreatedByUser(activeUserDAO);
        user.setCreatedStamp(now);
        user.setLastUpdatedByUser(activeUserDAO);
        user.setLastUpdatedStamp(now);

        user = jpaUserRepository.save(user);

        return user;
    }

    @Override
    public Set<GroupMemberDAO> findGroupMembershipsByUserId(IdWrapper<?> userId) throws UserNotFoundException {
        Optional<JpaUser> userDAO = jpaUserRepository.findById(((JpaUser.UserId) userId).getValue());

        if (userDAO.isPresent()) {
            return userDAO.get().getMemberships().stream().map(userMember -> (GroupMemberDAO) userMember).collect(Collectors.toSet());
        }
        throw new UserNotFoundException("userId=" + userId.getValue());
    }

    @Override
    public void changePassword(UserDAO user, String oldPassword, String newPassword) throws CryptographyException {

        try {
            user.setPrivateKey(
                    cryptographyService.pbeEncrypt(newPassword,
                            cryptographyService.pbeDecrypt(oldPassword, user.getPrivateKey())).getEncoded());
            jpaUserRepository.save((JpaUser) user);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException
                | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | InvalidParameterSpecException | IOException e) {
            throw new CryptographyException(e);
        }
    }
}
