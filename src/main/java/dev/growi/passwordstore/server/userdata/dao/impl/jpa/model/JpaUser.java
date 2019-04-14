package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import dev.growi.passwordstore.server.userdata.domain.model.IdWrapper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="userMemberPK.member")
    private Set<JpaUserMember> memberships = new HashSet<>();

    public JpaUser(){}

    public JpaUser(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

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

    @Override
    public Set<JpaUserMember> getMemberships(){
        return this.memberships;
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

        @Override
        public boolean equals(Object obj) {
            return this.getValue() != null && this.getValue().equals(((UserId)obj).getValue());
        }

        @Override
        public int hashCode(){
            return this.getValue() != null ? this.getValue().hashCode() : 0;
        }
    }
}
