package dev.growi.passwordstore.server.shared.dao.provider;

import dev.growi.passwordstore.server.core.base.dao.DataProvider;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

public interface MonitoredDataProvider<I, D extends MonitoredDAO> extends DataProvider<I, D> {
    MonitoredDAO setCreatedInfo(MonitoredDAO monitored) throws UserNotFoundException;

    MonitoredDAO setLastUpdatedInfo(MonitoredDAO monitored) throws UserNotFoundException;
}
