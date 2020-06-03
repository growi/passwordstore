package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO;
import dev.growi.passwordstore.server.carddata.dao.model.ContentWrapper;

import javax.persistence.*;

@Entity( name = "card_content")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class JpaContent implements CardContentDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private JpaCard card;

    private int sequenceNum;
    private String label;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public JpaCard getCard() {
        return card;
    }

    @Override
    public void setCard(JpaCard card) {
        this.card = card;
    }

    @Override
    public int getSequenceNum() {
        return sequenceNum;
    }

    @Override
    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public abstract ContentWrapper<?> getContent();

    @Override
    public abstract void setContent(ContentWrapper<?> contentWrapper);

}
