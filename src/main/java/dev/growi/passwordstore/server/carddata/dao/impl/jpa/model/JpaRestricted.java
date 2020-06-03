package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.AccessControlListDAO;
import dev.growi.passwordstore.server.carddata.dao.model.RestrictedDAO;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;

import javax.persistence.*;

@Entity(name = "restricted_object")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class JpaRestricted extends JpaMonitored implements RestrictedDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    private JpaAccessControlList acl;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public AccessControlListDAO getAcl() {
        return acl;
    }

    @Override
    public void setAcl(AccessControlListDAO acl) {
        this.acl = (JpaAccessControlList) acl;
    }

}
