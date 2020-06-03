package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity( name = "card_encrypted_content")
public abstract class JpaTextContent extends JpaContent {

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
