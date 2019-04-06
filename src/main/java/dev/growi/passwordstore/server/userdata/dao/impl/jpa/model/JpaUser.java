package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.crypto.EncryptedPrivateKeyInfo;
import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "user")
public class JpaUser extends JpaPrincipal implements UserDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long userId;
    @Column(nullable = false, unique = true, length = 50)
    private String userName;
    private String password;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;

    @Lob
    @Column(name = "privatekey", columnDefinition="BLOB")
    private byte[] privateKey;

    @Override
    public IdWrapper<?> getUserId() {
        return new UserId();
    }

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

    public class UserId implements IdWrapper<Long>, Serializable {

        public UserId() {}

        @Override
        public Class<Long> getIdClass() {
            return Long.class;
        }

        @Override
        public Long getValue() {
            return userId;
        }

        @Override
        public void setValue(Long newUserId) {
            userId = newUserId;
        }
    }
}
