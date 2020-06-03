package dev.growi.passwordstore.server.carddata.dao.model;

public interface ContentWrapper<T> {

    Class<T> getContentClass();
    T getContent();

}
