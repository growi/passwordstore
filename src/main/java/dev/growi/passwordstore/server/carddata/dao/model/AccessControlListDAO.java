package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;

import java.util.Set;

public interface AccessControlListDAO extends MonitoredDAO {

    Long getId();

    void setId(Long id);

    RestrictedDAO getObject();

    void setObject(RestrictedDAO card);

    <T extends AccessControlEntryDAO> Set<T> getEntries();

    <T extends AccessControlEntryDAO> void setEntries(Set<T> entries);
}
