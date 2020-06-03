package dev.growi.passwordstore.server.carddata.dao.impl.jpa.dataprovider;

import dev.growi.passwordstore.server.carddata.dao.exception.CardNotFoundException;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.model.*;
import dev.growi.passwordstore.server.carddata.dao.impl.jpa.repository.*;
import dev.growi.passwordstore.server.carddata.dao.model.*;
import dev.growi.passwordstore.server.carddata.dao.provider.AccessControlDataProvider;
import dev.growi.passwordstore.server.carddata.dao.provider.CardDataProvider;
import dev.growi.passwordstore.server.core.authentication.AuthenticationFacade;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.dataprovider.JpaMonitoreddDataProvider;
import dev.growi.passwordstore.server.shared.dao.impl.jpa.model.JpaMonitored;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class JpaCardDataProvider extends JpaMonitoreddDataProvider<Long, CardDAO> implements CardDataProvider {


    @Autowired
    private AuthenticationFacade authenticationFacade;

    @Autowired
    private UserDataProvider userDataProvider;

    @Autowired
    private JpaCardRepository jpaCardRepository;

    @Autowired
    private JpaFileContentRepository jpaFileContentRepository;

    @Autowired
    private JpaTextContentRepository jpaTextContentRepository;

    @Autowired
    private JpaStringContentRepository jpaStringContentRepository;

    @Autowired
    private AccessControlDataProvider accessControlDataProvider;

    @Override
    public List<? extends CardDAO> findAll() {
        return jpaCardRepository.findAll();
    }

    @Override
    public CardDAO findById(Long id) throws CardNotFoundException {

        Optional<JpaCard> opt = jpaCardRepository.findById(id);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new CardNotFoundException("id=" + id);
    }

    @Transactional
    @Override
    public CardDAO create(String title) throws UserNotFoundException, DatasourceException {

        CardDAO card = create();

        card.setTitle(title);
        return  jpaCardRepository.save((JpaCard)card);

    }

    @Transactional
    @Override
    public CardDAO create() throws UserNotFoundException, DatasourceException {

        UserDAO user = userDataProvider.findByUserName(authenticationFacade.getAuthentication().getName());

        JpaCard card = new JpaCard();
        this.setCreatedInfo(card);
        this.setLastUpdatedInfo(card);
        card.setAcl(accessControlDataProvider.create(user, user));
        card.getAcl().setObject(card);

        return card;
    }

    @Override
    public CardContentDAO createCardContent(CardDAO card, CardContentDAO.ContentType contentType, Object value) {

        JpaContent content;
        ContentWrapper<?> contentWrapper;

        switch(contentType){
            case FILE:
                content = createFileContent(card, new JpaByteContent.ByteArrayContentWrapper((byte[])value));
                break;
            case TEXT:
                content = createTextContent(card, new JpaTextContent.TextContentWrapper((String)value));
                break;
            case STRING:
                content = createStringContent(card, new JpaStringContent.StringContentWrapper((String)value));
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + contentType);
        }
        return content;
    }

    private JpaFileContent createFileContent(CardDAO card, JpaByteContent.ByteArrayContentWrapper contentWrapper){
        JpaFileContent content;
        content = new JpaFileContent(contentWrapper);
        content.setCard((JpaCard) card);

        return jpaFileContentRepository.save(content);
    }

    private JpaTextContent createTextContent(CardDAO card, JpaTextContent.TextContentWrapper contentWrapper){
        JpaTextContent content;
        content = new JpaTextContent(contentWrapper);
        content.setCard((JpaCard) card);

        return jpaTextContentRepository.save(content);
    }

    private JpaStringContent createStringContent(CardDAO card, JpaStringContent.StringContentWrapper contentWrapper){
        JpaStringContent content;
        content = new JpaStringContent(contentWrapper);
        content.setCard((JpaCard) card);

        return jpaStringContentRepository.save(content);
    }

    @Override
    public CardDAO save(CardDAO dao) throws UserNotFoundException {

        setLastUpdatedInfo((JpaMonitored)dao);

        return jpaCardRepository.save((JpaCard) dao);
    }

    @Override
    public void deleteById(Long id) {
        jpaCardRepository.deleteById(id);
    }
}
