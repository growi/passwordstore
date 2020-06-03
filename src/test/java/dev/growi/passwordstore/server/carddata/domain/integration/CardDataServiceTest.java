package dev.growi.passwordstore.server.carddata.domain.integration;

import dev.growi.passwordstore.server.carddata.domain.model.Card;
import dev.growi.passwordstore.server.carddata.domain.model.CardCollection;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.DatasourceException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.authentication.JpaUserPrincipal;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@EnableSpringConfigured
@EnableAspectJAutoProxy
@TestExecutionListeners(listeners = {DependencyInjectionTestExecutionListener.class})
public class CardDataServiceTest {

    @Autowired
    private UserDataProvider userDataProvider;

    private UserDetails adminPrincipal = null;
    private UserDetails userPrincipal = null;
    private User user = null;

    @BeforeAll
    @WithUserDetails("Admin")
    public void setup() throws UserNotFoundException, CryptographyException {
        adminPrincipal = new JpaUserPrincipal((JpaUser) userDataProvider.findByUserName("Admin"));

        user = User.create("User1", "password");

        UserDAO userDAO = userDataProvider.findByUserName("User1");
        userPrincipal = new JpaUserPrincipal((JpaUser) userDAO);
    }

    @Test
    @WithUserDetails("User1")
    public void createCardTest() throws UserNotFoundException, DatasourceException {

        Card card = Card.create("test");
        System.out.println(card);
    }

    @Test
    @WithUserDetails("User1")
    public void createCollectionTest() throws UserNotFoundException, DatasourceException {

        CardCollection collection = CardCollection.create();
        System.out.println(collection);
    }
}
