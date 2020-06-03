package dev.growi.passwordstore.server.userdate.domain.integration;

import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupMemberNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.authentication.JpaUserPrincipal;
import dev.growi.passwordstore.server.userdata.dao.impl.jpa.model.JpaUser;
import dev.growi.passwordstore.server.userdata.dao.model.UserDAO;
import dev.growi.passwordstore.server.userdata.dao.provider.GroupDataProvider;
import dev.growi.passwordstore.server.userdata.dao.provider.UserDataProvider;
import dev.growi.passwordstore.server.userdata.domain.model.Group;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import org.junit.Assert;
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
public class HierarchyCryptographyTest {

    @Autowired
    UserDataProvider userDataProvider;

    @Autowired
    GroupDataProvider groupDataProvider;

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
    public void inheritedAccessTest() throws UserNotFoundException, CryptographyException, GroupNotFoundException, GroupMemberNotFoundException {

        Assert.assertNotNull(userPrincipal);

        User newUser = User.create("User2", "password");

        Group group1 = Group.create("group1");
        Group group2 = Group.create("group2");
        Group group3 = Group.create("group3");

        group1.addMember(group2, 0, userPrincipal, "password");
        group2.addMember(group3, 0, userPrincipal, "password");

        group1.removeMember(user);
        group2.removeMember(user);

        group1.addMember(newUser, 0, userPrincipal, "password");
    }
}
