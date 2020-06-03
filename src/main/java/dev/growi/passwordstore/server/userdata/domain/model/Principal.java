package dev.growi.passwordstore.server.userdata.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.growi.passwordstore.server.core.base.dao.Dao;
import dev.growi.passwordstore.server.core.base.mapping.annotation.MappedValue;
import dev.growi.passwordstore.server.shared.domain.Monitored;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.shared.service.CryptographyService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = false, autowire = Autowire.BY_TYPE)
public abstract class Principal<M extends Principal, D extends PrincipalDAO> extends Monitored<M, D> {

    @Autowired
    protected CryptographyService cryptographyService;

    protected Long id = null;
    private PublicKey publicKey;

    protected Principal(){

    }

    Principal(D principalDAO) {
        super();
        setProperties(principalDAO);
    }

    @Override
    protected void setProperties(D principalDAO){
        super.setProperties(principalDAO);
        try {
            this.publicKey = cryptographyService.createRSAPublicKey(principalDAO.getPublicKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(new CryptographyException(e));
        }
    }

    @Override
    public void update(M template, boolean ignoreNull){
        if(!ignoreNull || template.getPublicKey() != null)  this.publicKey = template.getPublicKey();
    }

    protected  D updateDao(D dao){
        dao.setPublicKey(this.publicKey.getEncoded());

        return dao;
    }

    public Long getId() {
        return this.id;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }
}
