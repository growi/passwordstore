package dev.growi.passwordstore.server.carddata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.carddata.dao.model.AccessControlListDAO;
import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import dev.growi.passwordstore.server.shared.domain.Monitored;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Set;
import java.util.stream.Collectors;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = false, autowire = Autowire.BY_TYPE)
public class AccessControlList extends Monitored<AccessControlList, AccessControlListDAO> {

    private Long id;
    private Set<AccessControlEntry> aces;

    public AccessControlList(AccessControlListDAO aclDAO){
        super(aclDAO);
        this.id = aclDAO.getId();
        this.aces = aclDAO.getEntries().stream().map(dao -> new AccessControlEntry(dao)).collect(Collectors.toSet());
    }

    @Override
    protected void setProperties(AccessControlListDAO carddaoDAO) {

    }

    @Override
    public void update(AccessControlList template, boolean ignoreNull) {

    }

    @Override
    protected AccessControlListDAO getDao() throws EntityNotFoundException {
        return null;
    }

    @Override
    protected AccessControlListDAO updateDao(AccessControlListDAO dao) {
        return null;
    }

    @Override
    public AccessControlList save() throws EntityNotFoundException {
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

    public Set<AccessControlEntry> getAces() {
        return aces;
    }

    @Override
    public String toString() {
        return "AccessControlList{" +
                "id=" + id +
                ", aces=" + aces +
                '}';
    }
}
