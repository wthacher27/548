package com.myapp.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "APPUSER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Positive(message = "Height must be greater than 0")
    @Column(name = "HEIGHT_IN")
    private Float heightIn;

    @Positive(message = "Weight must be greater than 0")
    @Column(name = "WEIGHT_LBS")
    private Float weightLbs;

    @Column(name = "BODYFAT")
    private Float bodyFat;

    @Column(name = "EXPERIENCE")
    private Integer experience;

    @Positive(message = "Age must be greater than 0")
    @Column(name = "AGE")
    private Integer age;

    @Column(name = "BIRTHDAY")
    private LocalDate birthday;

    @Column(name = "password", nullable = false)
    private String password;

    public User() {
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return name;
    }

    public void setUsername(String username) {
        this.name = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getHeightIn() {
        return heightIn != null ? heightIn : 1f;
    }

    public void setHeightIn(Float heightIn) {
        this.heightIn = heightIn;
    }

    public Float getWeightLbs() {
        return weightLbs != null ? weightLbs : 1f;
    }

    public void setWeightLbs(Float weightLbs) {
        this.weightLbs = weightLbs;
    }

    public Float getBodyFat() {
        return bodyFat != null ? bodyFat : 1f;
    }

    public void setBodyFat(Float bodyFat) {
        this.bodyFat = bodyFat;
    }

    public int getExperience() {
        return experience != null ? experience : 1;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getAge() {
        return age != null ? age : 0;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}