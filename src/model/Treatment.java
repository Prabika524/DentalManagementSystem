package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import myjdbc.DatabaseConnection;

public class Treatment {
    private int id;
    private int patientId;
    private int dentistId;
    private String treatmentType;
    private String description;
    private double cost;
    private String date;
    private String status;
    private String notes;

    // Constructors
    public Treatment() {}

    public Treatment(int id, int patientId, int dentistId, String treatmentType, 
                    String description, double cost, String date, String status, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.treatmentType = treatmentType;
        this.description = description;
        this.cost = cost;
        this.date = date;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDentistId() { return dentistId; }
    public void setDentistId(int dentistId) { this.dentistId = dentistId; }

    public String getTreatmentType() { return treatmentType; }
    public void setTreatmentType(String treatmentType) { this.treatmentType = treatmentType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Database Methods
    public static List<Treatment> getByPatientId(int patientId) throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM treatments WHERE patient_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Treatment treatment = new Treatment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("dentist_id"),
                    rs.getString("treatment_type"),
                    rs.getString("description"),
                    rs.getDouble("cost"),
                    rs.getString("date"),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                treatments.add(treatment);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        return treatments;
    }

    public static List<Treatment> getByDentistId(int dentistId) throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM treatments WHERE dentist_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, dentistId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Treatment treatment = new Treatment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getInt("dentist_id"),
                    rs.getString("treatment_type"),
                    rs.getString("description"),
                    rs.getDouble("cost"),
                    rs.getString("date"),
                    rs.getString("status"),
                    rs.getString("notes")
                );
                treatments.add(treatment);
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        return treatments;
    }

    @Override
    public String toString() {
        return "Treatment [id=" + id + ", patientId=" + patientId + ", dentistId=" + dentistId 
               + ", type=" + treatmentType + ", cost=" + cost + ", date=" + date + ", status=" + status + "]";
    }
}