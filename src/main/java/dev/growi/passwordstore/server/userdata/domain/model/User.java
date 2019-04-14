package dev.growi.passwordstore.server.userdata.domain.model;

import com.fasterxml.jackson.annotation.*;
import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.service.CryptographyService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userName")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = true, autowire = Autowire.BY_TYPE)
public class User extends Principal {

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private GroupDataProvider groupDataProvider;

    @Autowired
    CryptographyService cryptographyService;

    private String userName;
    private EncryptedPrivateKeyInfo privateKey;

    public User(UserDAO userDAO) {
        super(userDAO);
        this.id = userDAO.getUserId();
        this.userName = userDAO.getUserName();
        try {
            this.privateKey = cryptographyService.createPBEKeyInfo(userDAO.getPrivateKey());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public EncryptedPrivateKeyInfo getPrivateKey() {
        return this.privateKey;
    }

    @JsonIgnore
    public Set<Group> getMemberships() throws UserNotFoundException {
        return userDataProvider.findGroupMembershipsByUserId(this.id)
                .stream()
                .map(member -> new Group(member.getGroup()))
                .collect(Collectors.toSet());
    }

    public void changePassword(String oldPassword, String newPassword) throws IOException, CryptographyException {

        UserDAO user = userDataProvider.findUserById(this.getId());
        userDataProvider.changePassword(user, oldPassword, newPassword);

    }
}
