package dev.growi.passwordstore.server.core.base.dao;

import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;

import java.util.List;

public interface DataProvider<I, D extends Dao> {

    public D create() throws DatasourceException;

    public D findById(I id) throws EntityNotFoundException;

    public List<? extends D> findAll();

    public D save(D dao) throws DatasourceException;

    public void deleteById(I id);

}
