package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import myjdbc.DatabaseConnection;

public class Dentist {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String specialization;
    private String licenseNumber;
    private String availableDays;
    private String availableTime;

    // Constructors
    public Dentist() {}

    public Dentist(int id, String name, String email, String phone, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
    }

    public Dentist(int id, String name, String email, String phone, String specialization, 
                  String licenseNumber, String availableDays, String availableTime) {
        this(id, name, email, phone, specialization);
        this.licenseNumber = licenseNumber;
        this.availableDays = availableDays;
        this.availableTime = availableTime;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getAvailableDays() { return availableDays; }
    public void setAvailableDays(String availableDays) { this.availableDays = availableDays; }

    public String getAvailableTime() { return availableTime; }
    public void setAvailableTime(String availableTime) { this.availableTime = availableTime; }

    // Database Operations
    public static Dentist getById(int dentistId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Dentist dentist = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM dentists WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, dentistId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                dentist = new Dentist(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("specialization"),
                    rs.getString("license_number"),
                    rs.getString("available_days"),
                    rs.getString("available_time")
                );
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return dentist;
    }

    public static List<Dentist> getAllDentists() throws SQLException {
        List<Dentist> dentists = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM dentists";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Dentist dentist = new Dentist(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("specialization"),
                    rs.getString("license_number"),
                    rs.getString("available_days"),
                    rs.getString("available_time")
                );
                dentists.add(dentist);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return dentists;
    }

    public boolean save() throws SQLException {
        if (id == 0) {
            return insert();
        } else {
            return update();
        }
    }

    private boolean insert() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO dentists (name, email, phone, specialization, " +
                         "license_number, available_days, available_time) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, specialization);
            stmt.setString(5, licenseNumber);
            stmt.setString(6, availableDays);
            stmt.setString(7, availableTime);

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
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    private boolean update() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "UPDATE dentists SET name = ?, email = ?, phone = ?, " +
                         "specialization = ?, license_number = ?, available_days = ?, " +
                         "available_time = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, specialization);
            stmt.setString(5, licenseNumber);
            stmt.setString(6, availableDays);
            stmt.setString(7, availableTime);
            stmt.setInt(8, id);

            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    public boolean delete() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "DELETE FROM dentists WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // Business Methods
    private boolean someDatabaseMethod() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            // Database operations here
            return true;
        } finally {
            // Proper resource cleanup
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            DatabaseConnection.closeConnection(conn);
        }
    }

    // Fixed getTreatments() method
    public List<Treatment> getTreatments() throws SQLException {
        return Treatment.getByDentistId(id);
    }

    // Fixed toString() method
    @Override
    public String toString() {
        return name + " (" + specialization + ")";
    }
}