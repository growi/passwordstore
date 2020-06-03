package dev.growi.passwordstore.server.carddata.dao.model;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaCard;

public interface CardContentDAO {

    Long getId();

    void setId(Long id);

    JpaCard getCard();

    void setCard(JpaCard card);

    int getSequenceNum();

    void setSequenceNum(int sequenceNum);

    String getLabel();

    void setLabel(String label);

    ContentWrapper<?> getContent();

    void setContent(ContentWrapper<?> contentWrapper);

    ContentType getType();

    boolean isEncrypted();

    public enum ContentType {
        STRING,
        TEXT,
        FILE
    }
}
