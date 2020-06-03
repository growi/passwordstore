package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.ContentWrapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity( name = "card_text_content")
public  class JpaTextContent extends JpaContent {

    @Lob
    @Column(columnDefinition="TEXT")
    private String content;

    public JpaTextContent(JpaTextContent.TextContentWrapper contentWrapper){
        this.setContent(contentWrapper);
    }

    @Override
    public ContentType getType() {
        return ContentType.TEXT;
    }

    @Override
    public boolean isEncrypted() {
        return false;
    }

    @Override
    public ContentWrapper<?> getContent() {
        return null;
    }

    @Override
    public void setContent(ContentWrapper<?> contentWrapper) {
        assert String.class.isAssignableFrom(contentWrapper.getContentClass());

        this.content = (String) contentWrapper.getContent();
    }

    public static class TextContentWrapper implements ContentWrapper<String>{

        private String value;

        public TextContentWrapper(String value){
            this.value = value;
        }

        @Override
        public Class<String> getContentClass() {
            return String.class;
        }

        @Override
        public String getContent() {
            return this.value;
        }
    }
}
