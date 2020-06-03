package dev.growi.passwordstore.server.carddata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.*;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository.*;
import dev.growi.passwordstore.server.carddata.dao.model.*;
import dev.growi.passwordstore.server.carddata.dao.provider.AccessControlDataProvider;
import dev.growi.passwordstore.server.carddata.dao.provider.CardCollectionDataProvider;
import dev.growi.passwordstore.server.core.authentication.AuthenticationFacade;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.dataprovider.JpaMonitoreddDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JpaCardCollectionDataProvider extends JpaMonitoreddDataProvider<Long, CardCollectionDAO> implements CardCollectionDataProvider {


    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private AccessControlDataProvider accessControlDataProvider;

    @Autowired
    private JpaCollectionRepository jpaCollectionRepository;

    @Override
    public List<CardCollectionDAO> findAll() {
        return null;
    }

    @Override
    public CardCollectionDAO findById(Long id)  {
        return null;
    }

    @Override
    @Transactional
    public CardCollectionDAO create() throws UserNotFoundException, DatasourceException {

        UserDAO user = userDataProvider.findByUserName(authenticationFacade.getAuthentication().getName());

        JpaCardCollection collection = new JpaCardCollection();
        this.setCreatedInfo(collection);
        this.setLastUpdatedInfo(collection);
        collection.setAcl(accessControlDataProvider.create(user, user));
        collection.getAcl().setObject(collection);
        collection = jpaCollectionRepository.save(collection);

        return collection;
    }

    @Override
    public CardCollectionDAO save(CardCollectionDAO dao) throws UserNotFoundException {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
