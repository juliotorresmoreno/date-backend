package com.onnasoft.date.services;

import com.onnasoft.date.models.Post;
import com.onnasoft.date.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    PostRepository repository;

    public Iterable<Post> findPostByUserId(Long id) {
        return repository.findPostByUserId(id);
    }

    public Post save(Post post) {
        return repository.save(post);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
