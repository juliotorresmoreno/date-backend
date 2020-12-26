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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

    public static class POSTSignInBody {
        String email;
        String password;

        public POSTSignInBody() {
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private void POSTSignInValidate(POSTSignInBody data) {

    }

    @PostMapping(value = "/sign-in", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<POSTSignInResponse> POSTSignIn(@RequestBody POSTSignInBody content) {
        try {
            POSTSignInValidate(content);
            final var user = userService.authenticate(content.getEmail(), content.getPassword());
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
            final var response = new POSTSignInResponseError("Ha ocurrido un error interno del servidor!");
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

    public static class POSTSignUpBody {
        String firstname;
        String lastname;
        String email;
        String phone;
        String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    private void POSTSignUpValidate(POSTSignUpBody data) {

    }

    @PostMapping(value = "/sign-up", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<POSTSignUpResponse> POSTSignUp(@RequestBody POSTSignUpBody content) {
        try {
            POSTSignUpValidate(content);
            final var user = new User(content.getFirstname(), content.getLastname(), content.getEmail(), content.getPhone(),
                    content.getPassword());
            userService.save(user);
            final var response = new POSTSignUpResponseOK();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserServiceException e) {
            final var response = new POSTSignUpResponseError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (DataIntegrityViolationException e) {
            final var response = new POSTSignUpResponseError("El correo electonico ya ha sido registrado!");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.warning(e.getMessage());
            final var response = new POSTSignUpResponseError("Ha ocurrido un error interno del servidor!");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
