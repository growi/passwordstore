package dev.growi.passwordstore.server.shared.dao.model;

import dev.growi.passwordstore.server.core.base.dao.Dao;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import java.time.Instant;

public interface MonitoredDAO extends Dao {

    UserDAO getCreatedByUser();

    void setCreatedByUser(UserDAO createdByUser);

    Instant getCreatedStamp();

    void setCreatedStamp(Instant createdStamp);

    UserDAO getLastUpdatedByUser();

    void setLastUpdatedByUser(UserDAO lastUpdatedByUser);

    Instant getLastUpdatedStamp();

    void setLastUpdatedStamp(Instant lastUpdatedStamp);
}
