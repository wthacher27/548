package com.myapp.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "APPUSER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "HEIGHT_IN")
    private Float heightIn;

    @Column(name = "WEIGHT_LBS")
    private Float weightLbs;

    @Column(name = "BODYFAT")
    private Float bodyFat;

    @Column(name = "EXPERIENCE")
    private Integer experience;

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
        return heightIn;
    }

    public void setHeightIn(Float heightIn) {
        this.heightIn = heightIn;
    }

    public Float getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(Float weightLbs) {
        this.weightLbs = weightLbs;
    }

    public Float getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(Float bodyFat) {
        this.bodyFat = bodyFat;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getAge() {
        return age;
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