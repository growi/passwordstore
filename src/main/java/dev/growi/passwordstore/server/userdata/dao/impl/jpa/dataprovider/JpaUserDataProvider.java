package dev.growi.passwordstore.server.userdata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.core.authentication.AuthenticationFacade;
import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.dataprovider.JpaMonitoreddDataProvider;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration.JpaDatasourceCondition;
import dev.growi.passwordstore.server.userdata.dao.model.GroupMemberDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.repository.JpaUserRepository;
import dev.growi.passwordstore.server.shared.service.CryptographyService;
import dev.growi.passwordstore.server.shared.service.PasswordGeneratorService;
import dev.growi.passwordstore.server.userdata.domain.exception.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
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
public class JpaUserDataProvider extends JpaMonitoreddDataProvider<Long, UserDAO> implements UserDataProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private JpaUserRepository jpaUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CryptographyService cryptographyService;

    @Autowired
    private PasswordGeneratorService passwordGenerator;

    @Value("${passwordstore.security.admin.password:}")
    private String configuredAdminPassword;

    @Override
    public void checkAdmin() throws CryptographyException {
        Optional<JpaUser> optUser = jpaUserRepository.findByUserName("Admin");

        String password = "";
        if(!configuredAdminPassword.isEmpty()){
            password = configuredAdminPassword;
            logger.warn("\u001B[31mATTENTION: Admin password is stored in property file. This is a security risk! DO NOT do this in a production environment!\033[0m");
        }
        if (!optUser.isPresent()) {
            if(password.isEmpty()){
                password = passwordGenerator.generate();
            }
            createUser("Admin", password, (UserDAO) null);

            logger.info("\033[32;1;2mCreated user 'Admin' with password '" + password + "'\033[0m");
        }
    }

    @Override
    public List<UserDAO> findAll() {
        return jpaUserRepository.findAll().stream().map(user -> (UserDAO) user).collect(Collectors.toList());
    }

    @Override
    public UserDAO findById(Long id) throws UserNotFoundException {

        Optional<JpaUser> optUser = jpaUserRepository.findById(id);
        if (optUser.isPresent()) {
            return optUser.get();
        }
        throw new UserNotFoundException("id=" + id);
    }

    @Override
    public UserDAO findByUserName(String userName) throws UserNotFoundException {

        Optional<JpaUser> optUser = jpaUserRepository.findByUserName(userName);

        if (optUser.isPresent()) {
            return optUser.get();
        }

        throw new UserNotFoundException("userName=" + userName);
    }

    @Override
    public UserDAO create() throws DatasourceException {
        return null;
    }

    @Override
    public UserDAO create(final String userName, final String password) throws UserNotFoundException, DatasourceException {

        UserDAO activeUserDAO = findByUserName(authenticationFacade.getAuthentication().getName());

        return createUser(userName, password, activeUserDAO);
    }

    private UserDAO createUser(final String userName, final String password, UserDAO activeUserDAO) throws DatasourceException {
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
            throw new DatasourceException(new CryptographyException(e));
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

        user = jpaUserRepository.save((JpaUser) user);

        return user;
    }

    @Override
    public Set<GroupMemberDAO> findGroupMembershipsByUserId(Long id) throws UserNotFoundException {
        Optional<JpaUser> userDAO = jpaUserRepository.findById(id);

        if (userDAO.isPresent()) {
            return userDAO.get().getMemberships().stream().map(userMember -> (GroupMemberDAO) userMember).collect(Collectors.toSet());
        }
        throw new UserNotFoundException("id=" + id);
    }

    @Override
    public void changePassword(UserDAO user, String oldPassword, String newPassword) throws CryptographyException, InvalidPasswordException {

        try {
            user.setPrivateKey(
                    cryptographyService.pbeEncrypt(newPassword,
                            cryptographyService.pbeDecrypt(oldPassword, user.getPrivateKey())).getEncoded());
            save(user);
        }catch(InvalidKeySpecException e){
            throw new InvalidPasswordException();
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidAlgorithmParameterException | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | InvalidParameterSpecException | IOException e) {
            throw new CryptographyException(e);
        }
    }

    @Override
    public UserDAO save(UserDAO user) throws UserNotFoundException {

        UserDAO activeUserDAO = findByUserName(authenticationFacade.getAuthentication().getName());

        user.setLastUpdatedByUser(activeUserDAO);
        user.setLastUpdatedStamp(Instant.now());

        return jpaUserRepository.save((JpaUser) user);
    }

    @Override
    public void deleteById(Long userId){

        jpaUserRepository.deleteById(userId);
    }
}
