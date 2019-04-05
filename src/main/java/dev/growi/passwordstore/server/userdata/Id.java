package dev.growi.passwordstore.server.userdata;

public interface Id<V> {

    Class<V> getIdClass();

    V getValue();

    void setValue(V value);

}
