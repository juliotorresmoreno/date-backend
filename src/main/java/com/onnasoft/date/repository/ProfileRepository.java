package com.onnasoft.date.repository;

import com.onnasoft.date.models.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {

    Profile findProfileByUserId(Long userId);
}
