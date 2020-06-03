package dev.growi.passwordstore.server.carddata.dao.provider;

import dev.growi.passwordstore.server.carddata.dao.model.AccessControlEntryDAO;
import dev.growi.passwordstore.server.carddata.dao.model.AccessControlListDAO;
import dev.growi.passwordstore.server.carddata.dao.model.RestrictedDAO;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;

import java.util.List;

public interface AccessControlDataProvider {

    List<? extends AccessControlListDAO> findAll();

    AccessControlListDAO findByObject(RestrictedDAO restrictedDAO);

    AccessControlListDAO create(PrincipalDAO principal, UserDAO user) throws UserNotFoundException, DatasourceException;

    AccessControlEntryDAO createAccessControlEntry(PrincipalDAO principal, int permissions) throws UserNotFoundException, DatasourceException;
}
