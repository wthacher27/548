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

import com.myapp.model.Goal;
import com.myapp.repository.GoalRepository;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4000"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/goals")
public class GoalController {
    @Autowired
    private GoalRepository goalRepository;

    @GetMapping
    public List<Goal> getAllGoals(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return goalRepository.findAll();
        return goalRepository.findByUser(userId.longValue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Goal> getGoal(@PathVariable Long id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return goalRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        }
        return goalRepository.findById(id)
            .filter(g -> g.getUser().equals(userId.longValue()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Goal> getGoalsByUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return goalRepository.findAll();
        return goalRepository.findByUser(userId.longValue());
    }

    @PostMapping
    public Goal createGoal(@RequestBody Goal goal) {
        return goalRepository.save(goal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Goal> updateGoal(@PathVariable Long id, @RequestBody Goal updated) {
        return goalRepository.findById(id).map(goal -> {
            goal.setUser(updated.getUser());
            goal.setGoal(updated.getGoal());
            goal.setProgress(updated.getProgress());
            return ResponseEntity.ok(goalRepository.save(goal));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGoal(@PathVariable Long id) {
        if (!goalRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        goalRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
