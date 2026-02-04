package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.myapp.database.DatabaseHelper;
import com.myapp.model.Exercise;

public class ExerciseDAO {
    public void create(Exercise e) {
        String sql = "INSERT INTO Exercise(WORKOUTID, NAME, REPS, WEIGHT, PR, MUSCLEID) VALUES(?,?,?,?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, e.workoutId);
            pstmt.setString(2, e.name);
            pstmt.setInt(3, e.reps);
            pstmt.setInt(4, e.weight);
            pstmt.setBoolean(5, e.pr);
            pstmt.setInt(6, e.muscleId);
            pstmt.executeUpdate();
        } catch (SQLException err) { err.printStackTrace(); }
    }

    public void updatePR(int id, boolean isPR) {
        String sql = "UPDATE Exercise SET PR = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, isPR);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException err) { err.printStackTrace(); }
    }
    
    // DELETE
    public void deleteByWorkout(int workoutId) {
        String sql = "DELETE FROM Exercise WHERE WORKOUTID = ?";
        // ... execution
    }
}