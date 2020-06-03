package dev.growi.passwordstore.server.userdata.domain.model;

import com.fasterxml.jackson.annotation.*;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.exception.InvalidPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.EncryptedPrivateKeyInfo;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "userName")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends Principal<User, UserDAO> {

    @Autowired
    private UserDataProvider userDataProvider;

    private String userName;
    private Boolean isAccountLocked;
    private EncryptedPrivateKeyInfo privateKey;

    public static User load(String userName) throws UserNotFoundException, CryptographyException {

        User user = new User();
        user.setProperties(user.userDataProvider.findByUserName(userName));

        return user;
    }

    public static User load(Long id) throws UserNotFoundException, CryptographyException {
        User user = new User();

        UserDAO dao = user.userDataProvider.findById(id);
        user.setProperties(user.userDataProvider.findById(id));

        return user;
    }

    public static User create(User template, String password) throws UserNotFoundException, CryptographyException {
        User user = create(template.getUserName(), password);
        user.update(template, true);

        return user.save();
    }

    public static User create(String userName, String password) throws UserNotFoundException, CryptographyException {
        User user = new User();
        user.setProperties(user.userDataProvider.create(userName, password));

        return user;
    }

    private User(){}

    public User(UserDAO userDAO) {
        setProperties(userDAO);
    }

    @Override
    protected void setProperties(UserDAO userDAO) {
        super.setProperties(userDAO);
        this.id = userDAO.getId();
        this.userName = userDAO.getUserName();
        this.isAccountLocked = userDAO.isAccountLocked();
        try {
            this.privateKey = cryptographyService.createPBEKeyInfo(userDAO.getPrivateKey());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // id and monitored fields wont be updated!
    @Override
    public void update(User template, boolean ignoreNull){
        super.update(template, ignoreNull);
        if(!ignoreNull || template.getUserName() != null) this.userName = template.getUserName();
        if(!ignoreNull || template.isAccountLocked() != null) this.isAccountLocked = template.isAccountLocked();
        if(!ignoreNull || template.getPrivateKey() != null) this.privateKey = template.getPrivateKey();
    }

    @Override
    protected UserDAO getDao() throws UserNotFoundException {
        UserDAO dao = userDataProvider.findById(this.getId());
        updateDao(dao);

        return dao;
    }

    // id and monitored fields wont be updated!
    @Override
    protected UserDAO updateDao(UserDAO dao) {
        super.updateDao(dao);
        dao.setUserName(this.getUserName());
        try {
            dao.setPrivateKey(this.getPrivateKey().getEncoded());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dao.isAccountExpired(this.isAccountLocked());

        return dao;
    }

    @Override
    public User save() throws UserNotFoundException {
        this.setProperties(userDataProvider.save(getDao()));
        return this;
    }

    @Override
    public void delete() {
        userDataProvider.deleteById(this.getId());
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean isAccountLocked() {
        return isAccountLocked;
    }

    public void setAccountLocked(Boolean accountLocked) {
        isAccountLocked = accountLocked;
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

    public void changePassword(String oldPassword, String newPassword, UserDetails activeUser) throws CryptographyException, UserNotFoundException, InvalidPasswordException {

        UserDAO user = userDataProvider.findById(this.getId());
        userDataProvider.changePassword(user, oldPassword, newPassword);
    }
}
