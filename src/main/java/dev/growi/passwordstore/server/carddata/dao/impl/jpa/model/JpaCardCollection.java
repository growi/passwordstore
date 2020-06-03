package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.CollectionDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "collection")
public class JpaCollection extends JpaRestricted implements CollectionDAO {

    private String title;

    private String description;

    @ManyToMany
    private Set<JpaCard> cards = new HashSet<>();

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public Set<JpaCard> getCards() {
        return cards;
    }

    @Override
    public void setCards(Set<JpaCard> cards) {
        this.cards = cards;
    }
}
