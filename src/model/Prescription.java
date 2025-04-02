package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import myjdbc.DatabaseConnection;

public class Prescription {
    private int id;
    private int patientId;
    private int dentistId;
    private String prescriptionDate;
    private String medication;
    private String dosage;
    private String duration;
    private String instructions;

    // Constructors
    public Prescription() {}

    public Prescription(int id, int patientId, int dentistId, String prescriptionDate, 
                       String medication, String dosage, String duration, String instructions) {
        this.id = id;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.prescriptionDate = prescriptionDate;
        this.medication = medication;
        this.dosage = dosage;
        this.duration = duration;
        this.instructions = instructions;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDentistId() { return dentistId; }
    public void setDentistId(int dentistId) { this.dentistId = dentistId; }

    public String getPrescriptionDate() { return prescriptionDate; }
    public void setPrescriptionDate(String prescriptionDate) { this.prescriptionDate = prescriptionDate; }

    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    // Database Methods

    /**
     * Retrieves a prescription by its ID from the database
     * @param id The ID of the prescription to retrieve
     * @return Prescription object if found, null otherwise
     * @throws SQLException If there's a database error
     */
    public static Prescription getById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Prescription prescription = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM prescriptions WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                prescription = new Prescription(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("dentist_id"),
                    rs.getString("prescription_date"),
                    rs.getString("medication"),
                    rs.getString("dosage"),
                    rs.getString("duration"),
                    rs.getString("instructions")
                );
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return prescription;
    }

    /**
     * Retrieves all prescriptions for a specific patient
     * @param patientId The ID of the patient
     * @return List of prescriptions for the patient
     * @throws SQLException If there's a database error
     */
    public static List<Prescription> getByPatientId(int patientId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM prescriptions WHERE patient_id = ? ORDER BY prescription_date DESC";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Prescription prescription = new Prescription(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("dentist_id"),
                    rs.getString("prescription_date"),
                    rs.getString("medication"),
                    rs.getString("dosage"),
                    rs.getString("duration"),
                    rs.getString("instructions")
                );
                prescriptions.add(prescription);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return prescriptions;
    }

    /**
     * Retrieves all prescriptions written by a specific dentist
     * @param dentistId The ID of the dentist
     * @return List of prescriptions by the dentist
     * @throws SQLException If there's a database error
     */
    public static List<Prescription> getByDentistId(int dentistId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM prescriptions WHERE dentist_id = ? ORDER BY prescription_date DESC";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, dentistId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Prescription prescription = new Prescription(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("dentist_id"),
                    rs.getString("prescription_date"),
                    rs.getString("medication"),
                    rs.getString("dosage"),
                    rs.getString("duration"),
                    rs.getString("instructions")
                );
                prescriptions.add(prescription);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return prescriptions;
    }

    /**
     * Saves the prescription to the database (inserts if new, updates if existing)
     * @return true if successful, false otherwise
     * @throws SQLException If there's a database error
     */
    public boolean save() throws SQLException {
        if (id == 0) {
            return insert();
        } else {
            return update();
        }
    }

    /**
     * Inserts a new prescription into the database
     * @return true if successful, false otherwise
     * @throws SQLException If there's a database error
     */
    private boolean insert() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO prescriptions (patient_id, dentist_id, prescription_date, " +
                         "medication, dosage, duration, instructions) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, prescriptionDate);
            stmt.setString(4, medication);
            stmt.setString(5, dosage);
            stmt.setString(6, duration);
            stmt.setString(7, instructions);

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

    /**
     * Updates an existing prescription in the database
     * @return true if successful, false otherwise
     * @throws SQLException If there's a database error
     */
    private boolean update() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "UPDATE prescriptions SET patient_id = ?, dentist_id = ?, " +
                         "prescription_date = ?, medication = ?, dosage = ?, " +
                         "duration = ?, instructions = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, prescriptionDate);
            stmt.setString(4, medication);
            stmt.setString(5, dosage);
            stmt.setString(6, duration);
            stmt.setString(7, instructions);
            stmt.setInt(8, id);

            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Deletes the prescription from the database
     * @return true if successful, false otherwise
     * @throws SQLException If there's a database error
     */
    public boolean delete() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "DELETE FROM prescriptions WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public String toString() {
        return "Prescription [id=" + id + ", patientId=" + patientId + ", dentistId=" + dentistId 
               + ", date=" + prescriptionDate + ", medication=" + medication + ", dosage=" + dosage 
               + ", duration=" + duration + "]";
    }
}