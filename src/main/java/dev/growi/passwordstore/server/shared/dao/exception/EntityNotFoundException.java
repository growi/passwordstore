package dev.growi.passwordstore.server.shared.dao.exception;

import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;

public class EntityNotFoundException extends DatasourceException {

    private String entityName = "";
    private String query = "";

    public EntityNotFoundException(String entityName, String query){
        this.entityName = entityName;
        this.query = query;
    }

    @Override
    public String getMessage(){
        return "Could not find entry of type " + this.entityName + " that fulfills query: " + query;
    }
}
