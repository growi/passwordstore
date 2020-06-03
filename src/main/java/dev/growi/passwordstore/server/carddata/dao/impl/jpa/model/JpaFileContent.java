package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import javax.persistence.Entity;

@Entity( name = "card_file_content")
public class JpaFileContent extends JpaByteContent {

    public JpaFileContent(JpaByteContent.ByteArrayContentWrapper contentWrapper){
        super(contentWrapper);
    }

    @Override
    public ContentType getType() {
        return ContentType.FILE;
    }

    @Override
    public boolean isEncrypted() {
        return false;
    }
}
