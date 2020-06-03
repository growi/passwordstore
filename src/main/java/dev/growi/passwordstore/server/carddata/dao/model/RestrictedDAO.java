package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;

public interface RestrictedDAO extends MonitoredDAO {

    Long getId();

    void setId(Long id);

    AccessControlListDAO getAcl();

    void setAcl(AccessControlListDAO acl);
}
