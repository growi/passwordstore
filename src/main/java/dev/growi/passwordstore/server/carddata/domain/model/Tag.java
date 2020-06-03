package dev.growi.passwordstore.server.carddata.domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import dev.growi.passwordstore.server.carddata.dao.model.TagDAO;
import dev.growi.passwordstore.server.core.base.domain.Model;
import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Configurable;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = true, autowire = Autowire.BY_TYPE)
public class Tag extends Model<Tag, TagDAO> {

    private Long id;
    private String name;

    private Tag(){

    }

    public Tag(TagDAO tagDAO){

        this.id = tagDAO.getId();
        this.name = tagDAO.getName();
    }

    @Override
    protected void setProperties(TagDAO dao) {
        this.setId(dao.getId());
        this.setName(dao.getName());
    }

    @Override
    public void update(Tag template, boolean ignoreNull) {
        if(!ignoreNull || template.getName() != null) this.setName(template.getName());
    }

    @Override
    protected TagDAO getDao() throws EntityNotFoundException {
        return null;
    }

    @Override
    protected TagDAO updateDao(TagDAO dao) {
        return null;
    }

    @Override
    public Tag save() throws EntityNotFoundException {
        return null;
    }

    @Override
    public void delete() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
