package com.onnasoft.date.controllers;

import java.util.ArrayList;

import com.onnasoft.date.models.User;
import com.onnasoft.date.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
public class PeopleController {

    @Autowired
    private UserService userService;

    interface GETIndex {
    }

    class GETIndexOK extends ArrayList<User.Profile> implements GETIndex {
        private static final long serialVersionUID = -4870482941048093581L;

        GETIndexOK() {
        }

        GETIndexOK(ArrayList<User.Profile> people) {
            people.forEach(profile -> {
                this.add(profile);
            });
        }
    }

    @GetMapping("")
    public ResponseEntity<GETIndex> GETIndex() {
        var people = userService.getRecommends();
        return new ResponseEntity<>(new GETIndexOK(people), HttpStatus.OK);
    }
}
