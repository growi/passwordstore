package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import javax.persistence.*;

@Entity( name = "card_encrypted_content")
public  class JpaEncryptedContent extends JpaByteContent {

    private ContentType type;

    public JpaEncryptedContent(ByteArrayContentWrapper contentWrapper) {
        super(contentWrapper);
    }

    public ContentType getType() {
        return type;
    }

    public void setType(ContentType type) {
        this.type = type;
    }

    @Override
    public boolean isEncrypted() {
        return true;
    }
}
