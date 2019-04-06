package dev.growi.passwordstore.server.userdata.dao.exception;

public class EntityNotFoundException extends DatasourceExceotion {

    private String entityName = "";
    private String query = "";

    EntityNotFoundException(String entityName, String query){
        this.entityName = entityName;
        this.query = query;
    }

    @Override
    public String getMessage(){
        return "Could not find entry of type " + this.entityName + " that fulfills query: " + query;
    }
}
