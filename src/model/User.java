package model;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import myjdbc.DatabaseConnection;

public class User {
    private int id;
    private String username;
    private String email;
    private String password; // Store hashed password only
    private String role;
    private String status; // Added status field
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    // Constructors
    public User() {}

    public User(int id, String username, String email, String role, String status,
               Timestamp createdAt, Timestamp lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt != null ? createdAt.toLocalDateTime() : null;
        this.lastLogin = lastLogin != null ? lastLogin.toLocalDateTime() : null;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { 
        this.password = password; 
    }

    public void setPlainPassword(String plainPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        this.password = hashPassword(plainPassword);
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getStatus() { return status; } // Added status getter
    public void setStatus(String status) { this.status = status; } // Added status setter

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    // Password Hashing Utility
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String hashPassword(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);

        PBEKeySpec spec = new PBEKeySpec(
            password.toCharArray(), 
            salt, 
            ITERATIONS, 
            KEY_LENGTH
        );

        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(salt) + ":" + 
               Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String inputPassword, String storedHash) 
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedHash.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedPassword = Base64.getDecoder().decode(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(
            inputPassword.toCharArray(),
            salt,
            ITERATIONS,
            storedPassword.length * 8
        );

        SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        // Constant-time comparison to prevent timing attacks
        int diff = storedPassword.length ^ testHash.length;
        for (int i = 0; i < storedPassword.length && i < testHash.length; i++) {
            diff |= storedPassword[i] ^ testHash[i];
        }
        return diff == 0;
    }

    // Database Operations
    public static List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, email, role, status, created_at, last_login FROM users";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("last_login")
                ));
            }
        }
        return users;
    }

    public static List<User> getUsersByRole(String role) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, username, email, role, status, created_at, last_login FROM users WHERE role = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, role);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("last_login")
                    ));
                }
            }
        }
        return users;
    }

    public static User getById(int id) throws SQLException {
        String query = "SELECT id, username, email, role, status, created_at, last_login FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("last_login")
                    );
                }
            }
        }
        return null;
    }

    public static User getByUsername(String username) throws SQLException {
        String query = "SELECT id, username, email, password, role, status, created_at, last_login FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("last_login")
                    );
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            }
        }
        return null;
    }

    public boolean save() throws SQLException, NoSuchAlgorithmException, InvalidKeySpecException {
        if (id == 0) {
            return insert();
        } else {
            return update();
        }
    }

    private boolean insert() throws SQLException {
        String query = "INSERT INTO users (username, email, password, role, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, role);
            stmt.setString(5, status != null ? status : "Active");

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                }
            }
            return true;
        }
    }

    private boolean update() throws SQLException {
        String query = "UPDATE users SET username = ?, email = ?, role = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, role);
            stmt.setString(4, status);
            stmt.setInt(5, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updatePassword() throws SQLException {
        String query = "UPDATE users SET password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, password);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateLastLogin() throws SQLException {
        String query = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean delete() throws SQLException {
        String query = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, username=%s, role=%s, status=%s]", 
               id, username, role, status);
    }

    // Additional utility methods for the admin dashboard
    public static List<String> getAllRoles() {
        return List.of("Admin", "Dentist", "Receptionist", "Patient");
    }

    public static List<String> getAllStatuses() {
        return List.of("Active", "Inactive", "Suspended");
    }
}