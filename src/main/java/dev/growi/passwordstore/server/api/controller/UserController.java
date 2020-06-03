package dev.growi.passwordstore.server.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.growi.passwordstore.server.api.exception.InputParsingException;
import dev.growi.passwordstore.server.api.exception.InputValidationException;
import dev.growi.passwordstore.server.api.message.ChangePasswordMessage;
import dev.growi.passwordstore.server.api.message.CreateUserMessage;
import dev.growi.passwordstore.server.api.service.EncoderService;
import dev.growi.passwordstore.server.api.service.PasswordStrengthValidator;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import dev.growi.passwordstore.server.userdata.dao.exception.UserNotFoundException;
import dev.growi.passwordstore.server.userdata.domain.exception.InvalidPasswordException;
import dev.growi.passwordstore.server.userdata.domain.model.User;
import dev.growi.passwordstore.server.userdata.domain.service.CollectionLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
public class UserController {

    @Autowired
    private EncoderService encoderService;

    @Autowired
    private CollectionLoaderService collectionLoaderService;

    @Autowired
    private PasswordStrengthValidator passwordStrengthValidator;

    private ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET, path = "/api/user", produces = "application/json")
    public List<User> getAllUsers(){
        return collectionLoaderService.getAllUsers();
    }


    @RequestMapping(method = RequestMethod.POST, path = "/api/user", consumes = "application/json", produces = "application/json")
    public User createUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody String json) throws UserNotFoundException, CryptographyException {

        CreateUserMessage message = null;

        try {
            message = mapper.readValue(json, CreateUserMessage.class);
        } catch (IOException e) {
            throw new InputParsingException(e);
        }

        User user = message.getUser();
        String password = message.getPassword();

        if(passwordStrengthValidator.isValidPassword(password)) {
            return User.create(user, password);
        }
        throw new InputValidationException("Password to weak!");
    }

    @RequestMapping(method = RequestMethod.GET,  path = "/api/user/{userid}", produces = "application/json")
    public User getUser(@PathVariable("userid") Long userId) throws UserNotFoundException, CryptographyException {

        User user = User.load(userId);

        return user;
    }

    @RequestMapping(method = RequestMethod.PUT,  path = "/api/user/{userid}", consumes = "application/json", produces = "application/json")
    public User updateUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("userid") Long userId, @RequestBody String json) throws UserNotFoundException, CryptographyException {

        User user = User.load(userId);
        User template;

        try {
            template = mapper.readValue(json, User.class);
        } catch (IOException e) {
            throw new InputParsingException(e);
        }

        user.update(template, false);
        user.save();

        return user;
    }

    @RequestMapping(method = RequestMethod.PATCH,  path = "/api/user/{userid}", consumes = "application/json", produces = "application/json")
    public User patchUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("userid") Long userId, @RequestBody String json) throws UserNotFoundException, CryptographyException {

        User user = User.load(userId);
        User template;

        try {
            template = mapper.readValue(json, User.class);
        } catch (IOException e) {
            throw new InputParsingException(e);
        }

        user.update(template, true);
        user.save();

        return user;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/api/user/{userid}", consumes = "application/json", produces = "application/json")
    public boolean deleteUser(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("userid") Long userId) throws UserNotFoundException, CryptographyException {

        User.load(userId).delete();
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/api/user/{userid}/password", consumes = "application/json", produces = "application/json")
    public User changePassword(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("userid") Long userId, @RequestBody String json) throws UserNotFoundException, CryptographyException, InvalidPasswordException {

        User user = User.load(userId);
        ChangePasswordMessage message = null;
        try {
            message = mapper.readValue(json, ChangePasswordMessage.class);
        } catch (IOException e) {
            throw new InputParsingException(e);
        }
        if(passwordStrengthValidator.isValidPassword(message.getNewPassword())){
            user.changePassword(message.getOldPassword(), message.getNewPassword(), userDetails);
        }else {
            throw new InputValidationException("Password to weak!");
        }
        return user;
    }
}
