package dev.growi.passwordstore.server.carddata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.carddata.dao.exception.CardNotFoundException;
import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO.ContentType;
import dev.growi.passwordstore.server.carddata.dao.model.CardDAO;
import dev.growi.passwordstore.server.carddata.dao.provider.CardDataProvider;
import dev.growi.passwordstore.server.shared.dao.model.MonitoredDAO;
import dev.growi.passwordstore.server.shared.domain.Monitored;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.userdetails.UserDetails;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = false, autowire = Autowire.BY_TYPE)
public class Card extends Monitored<Card, CardDAO> {

    @Autowired
    private CardDataProvider cardDataProvider;

    private Long  id;
    private String title;

    public static Card load(Long id) throws CardNotFoundException{
        Card card = new Card();
        card.setProperties(card.cardDataProvider.findById(id));

        return card;
    }

    public static Card create(Card template) throws UserNotFoundException, DatasourceException, CardNotFoundException {
        Card card = create(template.title);
        card.update(template, true);

        return card.save();
    }

    public static Card create(String title) throws UserNotFoundException, DatasourceException {
        Card card = new Card();
        card.setProperties(card.cardDataProvider.create(title));

        return card;
    }

    private Card(){};

    public Card(CardDAO cardDAO){
        setProperties(cardDAO);
    }

    @Override
    protected void setProperties(CardDAO dao) {
        super.setProperties(dao);
        this.id = dao.getId();
        this.title = dao.getTitle();
    }

    @Override
    public void update(Card template, boolean ignoreNull) {
        if(!ignoreNull || template.getTitle() != null) this.setTitle(template.getTitle());
    }

    @Override
    protected CardDAO getDao() throws CardNotFoundException {
        CardDAO dao = cardDataProvider.findById(this.getId());
        updateDao(dao);

        return dao;
    }

    @Override
    protected CardDAO updateDao(CardDAO dao){
        dao.setTitle(this.getTitle());

        return dao;
    }

    @Override
    public Card save() throws UserNotFoundException, CardNotFoundException {
        this.setProperties(cardDataProvider.save(getDao()));
        return this;
    }

    public void delete(){
        cardDataProvider.deleteById(this.getId());
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

    public AccessControlList getAccessControlList() throws CardNotFoundException {
        return new AccessControlList(cardDataProvider.findById(this.id).getAcl());
    }

    public void createContent(ContentType contentType, Object value) throws CardNotFoundException {

        CardDAO cardDAO = cardDataProvider.findById(this.id);
        cardDataProvider.createCardContent(cardDAO, contentType, value);
    }

    public void addContent(){

    }

    public void removeContent(){

    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
