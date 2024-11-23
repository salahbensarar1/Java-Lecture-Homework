package org.example.javalecturehomework.controller;

import org.example.javalecturehomework.model.Match;
import org.example.javalecturehomework.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchesController {
    public List<Match> getAllMatches() {
        List<Match> matches = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM matches";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                matches.add(new Match(
                        rs.getInt("id"),
                        rs.getDate("mdate"),
                        rs.getTime("startsat"),
                        rs.getDouble("ticketprice"),
                        rs.getString("mtype")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }

    public boolean addMatch(Match match) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO matches (id, mdate, startsat, ticketprice, mtype) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, match.getId());
            stmt.setDate(2, match.getMdate());
            stmt.setTime(3, match.getStartsAt());
            stmt.setDouble(4, match.getTicketPrice());
            stmt.setString(5, match.getMtype());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Add updateMatch and deleteMatch methods here
    public boolean updateMatch(int id, String date, String startTime, double ticketPrice, String matchType) {
        String query = "UPDATE matches SET mdate = ?, startsat = ?, ticketprice = ?, mtype = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, date);       // Match date (String in YYYY-MM-DD format)
            stmt.setString(2, startTime); // Start time (String in HH:MM:SS format)
            stmt.setDouble(3, ticketPrice); // Ticket price
            stmt.setString(4, matchType);   // Match type (e.g., "championship" or "cup")
            stmt.setInt(5, id);             // Match ID to update

            return stmt.executeUpdate() > 0; // Returns true if the update is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returns false in case of an error
        }
    }

    public boolean deleteMatch(int id) {
        String query = "DELETE FROM matches WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id); // Match ID to delete

            return stmt.executeUpdate() > 0; // Returns true if the delete is successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returns false in case of an error
        }
    }

}
