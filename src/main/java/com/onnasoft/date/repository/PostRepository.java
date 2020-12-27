package com.onnasoft.date.repository;

import com.onnasoft.date.models.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Long> {

    Iterable<Post> findPostByUserId(Long userId);
}
