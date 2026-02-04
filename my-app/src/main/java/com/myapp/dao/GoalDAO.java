package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.myapp.database.DatabaseHelper;

public class GoalDAO {
    public void updateProgress(int id, float newProgress) {
        String sql = "UPDATE GOAL SET PROGRESS = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setFloat(1, newProgress);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
