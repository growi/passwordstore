package dev.growi.passwordstore.server.userdata.jpa.dao;

import dev.growi.passwordstore.server.userdata.User;

import javax.crypto.EncryptedPrivateKeyInfo;
import dev.growi.passwordstore.server.userdata.Id;
import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user")
public class UserDAO extends PrincipalDAO implements User {

    @EmbeddedId
    private UserId userId;
    private String userName;
    private String password;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;

    @Lob
    @Column(name = "privatekey", columnDefinition="BLOB")
    private byte[] privateKey;

    @Override
    public Id<?> getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(Id<?> userId)  {
        this.userId = (UserId) userId;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public void setUserName() {
        this.userName = userName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
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
    public EncryptedPrivateKeyInfo getPrivateKey() {
        return null;
    }

    @Override
    public void setPrivateKey(EncryptedPrivateKeyInfo privateKey) {

    }

    @Embeddable
    public static class UserId implements Id<Long>, Serializable {

        private Long userId;

        public UserId() {}

        public UserId(Long userId){
            this.userId = userId;
        }

        @Override
        public Class<Long> getIdClass() {
            return Long.class;
        }

        @Override
        public Long getValue() {
            return this.userId;
        }

        @Override
        public void setValue(Long userId) {
            this.userId = userId;
        }
    }
}
