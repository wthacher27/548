package com.myapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Exercise") 
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    
    @Column(name = "WORKOUTID", nullable = false)
    private Long workoutId;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "REPS")
    private Integer reps;

    @Column(name = "WEIGHT")
    private Float weight;

    @Column(name = "PR")
    private Boolean pr;

   
    @Column(name = "MUSCLEID", nullable = false)
    private Long muscleId;

    public Exercise() {
    }

    // Getters and Setters

    public Long getWorkoutId() {
        return workoutId;
    }

    public void setWorkoutId(Long workoutId) {
        this.workoutId = workoutId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Boolean getPr() {
        return pr;
    }

    public void setPr(Boolean pr) {
        this.pr = pr;
    }

    public Long getMuscleId() {
        return muscleId;
    }

    public void setMuscleId(Long muscleId) {
        this.muscleId = muscleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}