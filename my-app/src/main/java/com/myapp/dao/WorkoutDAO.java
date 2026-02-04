package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.myapp.database.DatabaseHelper;
import com.myapp.model.Workout;

public class WorkoutDAO {
    // CREATE
    public void create(Workout w) {
        String sql = "INSERT INTO Workout(USERID, TYPE, DATE, SUMMARY, DURATION) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, w.userId);
            pstmt.setString(2, w.type);
            pstmt.setTimestamp(3, new java.sql.Timestamp(w.date.getTime()));
            pstmt.setString(4, w.summary);
            pstmt.setInt(5, w.duration);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // READ (By User)
    public List<Workout> getByUserId(int userId) {
        List<Workout> list = new ArrayList<>();
        String sql = "SELECT * FROM Workout WHERE USERID = ?";
        // ... (standard ResultSet logic)
        return list;
    }

    // UPDATE
    public void update(Workout w) {
        String sql = "UPDATE Workout SET SUMMARY = ?, DURATION = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, w.summary);
            pstmt.setInt(2, w.duration);
            pstmt.setInt(3, w.id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM Workout WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}