package dev.growi.passwordstore.server.userdata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.userdata.dao.impl.jpa.configuration.JpaDatasourceCondition;
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

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.time.Instant;
import java.util.Optional;

@Service
@Conditional(JpaDatasourceCondition.class)
public class JpaUserDataProvider implements UserDataProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    JpaUserRepository jpaUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CryptographyService cryptographyService;

    @Autowired
    PasswordGeneratorService passwordGenerator;

    @PostConstruct
    public void checkAdmin() {
        Optional<JpaUser> optUser = jpaUserRepository.findById(1L);

        if (!optUser.isPresent()) {
            String password = passwordGenerator.generate();
            createUser("Admin", password, (UserDAO) null);

            logger.info("Created user 'Admin' with password '" + password + "'");
        }
    }

    @Override
    public UserDAO findUserById(IdWrapper<?> id) throws UserNotFoundException {
        if (!JpaUser.UserId.class.isAssignableFrom(id.getIdClass())) {
            throw new IllegalArgumentException("Id value is of the wrong type. " +
                    "Expected " + JpaUser.UserId.class.getCanonicalName() +
                    " got " + id.getIdClass().getCanonicalName() + ".");
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
                              UserDetails activeUser) throws UserNotFoundException {

        UserDAO activeUserDAO = findUserByUserName(activeUser.getUsername());

        return createUser(userName, password, activeUserDAO);

    }

    private UserDAO createUser(final String userName, final String password, UserDAO activeUserDAO) {
        Instant now = Instant.now();
        String passwordHash = passwordEncoder.encode(password);

        JpaUser user = new JpaUser();

        try {
            KeyPair keyPair = cryptographyService.generateRSAKeyPair();
            EncryptedPrivateKeyInfo privateKey = cryptographyService.pbeEncrypt(password, keyPair.getPrivate());

            user.setPublicKey(keyPair.getPublic());
            user.setPrivateKey(privateKey);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchProviderException | BadPaddingException |
                IllegalBlockSizeException | NoSuchPaddingException | IOException | InvalidParameterSpecException |
                InvalidKeySpecException | InvalidAlgorithmParameterException e) {
            throw new SecurityException(e);
        }

        user.setUserName(userName);
        user.setPassword(passwordHash);
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
}
