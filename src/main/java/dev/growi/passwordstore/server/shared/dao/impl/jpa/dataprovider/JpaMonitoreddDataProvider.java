package dev.growi.passwordstore.server.shared.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.core.authentication.AuthenticationFacade;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.shared.dao.provider.SharedDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public abstract class JpaSharedDataProvider<I, D extends MonitoredDAO> implements SharedDataProvider<I, D> {

    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserDataProvider userDataProvider;

    @Override
    public MonitoredDAO setCreatedInfo(MonitoredDAO monitored) throws UserNotFoundException {

        UserDAO activeUserDAO = userDataProvider.findUserByUserName(authenticationFacade.getAuthentication().getName());

        monitored.setCreatedByUser(activeUserDAO);
        monitored.setCreatedStamp(Instant.now());

        return monitored;
    }

    @Override
    public MonitoredDAO setLastUpdatedInfo(MonitoredDAO monitored) throws UserNotFoundException {

        UserDAO activeUserDAO = userDataProvider.findUserByUserName(authenticationFacade.getAuthentication().getName());

        monitored.setLastUpdatedByUser(activeUserDAO);
        monitored.setLastUpdatedStamp(Instant.now());

        return monitored;
    }

}
