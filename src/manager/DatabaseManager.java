package manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:game.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT NOT NULL, " +
                    "highest_score INTEGER DEFAULT 0, " +
                    "highest_level INTEGER DEFAULT 1, " +
                    "coins INTEGER DEFAULT 0, " +
                    "unlocked_planes TEXT DEFAULT 'Default', " +
                    "equipped_plane TEXT DEFAULT 'Default'" +
                    ");";
            stmt.execute(createUsersTable);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void saveGameScore(String username, int score, int level) {
        String selectSql = "SELECT highest_score FROM users WHERE username = ?";
        String updateSql = "UPDATE users SET highest_score = ?, highest_level = ?, coins = coins + ? WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            selectStmt.setString(1, username);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                int currentHighest = rs.getInt("highest_score");
                int newHighest = Math.max(currentHighest, score);

                updateStmt.setInt(1, newHighest);
                updateStmt.setInt(2, level);
                updateStmt.setInt(3, score);
                updateStmt.setString(4, username);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> getTopScores() {
        ArrayList<String[]> scores = new ArrayList<>();
        String sql = "SELECT username, highest_score, highest_level FROM users ORDER BY highest_score DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                scores.add(new String[]{
                        rs.getString("username"),
                        String.valueOf(rs.getInt("highest_score")),
                        String.valueOf(rs.getInt("highest_level"))
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }

    public static int getCoins(String username) {
        String sql = "SELECT coins FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean buyPlane(String username, String planeName, int cost) {
        int currentCoins = getCoins(username);
        if (currentCoins >= cost) {
            String sql = "UPDATE users SET coins = coins - ?, unlocked_planes = unlocked_planes || ? WHERE username = ?";
            try (Connection conn = DriverManager.getConnection(URL);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, cost);
                pstmt.setString(2, "," + planeName);
                pstmt.setString(3, username);
                pstmt.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static List<String> getUnlockedPlanes(String username) {
        String sql = "SELECT unlocked_planes FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String planesStr = rs.getString("unlocked_planes");
                return Arrays.asList(planesStr.split(","));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static String getEquippedPlane(String username) {
        String sql = "SELECT equipped_plane FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("equipped_plane");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Default";
    }

    public static void setEquippedPlane(String username, String planeName) {
        String sql = "UPDATE users SET equipped_plane = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, planeName);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}