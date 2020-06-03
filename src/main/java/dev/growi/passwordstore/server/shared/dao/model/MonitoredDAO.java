package dev.growi.passwordstore.server.shared.dao;

import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import java.time.Instant;

public interface MonitoredDAO {

    UserDAO getCreatedByUser();

    void setCreatedByUser(UserDAO createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    UserDAO getLastUpdatedByUser();

    void setLastUpdatedByUser(UserDAO lastUpdatedByUser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);
}
