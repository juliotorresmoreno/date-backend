package com.onnasoft.date.repository;

import java.util.List;

import com.onnasoft.date.models.User;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findUserByEmail(String email);
}
