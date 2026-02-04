package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.myapp.database.DatabaseHelper;
import com.myapp.model.User;

public class UserDAO {

    // CREATE
    public void create(User user) {
        String sql = "INSERT INTO USER(NAME, HEIGHT_IN, WEIGHT_LBS, AGE) VALUES(?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.name);
            pstmt.setFloat(2, user.heightIn);
            pstmt.setFloat(3, user.weightLbs);
            pstmt.setInt(4, user.age);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // READ
    public User read(int id) {
        String sql = "SELECT * FROM USER WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.id = rs.getInt("ID");
                u.name = rs.getString("NAME");
                return u;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // UPDATE
    public void update(User user) {
        String sql = "UPDATE USER SET WEIGHT_LBS = ?, BODYFAT = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, user.weightLbs);
            pstmt.setFloat(2, user.bodyFat);
            pstmt.setInt(3, user.id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM USER WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<User> listUsers() {
    List<User> users = new ArrayList<>();
    // Using * avoids the "Unknown column" error during the initial query
    String sql = "SELECT * FROM USER";

    try (Connection conn = DatabaseHelper.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            User user = new User();
            
            // We use column indexes (1, 2, 3...) to avoid the naming conflict
            user.id = rs.getInt(1);           // ID
            user.name = rs.getString(2);      // NAME
            user.heightIn = rs.getFloat(3);   // This pulls the 3rd column, whatever it's named
            user.weightLbs = rs.getFloat(4);  // This pulls the 4th column
            user.bodyFat = rs.getFloat(5);
            user.experience = rs.getInt(6);
            user.age = rs.getInt(7);
            user.birthday = rs.getTimestamp(8);
            
            users.add(user);
        }
    } catch (SQLException e) {
        System.err.println("Database Error: " + e.getMessage());
    }
    return users;
}
}