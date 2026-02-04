package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.myapp.database.DatabaseHelper;
import com.myapp.model.MuscleGroup;

public class MuscleGroupDAO {

    // CREATE
    public void create(String name) {
        String sql = "INSERT INTO MuscleGroup(NAME) VALUES(?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // READ (Get all groups)
    public List<MuscleGroup> getAll() {
        List<MuscleGroup> groups = new ArrayList<>();
        String sql = "SELECT * FROM MuscleGroup";
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MuscleGroup mg = new MuscleGroup();
                mg.id = rs.getInt("ID");
                mg.name = rs.getString("NAME");
                groups.add(mg);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return groups;
    }

    // UPDATE
    public void update(int id, String newName) {
        String sql = "UPDATE MuscleGroup SET NAME = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM MuscleGroup WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}