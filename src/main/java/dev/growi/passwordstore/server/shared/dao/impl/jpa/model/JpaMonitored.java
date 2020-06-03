package dev.growi.passwordstore.server.shared.impl.jpa;

import dev.growi.passwordstore.server.shared.dao.MonitoredDAO;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
public abstract class JpaMonitored implements MonitoredDAO {

    @ManyToOne
    private JpaUser createdByUser;

    @ManyToOne
    private JpaUser lastUpdatedByUser;

    private Instant createdStamp;
    private Instant lastUpdatedStamp;

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
