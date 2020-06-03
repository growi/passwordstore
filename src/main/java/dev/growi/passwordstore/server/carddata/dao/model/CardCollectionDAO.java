package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaCard;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;

import java.util.Set;

public interface CollectionDAO extends MonitoredDAO {
    Long getId();

    void setId(Long id);

    String getTitle();

    void setTitle(String title);

    String getDescription();

    void setDescription(String description);

    Set<JpaCard> getCards();

    void setCards(Set<JpaCard> cards);
}
