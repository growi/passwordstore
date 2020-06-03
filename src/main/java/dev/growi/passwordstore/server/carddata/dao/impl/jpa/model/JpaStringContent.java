package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.ContentWrapper;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity( name = "card_string_content")
public class JpaStringContent extends JpaContent {

    private String content;

    public JpaStringContent(StringContentWrapper contentWrapper){
        this.setContent(contentWrapper);
    }

    @Override
    public ContentType getType() {
        return ContentType.STRING;
    }

    @Override
    public boolean isEncrypted() {
        return false;
    }

    @Override
    public ContentWrapper<?> getContent() {
        return new StringContentWrapper(this.content);
    }

    @Override
    public void setContent(ContentWrapper<?> contentWrapper) {
        assert String.class.isAssignableFrom(contentWrapper.getContentClass());

        this.content = (String) contentWrapper.getContent();
    }

    public static class StringContentWrapper implements ContentWrapper<String>{

        private String value;

        public StringContentWrapper(String value){
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
