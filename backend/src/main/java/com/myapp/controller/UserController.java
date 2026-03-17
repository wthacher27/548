package com.myapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.myapp.model.User;
import com.myapp.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Value("${app.admin.name}")
    private String adminName;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/isAdmin/{name}")
    public Boolean isAdmin(@PathVariable String name) {
        return name.equals(adminName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable int id) {
        return userRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> credentials, HttpSession session) {
        String name = credentials.get("name");
        String password = credentials.get("password");
        return userRepository.findByNameAndPassword(name, password)
            .map(user -> {
                session.setAttribute("userId", user.getId());
                session.setAttribute("isAdmin", adminName.equals(user.getName()));
                return ResponseEntity.ok(user);
            })
            .orElse(ResponseEntity.status(401).build());
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, HttpSession session) {
        try {
            User saved = userRepository.save(user);
            session.setAttribute("userId", saved.getId());
            session.setAttribute("isAdmin", adminName.equals(saved.getName()));
            return ResponseEntity.ok(saved);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body("Username already taken.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable int id, @Valid @RequestBody User updated) {
        return userRepository.findById(id).map(user -> {
            user.setName(updated.getName());
            user.setHeightIn(updated.getHeightIn());
            user.setWeightLbs(updated.getWeightLbs());
            user.setBodyFat(updated.getBodyFat());
            user.setExperience(updated.getExperience());
            user.setAge(updated.getAge());
            user.setBirthday(updated.getBirthday());
            user.setPassword(updated.getPassword());
            return ResponseEntity.ok(userRepository.save(user));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
