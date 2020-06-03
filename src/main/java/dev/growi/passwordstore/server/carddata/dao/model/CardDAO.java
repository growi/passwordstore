package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlList;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaCardCollection;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaContent;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaTag;
import dev.growi.passwordstore.server.core.base.dao.Dao;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;

import java.util.Set;

public interface CardDAO extends RestrictedDAO {

    Long getId();

    void setId(Long id);

    String getTitle();

    void setTitle(String title);

    Set<JpaContent> getContents();

    void setContents(Set<JpaContent> contents);

    Set<JpaTag> getTags();

    void setTags(Set<JpaTag> tags);

    Set<JpaCardCollection> getCollections();

    void setCollections(Set<JpaCardCollection> collections);
}
