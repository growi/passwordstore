package dev.growi.passwordstore.server.userdata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlEntry;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "principal")
@Inheritance(strategy = InheritanceType.JOINED)
public class JpaPrincipal extends JpaMonitored implements PrincipalDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy="principal")
    private Set<JpaAccessControlEntry> aces;

    private boolean enabled;

    @OneToMany(cascade=CascadeType.PERSIST, mappedBy="groupMemberPK.member")
    private Set<JpaGroupMember> memberships = new HashSet<>();

    @Lob
    @Column(name = "publickey", columnDefinition="BLOB")
    private byte[] publicKey;

    @Override
    public Long getId() {
        return this.id;
    }


    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void isEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public byte[] getPublicKey(){
        return this.publicKey;
    }

    @Override
    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public Set<JpaGroupMember> getMemberships() {
        return memberships;
    }

    @Override
    public void setMemberships(Set<JpaGroupMember> memberships) {
        this.memberships = memberships;
    }
}
