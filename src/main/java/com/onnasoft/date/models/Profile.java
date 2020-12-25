package com.onnasoft.date.models;

import java.sql.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = Profile.TABLE_NAME)
public class Profile {
    public static final String TABLE_NAME = "profile";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;

    private String countryOfBirth;
    private Date birthDate;
    private String countryOfResidence;
    private String title;
    private String description;
    private String pets;
    private String children;
    private String smoker;
    private String athlete;
    private String goOutOrHome;
    private int minimumAgeRange;
    private int maximumAgeRange;
    private String culture;

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public void setCountryOfBirthIfValue(String countryOfBirth) {
        if (countryOfBirth != null && !countryOfBirth.equals(""))
            this.setCountryOfBirth(countryOfBirth);
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setBirthDateIfValue(Date birthDate) {
        if (birthDate != null)
            setBirthDate(birthDate);
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public void setCountryOfResidenceIfValue(String countryOfResidence) {
        if (countryOfResidence != null && !countryOfResidence.equals(""))
            setCountryOfResidence(countryOfResidence);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitleIfValue(String title) {
        if (title != null && !title.equals(""))
            setTitle(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionIfValue(String description) {
        if (description != null && !description.equals(""))
            setDescription(description);
    }

    public String getPets() {
        return pets;
    }

    public void setPets(String pets) {
        this.pets = pets;
    }

    public void setPetsIfValue(String pets) {
        if (pets != null && !pets.equals(""))
            this.setPets(pets);
    }

    public String getChildren() {
        return children;
    }

    public void setChildren(String children) {
        this.children = children;
    }

    public void setChildrenIfValue(String children) {
        if (children != null && !children.equals(""))
            setChildren(children);
    }

    public String getSmoker() {
        return smoker;
    }

    public void setSmoker(String smoker) {
        this.smoker = smoker;
    }

    public void setSmokerIfValue(String smoker) {
        if (smoker != null && !smoker.equals(""))
            setSmoker(smoker);
    }

    public String getAthlete() {
        return athlete;
    }

    public void setAthlete(String athlete) {
        this.athlete = athlete;
    }

    public void setAthleteIfValue(String athlete) {
        if (athlete != null && !athlete.equals(""))
            setAthlete(athlete);
    }

    public String getGoOutOrHome() {
        return goOutOrHome;
    }

    public void setGoOutOrHome(String goOutOrHome) {
        this.goOutOrHome = goOutOrHome;
    }

    public void setGoOutOrHomeIfValue(String goOutOrHome) {
        if (goOutOrHome != null && !goOutOrHome.equals(""))
            setGoOutOrHome(goOutOrHome);
    }

    public int getMinimumAgeRange() {
        return minimumAgeRange;
    }

    public void setMinimumAgeRange(int minimumAgeRange) {
        if (minimumAgeRange > 18)
            this.minimumAgeRange = minimumAgeRange;
    }

    public int getMaximumAgeRange() {
        return maximumAgeRange;
    }

    public void setMaximumAgeRange(int maximumAgeRange) {
        if (maximumAgeRange > 100)
            this.maximumAgeRange = maximumAgeRange;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        this.culture = culture;
    }

    public void setCultureIfValue(String culture) {
        if (culture != null && !culture.equals(""))
            setCulture(culture);
    }
}
