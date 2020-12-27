package com.onnasoft.date.controllers;

import javax.servlet.http.HttpServletRequest;
import com.onnasoft.date.models.Profile;
import com.onnasoft.date.models.User;
import com.onnasoft.date.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;

    interface PATCHIndex {
    }

    class PATCHIndexError implements PATCHIndex {
        String message;

        PATCHIndexError(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static class PATCHIndexBody extends Profile {
    }

    private void PATCHIndexValidate(PATCHIndexBody data) {

    }

    @PatchMapping(value = "", consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE })
    ResponseEntity<PATCHIndex> PATCHIndex(HttpServletRequest request, @RequestBody PATCHIndexBody content) {
        try {
            PATCHIndexValidate(content);
            final var session = request.getSession();
            final var user = (User) session.getAttribute("user");
            final var userId = user.getId();
            var currentProfile = profileService.findProfileByUserId(userId);
            if (currentProfile == null) {
                currentProfile = new Profile();
                currentProfile.setUserId(userId);
            }
            currentProfile.setAthleteIfValue(content.getAthlete());
            currentProfile.setBirthDateIfValue(content.getBirthDate());
            currentProfile.setChildrenIfValue(content.getChildren());
            currentProfile.setCountryOfBirthIfValue(content.getCountryOfBirth());
            currentProfile.setCountryOfResidenceIfValue(content.getCountryOfResidence());
            currentProfile.setCultureIfValue(content.getCulture());
            currentProfile.setDescriptionIfValue(content.getDescription());
            currentProfile.setGoOutOrHomeIfValue(content.getGoOutOrHome());
            currentProfile.setMaximumAgeRange(content.getMaximumAgeRange());
            currentProfile.setMinimumAgeRange(content.getMinimumAgeRange());
            currentProfile.setPetsIfValue(content.getPets());
            currentProfile.setSmokerIfValue(content.getSmoker());
            currentProfile.setTitleIfValue(content.getTitle());
            currentProfile.setPhotoUrlIfValue(content.getPhotoUrl());
            profileService.save(currentProfile);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            final var response = new PATCHIndexError("¡No se han podido salvar los datos!.");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
