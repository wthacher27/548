// package com.myapp;

// import java.util.List;
// import java.util.Scanner;

// import com.myapp.dao.InjuryDAO;
// import com.myapp.dao.UserDAO;
// import com.myapp.dao.WorkoutDAO;
// import com.myapp.model.Injury;
// import com.myapp.model.User;
// import com.myapp.model.Workout;
// import com.mysql.cj.jdbc.AbandonedConnectionCleanupThread;
// public class AppOLD {
//     private static final Scanner scanner = new Scanner(System.in);
//     private static final UserDAO userDAO = new UserDAO();
//     private static final WorkoutDAO workoutDAO = new WorkoutDAO();
//     private static final InjuryDAO injuryDAO = new InjuryDAO();

//     public static void main(String[] args) {
//         boolean running = true;
//         System.out.println("=== WORKOUT TRACKER CONSOLE ===");

//         while (running) {
//             System.out.println("\n1. View All Users");
//             System.out.println("2. User login");

//             System.out.println("3. Exit");
//             System.out.print("Select an option: ");

//             String choice = scanner.nextLine();

//             switch (choice) {
//                 case "1" -> displayUsers();
//                 case "2" -> displayUser();
//                 case "3" -> {
//                     running = false;
//                     System.out.println("Exiting... Goodbye!");
//                     AbandonedConnectionCleanupThread.checkedShutdown();
//                 }
//                 default -> System.out.println("Invalid choice. Try again.");
//             }
//         }
//     }

//     private static void displayUsers() {
//         System.out.println("\n--- Registered Users ---");
//         List<User> users = userDAO.listUsers();
//         if (users.isEmpty()) {
//             System.out.println("No users found in database.");
//         } else {
//             users.forEach(u -> System.out.println("ID: " + u.id + " | Name: " + u.name));
//         }
//     }

//     private static void displayWorkouts(int id) {
//         List<Workout> workouts = workoutDAO.getByUserId(id);

//         System.out.println("\n--- Workout History ---");
//         if (workouts.isEmpty()) {
//             System.out.println("No workouts found for this user.");
//         } else {
//             workouts.forEach(w -> System.out.println("[" + w.date + "] " + w.type + ": " + w.summary));
//         }
//     }

//     private static void displayInjuries(int id) {
        
//         List<Injury> injuries = injuryDAO.getByUserId(id);

//         System.out.println("\n--- Injury/Recovery Log ---");
//         if (injuries.isEmpty()) {
//             System.out.println("No injury records found.");
//         } else {
//             injuries.forEach(i -> System.out.println("Injury: " + i.injury + " | Status: " + i.recovery));
//         }
//     }

//     private static void displayUser() {
//         System.out.print("Enter User ID: ");
//         int userId = Integer.parseInt(scanner.nextLine());
//         User user = userDAO.getUserbyID(userId);
//         if (user == null) {
//             System.out.println("No users found in database.");
//         } else {
//             if (user.name == null) {
//                 System.out.println("No users found in database.");
//             } else {
//                 boolean running = true;
//                 while (running) {
//                     System.out.println("ID: " + user.id + " | Name: " + user.name);
//                     System.out.println("Age: " + user.age + " | experience: " + user.experience);
//                     System.out.println("Height in inches: " + user.heightIn + " | Weight in lbs: " + user.weightLbs);
                    
                    
//                     System.out.println("DO you want to view/add/edit/delete data?");
//                     System.out.println("1. View Workouts for User");
//                     System.out.println("2. View Injuries for User");
//                     System.out.println("3. add data");
//                     System.out.println("4. edit data");
//                     System.out.println("5. delete data");
//                     System.out.println("6. logout");
//                     String choice = scanner.nextLine();

//                     switch (choice) {
//                         case "1" -> displayWorkouts(userId);
//                         case "2" -> displayInjuries(userId);
//                         case "3" -> adddatatoUser(userId);
//                         case "4" -> editUser(userId);
//                         case "5" -> delUser(userId);
//                         case "6" -> {
//                             running = false;
//                             System.out.println("Logging out!");
//                         }
//                         default -> System.out.println("Invalid choice. Try again.");
//                     }
//                 }
//             }
//         }
//     }

//     private static Object delUser(int userId) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'delUser'");
//     }

//     private static Object adddatatoUser(int userId) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'adddatatoUser'");
//     }

//     private static void editUser(int userId) {
//         throw new UnsupportedOperationException("Not supported yet.");
//     }
// }