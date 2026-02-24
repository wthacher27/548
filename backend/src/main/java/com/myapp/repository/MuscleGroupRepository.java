package com.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.model.MuscleGroup;

@Repository

public interface MuscleGroupRepository extends JpaRepository<MuscleGroup, Integer> {
    // You can add custom methods here if needed
    // List<User> findByName(String name);
}