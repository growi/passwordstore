package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.persistence.*;
import java.time.Instant;

@MappedSuperclass
public class JpaPrincipal implements PrincipalDAO {

    private boolean enabled;
    private Instant createdStamp;
    private Instant lastUpdatedStamp;

    @Lob
    @Column(name = "publickey", columnDefinition="BLOB")
    private byte[] publicKey;

    @ManyToOne
    private JpaUser createdByUser;

    @ManyToOne
    private JpaUser lastUpdatedByUser;

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void isEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public byte[] getPublicKey(){
        return this.publicKey;
    }

    @Override
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public UserDAO getCreatedByUser() {
        return this.createdByUser;
    }

    @Override
    public void setCreatedByUser(UserDAO createdByUser) {
        this.createdByUser = (JpaUser) createdByUser;
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
    public UserDAO getLastUpdatedByUser() {
        return this.lastUpdatedByUser;
    }

    @Override
    public void setLastUpdatedByUser(UserDAO lastUpdatedByUser) {
        this.lastUpdatedByUser = (JpaUser) lastUpdatedByUser;
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
