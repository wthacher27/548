package com.myapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.myapp.model.Injury;

@Repository
public interface InjuryRepository extends JpaRepository<Injury, Long> {
    List<Injury> findByUser(Long userId);
}
