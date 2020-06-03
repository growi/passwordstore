package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.persistence.*;

@Entity(name = "user")
public class JpaUser extends JpaPrincipal implements UserDAO {

    @Column(nullable = false, unique = true, length = 50)
    private String userName;
    private String password;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;

    public JpaUser(){}

    public JpaUser(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    @Lob
    @Column(name = "privatekey", columnDefinition="BLOB")
    private byte[] privateKey;

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountExpired() {
        return this.accountExpired;
    }

    @Override
    public void isAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    @Override
    public boolean isAccountLocked() {
        return this.accountLocked;
    }

    @Override
    public void isAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Override
    public boolean isCredentialsExpired() {
        return this.credentialsExpired;
    }

    @Override
    public void isCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    @Override
    public byte[] getPrivateKey(){
        return this.privateKey;
    }

    @Override
    public void setPrivateKey(byte[] privateKey){
        this.privateKey = privateKey;
    }
}
