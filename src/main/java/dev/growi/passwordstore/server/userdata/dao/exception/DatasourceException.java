package dev.growi.passwordstore.server.userdata.dao.exception;

import java.io.IOException;

public class DatasourceExceotion extends IOException {

    public DatasourceExceotion(){

    }

    public DatasourceExceotion(Throwable t){
        super(t);
    }
}
