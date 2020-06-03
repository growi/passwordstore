package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.AccessControlEntryDAO;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaPrincipal;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;

import javax.persistence.*;

@Entity(name = "access_control_entry")
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaAccessControlEntry extends JpaMonitored implements AccessControlEntryDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JpaPrincipal principal;

    @ManyToOne
    private JpaAccessControlList acl;

    @Lob
    @Column(columnDefinition = "BLOB")
    private byte[] secret;
    private int permission;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public PrincipalDAO getPrincipal() {
        return principal;
    }

    @Override
    public void setPrincipal(PrincipalDAO principal) {
        this.principal = (JpaPrincipal) principal;
    }

    @Override
    public JpaAccessControlList getAcl() {
        return acl;
    }

    @Override
    public void setAcl(JpaAccessControlList acl) {
        this.acl = acl;
    }

    @Override
    public byte[] getSecret() {
        return secret;
    }

    @Override
    public void setSecret(byte[] secret) {
        this.secret = secret;
    }

    @Override
    public int getPermission() {
        return permission;
    }

    @Override
    public void setPermission(int permission) {
        this.permission = permission;
    }
}
