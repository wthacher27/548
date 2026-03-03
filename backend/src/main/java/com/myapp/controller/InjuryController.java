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

import com.myapp.model.Injury;
import com.myapp.repository.InjuryRepository;

import jakarta.servlet.http.HttpSession;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4000"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/injuries")
public class InjuryController {
    @Autowired
    private InjuryRepository injuryRepository;

    @GetMapping
    public List<Injury> getAllInjuries(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return injuryRepository.findAll();
        return injuryRepository.findByUser(userId.longValue());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Injury> getInjury(@PathVariable Long id, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) {
            return injuryRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        }
        return injuryRepository.findById(id)
            .filter(i -> i.getUser().equals(userId.longValue()))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<Injury> getInjuriesByUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (Boolean.TRUE.equals(session.getAttribute("isAdmin"))) return injuryRepository.findAll();
        return injuryRepository.findByUser(userId.longValue());
    }

    @PostMapping
    public Injury createInjury(@RequestBody Injury injury) {
        return injuryRepository.save(injury);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Injury> updateInjury(@PathVariable Long id, @RequestBody Injury updated) {
        return injuryRepository.findById(id).map(injury -> {
            injury.setUser(updated.getUser());
            injury.setInjury(updated.getInjury());
            injury.setRecovery(updated.getRecovery());
            return ResponseEntity.ok(injuryRepository.save(injury));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInjury(@PathVariable Long id) {
        if (!injuryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        injuryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
