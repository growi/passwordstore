package dev.growi.passwordstore.server.carddata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO;
import dev.growi.passwordstore.server.carddata.dao.model.CardDAO;
import dev.growi.passwordstore.server.carddata.dao.model.ContentWrapper;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

import java.time.Instant;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = true, autowire = Autowire.BY_TYPE)
public class CardContent {

    private Long id;
    private int sequenceNum;
    private String label;
    private ContentWrapper<?> content;
    private CardContentDAO.ContentType type;
    private boolean isEncrypted;

    public CardContent(CardContentDAO cardContentDAO){

        this.id = cardContentDAO.getId();
        this.sequenceNum = cardContentDAO.getSequenceNum();
        this.label = cardContentDAO.getLabel();
        this.content = cardContentDAO.getContent();
        this.type = cardContentDAO.getType();
        this.isEncrypted = cardContentDAO.isEncrypted();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ContentWrapper<?> getContent() {
        return content;
    }

    public void setContent(ContentWrapper<?> content) {
        this.content = content;
    }

    public CardContentDAO.ContentType getType() {
        return type;
    }

    public void setType(CardContentDAO.ContentType type) {
        this.type = type;
    }

    public boolean isEncrypted() {
        return isEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        isEncrypted = encrypted;
    }
}
