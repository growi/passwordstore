package dev.growi.passwordstore.server.api.controller;

import dev.growi.passwordstore.server.api.service.EncoderService;
import dev.growi.passwordstore.server.userdata.dao.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.model.User;
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
public class UserController {

    @Autowired
    EncoderService encoderService;

    @Autowired
    UserDataService userDataService;

    @RequestMapping("/api/user")
    public List<User> getAllUsers(){
        return userDataService.getAll();
    }

    @RequestMapping("/api/user/create/{username}/{password}")
    public User createUser(@PathVariable("username") String userName,
                           @PathVariable("password") String password,
                           @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException, CryptographyException {
        return userDataService.createUser(userName, password, userDetails);
    }

    @RequestMapping(path = "/api/user/{username}", produces = "application/json")
    public User getUser(@PathVariable("username") String userName) throws UserNotFoundException {

        return userDataService.getByUserName(userName);
    }

    @RequestMapping(path = "/api/user/{username}/publickey", produces = "text/plain")
    public String getPublicKey(@PathVariable("username") String userName) throws IOException {

        return encoderService.armorKey( userDataService.getByUserName(userName).getPublicKey() );
    }

    @RequestMapping(path = "/api/user/{username}/privatekey", produces = "text/plain")
    public String getPrivateKey(@PathVariable("username") String userName) throws IOException {

        return encoderService.armorKey( userDataService.getByUserName(userName).getPrivateKey() );
    }
}
