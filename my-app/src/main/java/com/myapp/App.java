package com.myapp;

import com.myapp.dao.*;
import com.myapp.model.*;
import java.util.Scanner;
import java.util.List;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserDAO userDAO = new UserDAO();
    private static final WorkoutDAO workoutDAO = new WorkoutDAO();
    private static final InjuryDAO injuryDAO = new InjuryDAO();

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("=== WORKOUT TRACKER CONSOLE ===");

        while (running) {
            System.out.println("\n1. View All Users");
            System.out.println("2. View Workouts for User");
            System.out.println("3. View Injuries for User");
            System.out.println("4. Exit");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    displayUsers();
                    break;
                case "2":
                    displayWorkouts();
                    break;
                case "3":
                    displayInjuries();
                    break;
                case "4":
                    running = false;
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void displayUsers() {
        System.out.println("\n--- Registered Users ---");
        // Assumes you added a readAll() or listUsers() to your UserDAO
        List<User> users = userDAO.listUsers(); 
        if (users.isEmpty()) {
            System.out.println("No users found in database.");
        } else {
            users.forEach(u -> System.out.println("ID: " + u.id + " | Name: " + u.name));
        }
    }

    private static void displayWorkouts() {
        System.out.print("Enter User ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        List<Workout> workouts = workoutDAO.getByUserId(userId);
        
        System.out.println("\n--- Workout History ---");
        if (workouts.isEmpty()) {
            System.out.println("No workouts found for this user.");
        } else {
            workouts.forEach(w -> System.out.println("[" + w.date + "] " + w.type + ": " + w.summary));
        }
    }

    private static void displayInjuries() {
        System.out.print("Enter User ID: ");
        int userId = Integer.parseInt(scanner.nextLine());
        List<Injury> injuries = injuryDAO.getByUserId(userId);

        System.out.println("\n--- Injury/Recovery Log ---");
        if (injuries.isEmpty()) {
            System.out.println("No injury records found.");
        } else {
            injuries.forEach(i -> System.out.println("Injury: " + i.injury + " | Status: " + i.recovery));
        }
    }
}