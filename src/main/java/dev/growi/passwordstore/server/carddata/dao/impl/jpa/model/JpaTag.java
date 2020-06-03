package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.TagDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "tag")
public class JpaTag implements TagDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany
    Set<JpaCard> taggedCards = new HashSet<>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<JpaCard> getTaggedCards() {
        return taggedCards;
    }

    @Override
    public void setTaggedCards(Set<JpaCard> taggedCards) {
        this.taggedCards = taggedCards;
    }
}
