package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.CardDAO;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "card")
public class JpaCard extends JpaRestricted implements CardDAO {

    private String title;

    @OneToMany( mappedBy = "card" )
    private Set<JpaContent> contents = new HashSet<>();

    @ManyToMany
    private Set<JpaTag> tags = new HashSet<>();

    @ManyToMany
    private Set<JpaCardCollection> collections = new HashSet<>();

    @Override
    public String getTitle(){
        return this.title;
    }

    @Override
    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public Set<JpaContent> getContents() {
        return contents;
    }

    @Override
    public void setContents(Set<JpaContent> contents) {
        this.contents = contents;
    }

    @Override
    public Set<JpaTag> getTags() {
        return tags;
    }

    @Override
    public void setTags(Set<JpaTag> tags) {
        this.tags = tags;
    }

    @Override
    public Set<JpaCardCollection> getCollections() {
        return collections;
    }

    @Override
    public void setCollections(Set<JpaCardCollection> collections) {
        this.collections = collections;
    }
}
