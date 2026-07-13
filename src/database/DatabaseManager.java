package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:game.db";

    public static void initialize() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "username TEXT PRIMARY KEY, " +
                    "password TEXT, " +
                    "high_score INTEGER DEFAULT 0)");

            stmt.execute("CREATE TABLE IF NOT EXISTS games (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +
                    "score INTEGER, " +
                    "last_level INTEGER, " +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    "bg_music INT DEFAULT 1, " +
                    "shot_sound INT DEFAULT 1, " +
                    "crash_sound INT DEFAULT 1, " +
                    "game_over_sound INT DEFAULT 1)");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean registerUser(String username, String password) {
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
             java.sql.PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            return pstmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void saveGameScore(String username, int score, int level) {
        String insertGameSql = "INSERT INTO games(username, score, last_level) VALUES(?, ?, ?)";
        String updateHighScoreSql = "UPDATE users SET high_score = ? WHERE username = ? AND ? > high_score";

        try (Connection conn = DriverManager.getConnection(URL)) {
            try (java.sql.PreparedStatement pstmt1 = conn.prepareStatement(insertGameSql)) {
                pstmt1.setString(1, username);
                pstmt1.setInt(2, score);
                pstmt1.setInt(3, level);
                pstmt1.executeUpdate();
            }

            try (java.sql.PreparedStatement pstmt2 = conn.prepareStatement(updateHighScoreSql)) {
                pstmt2.setInt(1, score);
                pstmt2.setString(2, username);
                pstmt2.setInt(3, score);
                pstmt2.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static java.util.ArrayList<String[]> getTopScores() {
        java.util.ArrayList<String[]> list = new java.util.ArrayList<>();
        String sql = "SELECT username, high_score FROM users ORDER BY high_score DESC LIMIT 10";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             java.sql.ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("username"), String.valueOf(rs.getInt("high_score"))});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}