package dev.growi.passwordstore.server.carddata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlEntry;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.JpaAccessControlList;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository.JpaAccessControlEntryRepository;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository.JpaAccessControlListRepository;
import dev.growi.passwordstore.server.carddata.dao.model.AccessControlEntryDAO;
import dev.growi.passwordstore.server.carddata.dao.model.AccessControlListDAO;
import dev.growi.passwordstore.server.carddata.dao.model.RestrictedDAO;
import dev.growi.passwordstore.server.carddata.dao.provider.AccessControlDataProvider;
import dev.growi.passwordstore.server.shared.dao.exception.EntityNotFoundException;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.dataprovider.JpaMonitoreddDataProvider;
import dev.growi.passwordstore.server.shared.service.CryptographyService;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.PrincipalDAO;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;

@Service
public class JpaAccessControlDataProvider  extends JpaMonitoreddDataProvider<Long, AccessControlListDAO> implements AccessControlDataProvider {

    @Autowired
    private CryptographyService cryptographyService;

    @Autowired
    private JpaAccessControlListRepository jpaAccessControlListRepository;

    @Autowired
    private JpaAccessControlEntryRepository jpaAccessControlEntryRepository;

    @Override
    public List<? extends AccessControlListDAO> findAll() {
        return null;
    }

    @Override
    public AccessControlListDAO findByObject(RestrictedDAO restrictedDAO) {
        return null;
    }

    @Override
    public AccessControlListDAO findById(Long id) throws EntityNotFoundException {

        Optional<JpaAccessControlList> opt = jpaAccessControlListRepository.findById(id);
        if(opt.isPresent()){
            return opt.get();
        }
        throw new EntityNotFoundException("AccessControlListDAO", "id="+id);
    }

    @Override
    public AccessControlListDAO create() throws UserNotFoundException, DatasourceException {

        JpaAccessControlList acl = new JpaAccessControlList();
        this.setCreatedInfo(acl);
        this.setLastUpdatedInfo(acl);
        acl = jpaAccessControlListRepository.save(acl);

        return acl;
    }

    @Override
    public AccessControlListDAO create(PrincipalDAO principal, UserDAO user) throws UserNotFoundException, DatasourceException {

        AccessControlListDAO acl = create();

        AccessControlEntryDAO ace = createAccessControlEntry(principal, 0);
        ace.setAcl((JpaAccessControlList) acl);
        acl.getEntries().add(ace);

        return acl;
    }

    @Override
    public AccessControlListDAO save(AccessControlListDAO dao){
        return jpaAccessControlListRepository.save((JpaAccessControlList) dao);
    }

    @Override
    public void deleteById(Long id){
        jpaAccessControlListRepository.deleteById(id);
    }

    @Override
    public AccessControlEntryDAO createAccessControlEntry(PrincipalDAO principal, int permissions) throws UserNotFoundException, DatasourceException {

        byte[] encrypted;
        try {
            SecretKey key = cryptographyService.generateAESKey();
            encrypted = cryptographyService.encryptRSA(
                    cryptographyService.createRSAPublicKey(principal.getPublicKey()),
                    key.getEncoded()
            );
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchProviderException | NoSuchPaddingException
                | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            throw new DatasourceException(new CryptographyException(e));
        }

        JpaAccessControlEntry ace = new JpaAccessControlEntry();
        ace.setPrincipal(principal);
        ace.setPermission(0);
        ace.setSecret(encrypted);
        this.setCreatedInfo(ace);
        this.setLastUpdatedInfo(ace);
        ace = jpaAccessControlEntryRepository.save(ace);

        return ace;
    }
}
