package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.AccessControlEntryDAO;
import dev.growi.passwordstore.server.carddata.dao.model.AccessControlListDAO;
import dev.growi.passwordstore.server.carddata.dao.model.RestrictedDAO;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "access_control_list")
public class JpaAccessControlList extends JpaMonitored implements AccessControlListDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "acl")
    private JpaRestricted object;
    @OneToMany(mappedBy = "acl", cascade = CascadeType.ALL)
    private Set<JpaAccessControlEntry> entries = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public RestrictedDAO getObject() {
        return object;
    }

    @Override
    public void setObject(RestrictedDAO object) {
        this.object = (JpaRestricted) object;
    }

    @Override
    public <T extends AccessControlEntryDAO> Set<T> getEntries() {
        return (Set<T>) this.entries;
    }

    @Override
    public <T extends AccessControlEntryDAO> void setEntries(Set<T> entries) {
        this.entries = (Set<JpaAccessControlEntry>) entries;
    }

}
