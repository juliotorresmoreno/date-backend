package com.onnasoft.date.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = User.TABLE_NAME)
public class User {
    public static final String TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;

    public User() {
        this.id = Long.valueOf(0);
        this.firstname = "";
        this.lastname = "";
        this.email = "";
        this.phone = "";
        this.password = "";
    }

    public User(Long id, String firstname, String lastname, String email, String phone, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public User(String firstname, String lastname, String email, String phone, String password) {
        this.id = Long.valueOf(0);
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public class Profile {
        private Long id;

        private String firstname;
        private String lastname;
        private String email;
        private String phone;

        Profile(User user) {
            this.id = user.id;
            this.firstname = user.firstname;
            this.lastname = user.lastname;
            this.email = user.email;
            this.phone = user.phone;
        }

        public Long getId() {
            return id;
        }

        public String getFirstname() {
            return firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }
    }

    public Profile getProfile() {
        return new Profile(this);
    }
}
