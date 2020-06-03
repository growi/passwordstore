package dev.growi.passwordstore.server.carddata.dao.model;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AccessControlList {

    @OneToOne
    private Card card;
    @OneToMany
    private Set<AccessControlListGroupEntry> groups = new HashSet<>();
    @OneToMany
    private Set<AccessControlListUserEntry> gusers = new HashSet<>();

}
