package dev.growi.passwordstore.server.carddata.dao.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Card {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long cardId;

    @OneToOne
    private AccessControlList acl;
    @OneToMany
    private Set<CardContent> contents = new HashSet<>();

}
