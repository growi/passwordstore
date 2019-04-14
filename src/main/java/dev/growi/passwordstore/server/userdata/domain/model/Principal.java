package dev.growi.passwordstore.server.userdata.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.domain.service.CryptographyService;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Configurable(preConstruction = true, dependencyCheck = true, autowire = Autowire.BY_TYPE)
public abstract class Principal {

    @Autowired
    CryptographyService cryptographyService;

    protected IdWrapper<?> id = null;
    private PublicKey publicKey;
    private String createdByUser;
    private Instant createdStamp;
    private String lastUpdatedByUser;
    private Instant lastUpdatedStamp;

    Principal(PrincipalDAO principalDAO) {

        this.createdByUser = principalDAO.getCreatedByUser() != null ? principalDAO.getCreatedByUser().getUserName() : "";
        this.createdStamp = principalDAO.getCreatedStamp();
        this.lastUpdatedByUser = principalDAO.getLastUpdatedByUser() != null ? principalDAO.getLastUpdatedByUser().getUserName() : "";
        this.lastUpdatedStamp = principalDAO.getLastUpdatedStamp();
        try {
            this.publicKey = cryptographyService.createRSAPublicKey(principalDAO.getPublicKey());
        } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException(new CryptographyException(e));
        }
    }

    public IdWrapper<?> getId() {
        return this.id;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public String getCreatedByUser() {
        return this.createdByUser;
    }

    public Instant getCreatedStamp() {
        return this.createdStamp;
    }

    public String getLastUpdatedByUser() {
        return this.lastUpdatedByUser;
    }

    public Instant getLastUpdateStamp() {
        return this.lastUpdatedStamp;
    }
}
