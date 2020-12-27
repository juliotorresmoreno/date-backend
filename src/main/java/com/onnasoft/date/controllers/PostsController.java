package com.onnasoft.date.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.onnasoft.date.models.Post;
import com.onnasoft.date.models.User;
import com.onnasoft.date.services.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostService postService;

    interface GETIndex {
    }

    class GETIndexOK extends ArrayList<Post> implements GETIndex {
        /**
         *
         */
        private static final long serialVersionUID = -3132910626614384936L;

        GETIndexOK(Iterable<Post> posts) {
            posts.forEach((post) -> {
                this.add(post);
            });
        }
    }

    @GetMapping("")
    public ResponseEntity<GETIndex> GETIndex(HttpServletRequest request) {
        var user = (User) request.getAttribute("user");
        var posts = postService.findPostByUserId(user.getId());
        return new ResponseEntity<>(new GETIndexOK(posts), HttpStatus.OK);
    }

    interface GETRecommends {
    }

    class GETRecommendsOK implements GETRecommends {
        GETRecommendsOK() {

        }
    }

    @GetMapping("/recommends")
    public ResponseEntity<GETRecommends> GETRecommends() {
        return new ResponseEntity<>(new GETRecommendsOK(), HttpStatus.OK);
    }

    interface POSTIndex {
    }

    class POSTIndexOK implements POSTIndex {
        POSTIndexOK() {

        }
    }

    public static class POSTIndexBody extends Post {

    }

    private void POSTIndexValidate(POSTIndexBody data) {

    }

    @PostMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<POSTIndex> POSTIndex(@RequestBody POSTIndexBody content) {
        POSTIndexValidate(content);
        postService.save(content);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    interface PATCHIndex {
    }

    class PATCHIndexOK implements PATCHIndex {
        PATCHIndexOK() {

        }
    }

    public static class PATHIndexBody extends Post {
    }

    @PatchMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<PATCHIndex> PATCHIndex(HttpServletRequest request, @RequestBody PATHIndexBody content) {
        try {
            var user = (User) request.getSession().getAttribute("user");
            var post = new Post();
            post.setUserId(user.getId());
            post.setTitleIfValue(content.getTitle());
            post.setDescriptionIfValue(content.getDescription());
            postService.save(post);
            return new ResponseEntity<>(new PATCHIndexOK(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new PATCHIndexOK(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    interface DELETEIndex {
    }

    class DELETEIndexOK implements DELETEIndex {
        DELETEIndexOK() {
        }
    }

    class DELETEIndexError implements DELETEIndex {
        String message;

        DELETEIndexError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<DELETEIndex> DELETEIndex(@PathVariable("id") String id) {
        try {
            var longId = Long.parseLong(id);
            postService.delete(longId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            final var response = new DELETEIndexError(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
