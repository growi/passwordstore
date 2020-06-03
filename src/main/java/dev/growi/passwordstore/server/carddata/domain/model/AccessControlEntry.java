package dev.growi.passwordstore.server.carddata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.carddata.dao.model.AccessControlEntryDAO;
import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import dev.growi.passwordstore.server.shared.domain.Monitored;
import dev.growi.passwordstore.server.userdata.dao.model.GroupDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.domain.model.Group;
import dev.growi.passwordstore.server.userdata.domain.model.Principal;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = false, autowire = Autowire.BY_TYPE)
public class AccessControlEntry extends Monitored<AccessControlEntry, AccessControlEntryDAO> {

    private Long id;
    private int permissions;
    private Principal principal;
    private String secret;

    public AccessControlEntry(AccessControlEntryDAO aceDAO) {
        super(aceDAO);
        this.id = aceDAO.getId();
        this.permissions = aceDAO.getPermission();
        this.principal = UserDAO.class.isAssignableFrom(aceDAO.getPrincipal().getClass()) ?
                new User((UserDAO) aceDAO.getPrincipal()) :
                new Group((GroupDAO) aceDAO.getPrincipal());
    }

    @Override
    protected void setProperties(AccessControlEntryDAO carddaoDAO) {

    }

    @Override
    public void update(AccessControlEntry template, boolean ignoreNull) {

    }

    @Override
    protected AccessControlEntryDAO getDao() throws EntityNotFoundException {
        return null;
    }

    @Override
    protected AccessControlEntryDAO updateDao(AccessControlEntryDAO dao) {
        return null;
    }

    @Override
    public AccessControlEntry save() throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public Principal getPrincipal() {
        return principal;
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public String toString() {
        return "AccessControlEntry{" +
                "id=" + id +
                ", permissions=" + permissions +
                ", principal=" + principal +
                ", secret='" + secret + '\'' +
                '}';
    }
}
