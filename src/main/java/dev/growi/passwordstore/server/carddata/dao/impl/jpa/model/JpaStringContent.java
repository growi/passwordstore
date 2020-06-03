package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO;

import javax.persistence.*;

@Entity( name = "card_encrypted_content")
public abstract class JpaEncryptedContent extends JpaContent {

    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] value;

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public void setValue(byte[] value) {
        this.value = value;
    }
}
