package dev.growi.passwordstore.server.userdata.domain.model;

import java.io.Serializable;

public interface IdWrapper<V> extends Serializable {

    Class<V> getIdClass();

    V getValue();

    void setValue(V value);

}
