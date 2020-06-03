package dev.growi.passwordstore.server.carddata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.carddata.dao.exception.CardNotFoundException;
import dev.growi.passwordstore.server.carddata.dao.model.CardCollectionDAO;
import dev.growi.passwordstore.server.carddata.dao.provider.CardCollectionDataProvider;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.shared.domain.Monitored;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = false, autowire = Autowire.BY_TYPE)
public class CardCollection extends Monitored<CardCollection, CardCollectionDAO> {

    @Autowired
    private CardCollectionDataProvider cardCollectionDataProvider;

    private Long id;
    private String title;
    private String description;

    public static CardCollection load(Long id){
        CardCollection cardCollection = new CardCollection();
        cardCollection.setProperties(cardCollection.cardCollectionDataProvider.findById(id));

        return cardCollection;
    }

    public static CardCollection create(CardCollection template) throws UserNotFoundException, DatasourceException {
        CardCollection cardCollection = create();
        cardCollection.update(template, true);

        return cardCollection;
    }

    public static CardCollection create() throws UserNotFoundException, DatasourceException {
        CardCollection cardCollection = new CardCollection();
        cardCollection.setProperties(cardCollection.cardCollectionDataProvider.create());

        return cardCollection;
    }

    private CardCollection(){

    }

    public CardCollection(CardCollectionDAO cardCollectionDAO){
        super(cardCollectionDAO);
        this.id = cardCollectionDAO.getId();
        this.title = cardCollectionDAO.getTitle();
        this.description = cardCollectionDAO.getDescription();
    }

    @Override
    protected void setProperties(CardCollectionDAO dao) {
        super.setProperties(dao);
        this.id = dao.getId();
        this.title = dao.getTitle();
        this.description = dao.getDescription();
    }

    @Override
    public void update(CardCollection template, boolean ignoreNull) {
        if(!ignoreNull || template.getTitle() != null) this.setTitle(template.getTitle());
        if(!ignoreNull || template.getDescription() != null) this.setDescription(template.getDescription());
    }

    @Override
    protected CardCollectionDAO getDao() throws CardNotFoundException {
        CardCollectionDAO dao  = cardCollectionDataProvider.findById(this.getId());
        updateDao(dao);

        return dao;
    }

    @Override
    protected CardCollectionDAO updateDao(CardCollectionDAO dao) {
        dao.setTitle(this.getTitle());
        dao.setDescription(this.getDescription());

        return dao;
    }

    @Override
    public CardCollection save() throws CardNotFoundException, UserNotFoundException {
        this.setProperties(cardCollectionDataProvider.save(this.getDao()));

        return this;
    }

    @Override
    public void delete() {
        cardCollectionDataProvider.deleteById(this.getId());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CardCollection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
