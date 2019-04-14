package dev.growi.passwordstore.server.api.controller;

import dev.growi.passwordstore.server.api.service.EncoderService;
import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.GroupNotFoundException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.Group;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import dev.growi.passwordstore.server.userdata.domain.service.GroupDataService;
import dev.growi.passwordstore.server.userdata.domain.service.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

@RestController
public class GroupController {

    @Autowired
    UserDataService userDataService;

    @Autowired
    GroupDataService groupDataService;

    @Autowired
    EncoderService encoderService;

    @RequestMapping("/api/group")
    public List<Group> getAllGroups(){
        return groupDataService.getAll();
    }

    @RequestMapping("/api/group/create/{groupname}")
    public Group createGroup(@PathVariable("groupname") String groupName,
                             @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, CryptographyException {

        return groupDataService.createGroup(groupName, userDetails);

    }

    @RequestMapping("/api/group/{groupname}/adduser/{username}/{password}")
    public Group addUserMember(@PathVariable("groupname") String groupName,
                           @PathVariable("username") String userName,
                           @PathVariable("password") String password,
                           @AuthenticationPrincipal UserDetails userDetails) throws GroupNotFoundException, UserNotFoundException {

        Group group = groupDataService.getByName(groupName);
        User user = userDataService.getByUserName(userName);

        // group.addMember(user);
        return group;
    }


    @RequestMapping("/api/group/{groupname}/addgroup/{membername}/{password}")
    public Group addGroupMember(@PathVariable("groupname") String groupName,
                         @PathVariable("membername") String memberName,
                         @PathVariable("password") String password,
                         @AuthenticationPrincipal UserDetails userDetails) throws GroupNotFoundException, UserNotFoundException {

        Group group = groupDataService.getByName(groupName);
//        User user = userDataService.getByUserName(userName);

        // group.addMember(user);
        return group;
    }

    @RequestMapping(path = "/api/group/{groupname}", produces = "application/json")
    public Group getGroup(@PathVariable("groupname") String groupName) throws GroupNotFoundException {

        return groupDataService.getByName(groupName);
    }

    @RequestMapping(path = "/api/group/{groupname}/publickey", produces = "text/plain")
    public String getPublicKey(@PathVariable("groupname") String groupName) throws IOException,
            InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException {

        return encoderService.armorKey( groupDataService.getByName(groupName).getPublicKey() );
    }


}
