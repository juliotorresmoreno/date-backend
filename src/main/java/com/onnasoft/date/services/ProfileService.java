package com.onnasoft.date.services;

import com.onnasoft.date.models.Profile;
import com.onnasoft.date.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public Profile findProfileByUserId(Long userId) {
        return profileRepository.findProfileByUserId(userId);
    }

    public void save(Profile entity) {
        profileRepository.save(entity);
    }
}
