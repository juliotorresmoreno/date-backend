package com.onnasoft.date.controllers;

import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/people")
public class PeopleController {
    interface GETIndex {
    }

    class GETIndexOK extends ArrayList<Object> implements GETIndex {
        private static final long serialVersionUID = -4870482941048093581L;
    }

    @GetMapping("")
    public ResponseEntity<GETIndex> GETIndex() {
        return new ResponseEntity<>(new GETIndexOK(), HttpStatus.OK);
    }    
}
