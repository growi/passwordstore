package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import javax.persistence.*;

@Entity( name = "card_encrypted_content")
public  class JpaEncryptedContent extends JpaContent {

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

    public enum Type {
        STRING,
        TEXT,
        FILE
    }
}
