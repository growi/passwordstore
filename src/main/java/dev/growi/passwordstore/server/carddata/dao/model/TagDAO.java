package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaCard;
import dev.growi.passwordstore.server.core.base.dao.Dao;

import java.util.Set;

public interface TagDAO extends Dao {
    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    Set<JpaCard> getTaggedCards();

    void setTaggedCards(Set<JpaCard> taggedCards);
}
