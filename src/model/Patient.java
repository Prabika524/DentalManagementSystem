package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import myjdbc.DatabaseConnection;

public class Patient {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String dob;
    private String gender;
    private String bloodGroup;
    private String medicalHistory;

    // Constructors
    public Patient() {}

    public Patient(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Patient(int id, String name, String email, String phone, String address, 
                  String dob, String gender, String bloodGroup, String medicalHistory) {
        this(id, name, email, phone);
        this.address = address;
        this.dob = dob;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.medicalHistory = medicalHistory;
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

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    // Database Operations
    public static Patient getById(int patientId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Patient patient = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM patients WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                patient = new Patient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("dob"),
                    rs.getString("gender"),
                    rs.getString("blood_group"),
                    rs.getString("medical_history")
                );
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return patient;
    }

    public static List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM patients";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("dob"),
                    rs.getString("gender"),
                    rs.getString("blood_group"),
                    rs.getString("medical_history")
                );
                patients.add(patient);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return patients;
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
            String query = "INSERT INTO patients (name, email, phone, address, dob, " +
                         "gender, blood_group, medical_history) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setString(5, dob);
            stmt.setString(6, gender);
            stmt.setString(7, bloodGroup);
            stmt.setString(8, medicalHistory);

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
            String query = "UPDATE patients SET name = ?, email = ?, phone = ?, " +
                         "address = ?, dob = ?, gender = ?, blood_group = ?, " +
                         "medical_history = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setString(5, dob);
            stmt.setString(6, gender);
            stmt.setString(7, bloodGroup);
            stmt.setString(8, medicalHistory);
            stmt.setInt(9, id);

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
            String query = "DELETE FROM patients WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    // Business Methods
    public List<Appointment> getAppointments() throws SQLException {
        return Appointment.getByPatientId(id);
    }

    public List<Treatment> getTreatments() throws SQLException {
        return Treatment.getByPatientId(id);
    }

    public List<Prescription> getPrescriptions() throws SQLException {
        return Prescription.getByPatientId(id);
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}