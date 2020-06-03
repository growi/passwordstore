package dev.growi.passwordstore.server.carddata.dao.impl.jpa.model;

import dev.growi.passwordstore.server.carddata.dao.model.ContentWrapper;
import javassist.bytecode.ByteArray;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@MappedSuperclass
public abstract class JpaByteContent extends JpaContent {

    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] content;

    public JpaByteContent(ByteArrayContentWrapper contentWrapper){
        this.setContent(contentWrapper);
    }

    @Override
    public ContentWrapper<?> getContent() {
        return new ByteArrayContentWrapper(this.content);
    }

    @Override
    public void setContent(ContentWrapper<?> contentWrapper) {
        assert ByteArrayInputStream.class.isAssignableFrom(contentWrapper.getContentClass());

        ByteArrayInputStream stream = ((ByteArrayContentWrapper)contentWrapper).getContent();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[4096];

        while ((read = stream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, read);
        }
        this.content = buffer.toByteArray();
    }

    public static class ByteArrayContentWrapper implements ContentWrapper<ByteArrayInputStream>{

        private ByteArrayInputStream stream;

        public ByteArrayContentWrapper(byte[] bytes){
            stream = new ByteArrayInputStream(bytes);
        }

        @Override
        public Class<ByteArrayInputStream> getContentClass() {
            return ByteArrayInputStream.class;
        }

        @Override
        public ByteArrayInputStream getContent() {
            return stream;
        }
    }
}
