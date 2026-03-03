package com.myapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.myapp.model.Workout;
import com.myapp.repository.WorkoutRepository;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4000"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {
    @Autowired
    private WorkoutRepository workoutRepository;

    @GetMapping
    public List<Workout> getAllWorkouts(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return workoutRepository.findAll();
        return workoutRepository.findByUser(userId.longValue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Workout> getWorkout(@PathVariable Long id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return workoutRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        }
        return workoutRepository.findById(id)
            .filter(w -> w.getUser().equals(userId.longValue()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Workout> getWorkoutsByUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return workoutRepository.findAll();
        return workoutRepository.findByUser(userId.longValue());
    }

    @PostMapping
    public Workout createWorkout(@RequestBody Workout workout) {
        return workoutRepository.save(workout);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(@PathVariable Long id, @RequestBody Workout updated) {
        return workoutRepository.findById(id).map(workout -> {
            workout.setUser(updated.getUser());
            workout.setType(updated.getType());
            workout.setDate(updated.getDate());
            workout.setSummary(updated.getSummary());
            workout.setDuration(updated.getDuration());
            return ResponseEntity.ok(workoutRepository.save(workout));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(@PathVariable Long id) {
        if (!workoutRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        workoutRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
