package dev.growi.passwordstore.server.carddata.dao.provider;

import dev.growi.passwordstore.server.carddata.dao.exception.CardNotFoundException;
import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO;
import dev.growi.passwordstore.server.carddata.dao.model.CardDAO;
import dev.growi.passwordstore.server.shared.dao.provider.MonitoredDataProvider;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;

import java.util.List;

public interface CardDataProvider extends MonitoredDataProvider<Long, CardDAO> {

    List<? extends CardDAO> findAll();

    CardDAO findById(Long id) throws CardNotFoundException;

    CardDAO create(String title) throws UserNotFoundException, DatasourceException;

    CardContentDAO createCardContent(CardDAO card, CardContentDAO.ContentType contentType, Object value);

    CardDAO save(CardDAO dao) throws UserNotFoundException;

    void deleteById(Long id);
}
