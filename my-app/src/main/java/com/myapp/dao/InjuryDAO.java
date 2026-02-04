package com.myapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.myapp.database.DatabaseHelper;
import com.myapp.model.Injury;

public class InjuryDAO {

    // CREATE
    public void create(Injury injury) {
        String sql = "INSERT INTO Injury(USERID, INJURY, RECOVERY) VALUES(?,?,?)";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, injury.userId);
            pstmt.setString(2, injury.injury);
            pstmt.setString(3, injury.recovery);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // READ (Get all injuries for a specific user)
    public List<Injury> getByUserId(int userId) {
        List<Injury> injuries = new ArrayList<>();
        String sql = "SELECT * FROM Injury WHERE USERID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Injury i = new Injury();
                i.id = rs.getInt("ID");
                i.userId = rs.getInt("USERID");
                i.injury = rs.getString("INJURY");
                i.recovery = rs.getString("RECOVERY");
                injuries.add(i);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return injuries;
    }

    // UPDATE (Update recovery status)
    public void updateRecovery(int id, String newStatus) {
        String sql = "UPDATE Injury SET RECOVERY = ? WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // DELETE
    public void delete(int id) {
        String sql = "DELETE FROM Injury WHERE ID = ?";
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}
