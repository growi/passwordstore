package dev.growi.passwordstore.server.core.base.domain;

import dev.growi.passwordstore.server.core.base.dao.Dao;
import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;

public abstract class Model<M extends Model,D extends Dao> {

    protected abstract void setProperties(D dao);

    public abstract void update(M template, boolean ignoreNull);

    protected abstract D getDao() throws EntityNotFoundException;

    protected abstract D updateDao(D dao);

    public abstract M save() throws EntityNotFoundException;

    public abstract void delete();

}
