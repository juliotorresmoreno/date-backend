package com.onnasoft.date.services;

import java.security.NoSuchAlgorithmException;
import com.onnasoft.date.models.User;
import com.onnasoft.date.repository.UserRepository;
import com.onnasoft.date.utils.Secure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    public class UserServiceException extends Exception {

        private static final long serialVersionUID = 5381463180788311974L;

        UserServiceException(String message) {
            super(message);
        }
    }

    public class FirstNameIsRequiredException extends UserServiceException {

        private static final long serialVersionUID = -1494462003906884727L;

        FirstNameIsRequiredException() {
            super("firstname es requerido!");
        }
    }

    public class PasswordIsRequiredException extends UserServiceException {

        private static final long serialVersionUID = -1035021900613497820L;

        PasswordIsRequiredException() {
            super("password es requerido!");
        }
    }

    public class PasswordLengthException extends UserServiceException {

        private static final long serialVersionUID = 7055960499038419177L;

        PasswordLengthException() {
            super("password es requerido!");
        }
    }

    void validate(User entity)
            throws FirstNameIsRequiredException, PasswordIsRequiredException, PasswordLengthException {
        var pass = entity.getPassword();
        if (entity.getId().equals((long) 0)) {
            if (pass.equals("")) {
                throw new PasswordIsRequiredException();
            }
            if (pass.length() < 8) {
                throw new PasswordLengthException();
            }
        }
        if (entity.getFirstname().equals("")) {
            throw new FirstNameIsRequiredException();
        }
    }

    public User save(User entity) throws FirstNameIsRequiredException, PasswordIsRequiredException,
            PasswordLengthException, NoSuchAlgorithmException {
        validate(entity);
        if (entity.getId().equals((long) 0)) {
            entity.setPassword(Secure.getStringFromSHA256(entity.getPassword()));
        }
        return repository.save(entity);
    }

    public class UserNotFound extends UserServiceException {

        private static final long serialVersionUID = -1559608864532290097L;

        public UserNotFound() {
            super("El usuario no existe");
        }
    }

    public class PasswordNotValid extends UserServiceException {

        private static final long serialVersionUID = 4719705332322383090L;

        PasswordNotValid() {
            super("La contraseña no es valida!");
        }
    }

    public User authenticate(String email, String password)
            throws UserNotFound, NoSuchAlgorithmException, PasswordNotValid {
        var users = repository.findUserByEmail(email);
        if (users.size() != 1) {
            throw new UserNotFound();
        }
        var user = users.iterator().next();
        var hashed = Secure.getStringFromSHA256(password);

        if (!user.getPassword().equals(hashed)) {
            throw new PasswordNotValid();
        }

        return user;
    }

    public User findUserByEmail(String email) throws UserNotFound {
        var users = repository.findUserByEmail(email);
        if (users.size() != 1) {
            throw new UserNotFound();
        }
        return users.iterator().next();
    }
}
