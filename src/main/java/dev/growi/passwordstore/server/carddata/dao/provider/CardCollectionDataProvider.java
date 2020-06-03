package dev.growi.passwordstore.server.carddata.dao.provider;

import dev.growi.passwordstore.server.carddata.dao.model.CardCollectionDAO;
import dev.growi.passwordstore.server.shared.dao.provider.MonitoredDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;

import java.util.List;

public interface CardCollectionDataProvider extends MonitoredDataProvider<Long, CardCollectionDAO> {

    List<CardCollectionDAO> findAll();

    CardCollectionDAO findById(Long id) ;

    CardCollectionDAO create() throws UserNotFoundException,  DatasourceException;

    CardCollectionDAO save(CardCollectionDAO dao) throws UserNotFoundException;

    void deleteById(Long id);
}
