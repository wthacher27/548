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

import com.myapp.model.Exercise;
import com.myapp.repository.ExerciseRepository;
import com.myapp.repository.WorkoutRepository;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4000"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @GetMapping
    public List<Exercise> getAllExercises(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return exerciseRepository.findAll();
        return workoutRepository.findByUser(userId.longValue()).stream()
            .flatMap(w -> exerciseRepository.findByWorkoutId(w.getId()).stream())
            .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercise> getExercise(@PathVariable Long id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return exerciseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        }
        return exerciseRepository.findById(id)
            .filter(e -> workoutRepository.findById(e.getWorkoutId())
                .map(w -> w.getUser().equals(userId.longValue()))
                .orElse(false))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/workout/{workoutId}")
    public ResponseEntity<List<Exercise>> getExercisesByWorkout(@PathVariable Long workoutId, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return workoutRepository.findById(workoutId)
                .map(w -> ResponseEntity.ok(exerciseRepository.findByWorkoutId(workoutId)))
                .orElse(ResponseEntity.notFound().build());
        }
        return workoutRepository.findById(workoutId)
            .filter(w -> w.getUser().equals(userId.longValue()))
            .map(w -> ResponseEntity.ok(exerciseRepository.findByWorkoutId(workoutId)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercise> updateExercise(@PathVariable Long id, @RequestBody Exercise updated) {
        return exerciseRepository.findById(id).map(exercise -> {
            exercise.setWorkoutId(updated.getWorkoutId());
            exercise.setName(updated.getName());
            exercise.setReps(updated.getReps());
            exercise.setWeight(updated.getWeight());
            exercise.setPr(updated.getPr());
            exercise.setMuscleId(updated.getMuscleId());
            return ResponseEntity.ok(exerciseRepository.save(exercise));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        if (!exerciseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        exerciseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
