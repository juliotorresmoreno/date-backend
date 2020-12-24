package com.onnasoft.date.services;

import com.onnasoft.date.models.City;
import com.onnasoft.date.repository.CityRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {
    @Autowired
    private CityRepository cityRepository;

    public Iterable<City> findAll() {
        return cityRepository.findAll();
    }
}
