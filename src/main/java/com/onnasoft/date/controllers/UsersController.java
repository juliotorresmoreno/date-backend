package com.onnasoft.date.controllers;

import java.util.logging.Logger;

import com.onnasoft.date.models.User;
import com.onnasoft.date.models.User.Profile;
import com.onnasoft.date.services.UserService;
import com.onnasoft.date.services.UserService.UserServiceException;
import com.onnasoft.date.utils.Secure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {
    Logger logger;

    @Autowired
    UserService userService;

    @Autowired
    private Environment env;

    UsersController() {
        this.logger = Logger.getGlobal();
    }

    interface POSTSignInResponse {
    }

    class POSTSignInResponseOK implements POSTSignInResponse {
        private String token;
        private Profile profile;

        POSTSignInResponseOK(String token, Profile profile) {
            this.profile = profile;
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public Profile getProfile() {
            return profile;
        }
    }

    class POSTSignInResponseError implements POSTSignInResponse {
        private String message;

        POSTSignInResponseError(String error) {
            this.message = error;
        }

        public String getMessage() {
            return message;
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<POSTSignInResponse> POSTSignIn(@RequestParam(value = "email") String email,
            @RequestParam(value = "password") String password) {
        try {
            final var user = userService.authenticate(email, password);
            final var profile = user.getProfile();
            final var secret = env.getProperty("secret");
            final var token = Secure.getJWTToken(secret, profile.getEmail());
            final var response = new POSTSignInResponseOK(token, profile);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserServiceException e) {
            final var response = new POSTSignInResponseError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            var response = new POSTSignInResponseError("Ha ocurrido un error interno del servidor!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    interface POSTSignUpResponse {
    }

    class POSTSignUpResponseOK implements POSTSignUpResponse {
        private boolean success;

        POSTSignUpResponseOK() {
            this.success = true;
        }

        public boolean isSuccess() {
            return success;
        }
    }

    class POSTSignUpResponseError implements POSTSignUpResponse {
        private boolean success;
        private String message;

        POSTSignUpResponseError(String error) {
            this.success = false;
            this.message = error;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }

    @RequestMapping(value = "/sign-up", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<POSTSignUpResponse> POSTSignUp(@RequestParam(value = "firstname") String firstname,
            @RequestParam(value = "lastname") String lastname, @RequestParam(value = "email") String email,
            @RequestParam(value = "phone") String phone, @RequestParam(value = "password") String password) {
        try {
            var user = new User(firstname, lastname, email, phone, password);
            userService.save(user);
            var response = new POSTSignUpResponseOK();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserServiceException e) {
            var response = new POSTSignUpResponseError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            var response = new POSTSignUpResponseError("El correo electonico ya ha sido registrado!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            var response = new POSTSignUpResponseError("Ha ocurrido un error interno del servidor!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
