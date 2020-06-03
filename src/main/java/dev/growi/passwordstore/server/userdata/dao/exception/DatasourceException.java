package dev.growi.passwordstore.server.userdata.dao.exception;

import java.io.IOException;

public class DatasourceException extends IOException {

    public DatasourceException(){

    }

    public DatasourceException(Throwable t){
        super(t);
    }
}
