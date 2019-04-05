package dev.growi.passwordstore.server.userdata.jpa.dao;

import dev.growi.passwordstore.server.userdata.Principal;
import dev.growi.passwordstore.server.userdata.User;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.security.PublicKey;
import java.time.Instant;

@MappedSuperclass
public class PrincipalDAO implements Principal {

    private boolean enabled;
    private Instant createdStamp;
    private Instant lastUpdatedStamp;

    @Lob
    @Column(name = "publickey", columnDefinition="BLOB")
    private byte[] publicKey;

    @ManyToOne
    private UserDAO createdByUser;

    @ManyToOne
    private UserDAO lastUpdatedByUser;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void isEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public PublicKey getPublicKey() {
        return null;
    }

    @Override
    public void setPublicKey(PublicKey publicKey) {

    }

    @Override
    public User getCreatedByUser() {
        return this.createdByUser;
    }

    @Override
    public void setCreatedByUser(User createdByUser) {
        this.createdByUser = (UserDAO) createdByUser;
    }

    @Override
    public Instant getCreatedStamp() {
        return this.createdStamp;
    }

    @Override
    public void setCreatedStamp(Instant createdStamp) {
        this.createdStamp = createdStamp;
    }

    @Override
    public User getLastUpdatedByUser() {
        return this.lastUpdatedByUser;
    }

    @Override
    public void setLastUpdatedByUsr(User lastUpdatedByUser) {
        this.lastUpdatedByUser = (UserDAO) lastUpdatedByUser;
    }

    @Override
    public Instant getLastUpdatedStamp() {
        return this.lastUpdatedStamp;
    }

    @Override
    public void setLastUpdatedStamp(Instant lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }
}
