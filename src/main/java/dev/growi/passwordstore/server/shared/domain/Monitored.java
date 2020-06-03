package dev.growi.passwordstore.server.shared.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.growi.passwordstore.server.core.base.dao.Dao;
import dev.growi.passwordstore.server.core.base.domain.Model;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class Monitored<M extends Monitored,D extends MonitoredDAO> extends Model<M, D> {

    private String createdByUser;
    private Instant createdStamp;
    private String lastUpdatedUser;
    private Instant lastUpdatedStamp;

    protected Monitored(){

    }

    public Monitored(UserDetails user) throws UserNotFoundException {

        Instant now = Instant.now();

        this.createdByUser = user.getUsername();
        this.createdStamp = now;
        this.lastUpdatedUser = user.getUsername();
        this.lastUpdatedStamp = now;
    }

    public Monitored(D monitoredDAO){
        this.setProperties(monitoredDAO);
    }

    protected void setProperties(D monitoredDAO){
        this.createdByUser = monitoredDAO.getCreatedByUser() != null ? monitoredDAO.getCreatedByUser().getUserName() : null;
        this.createdStamp = monitoredDAO.getCreatedStamp();
        this.lastUpdatedUser = monitoredDAO.getLastUpdatedByUser() != null ? monitoredDAO.getLastUpdatedByUser().getUserName() : null;
        this.lastUpdatedStamp = monitoredDAO.getLastUpdatedStamp();
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public Instant getCreatedStamp() {
        return createdStamp;
    }

    public void setCreatedStamp(Instant createdStamp) {
        this.createdStamp = createdStamp;
    }

    public String getLastUpdatedUser() {
        return lastUpdatedUser;
    }

    public void setLastUpdatedUser(String lastUpdatedUser) {
        this.lastUpdatedUser = lastUpdatedUser;
    }

    public Instant getLastUpdatedStamp() {
        return lastUpdatedStamp;
    }

    public void setLastUpdatedStamp(Instant lastUpdatedStamp) {
        this.lastUpdatedStamp = lastUpdatedStamp;
    }
}
