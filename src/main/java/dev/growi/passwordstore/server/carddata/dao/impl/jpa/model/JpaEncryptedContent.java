package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO;

import javax.persistence.*;

@Entity( name = "card_content")
public abstract class JpaCardContent implements CardContentDAO {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long cardContentId;

    @ManyToOne
    private JpaCard card;

    private int sequenceNum;
    private String label;
    private String type;

    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] value;

    @Override
    public Long getCardContentId() {
        return cardContentId;
    }

    @Override
    public void setCardContentId(Long cardContentId) {
        this.cardContentId = cardContentId;
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
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public void setValue(byte[] value) {
        this.value = value;
    }
}
