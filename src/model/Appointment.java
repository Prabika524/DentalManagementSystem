package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import myjdbc.DatabaseConnection;

public class Appointment {
    private int id;
    private int patientId;
    private String patientName;
    private int dentistId;
    private String appointmentDate;
    private String appointmentTime;
    private String status; // Can be: Scheduled, Completed, Cancelled, No-Show
    private String reason;
    private String notes;

    // Default constructor
    public Appointment() {}

    // Full parameterized constructor
    public Appointment(int id, int patientId, String patientName, int dentistId, 
                      String appointmentDate, String appointmentTime, 
                      String status, String reason, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.patientName = patientName;
        this.dentistId = dentistId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.reason = reason;
        this.notes = notes;
    }

    // Simplified constructor for display purposes
    public Appointment(int id, String patientName, String appointmentTime, String reason) {
        this.id = id;
        this.patientName = patientName;
        this.appointmentTime = appointmentTime;
        this.reason = reason;
    }

    /* Getters and Setters */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public int getDentistId() { return dentistId; }
    public void setDentistId(int dentistId) { this.dentistId = dentistId; }

    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String appointmentTime) { this.appointmentTime = appointmentTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    /* Database Methods */

    public static Appointment getById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Appointment appointment = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT a.id, a.patient_id, p.name as patient_name, a.dentist_id, " +
                          "a.appointment_date, a.appointment_time, a.status, a.reason, a.notes " +
                          "FROM appointments a JOIN patients p ON a.patient_id = p.id WHERE a.id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                appointment = new Appointment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("dentist_id"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("reason"),
                    rs.getString("notes")
                );
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return appointment;
    }

    public static List<Appointment> getByPatientId(int patientId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT a.id, a.patient_id, p.name as patient_name, a.dentist_id, " +
                          "a.appointment_date, a.appointment_time, a.status, a.reason, a.notes " +
                          "FROM appointments a JOIN patients p ON a.patient_id = p.id " +
                          "WHERE a.patient_id = ? ORDER BY appointment_date, appointment_time";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("dentist_id"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("reason"),
                    rs.getString("notes")
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return appointments;
    }

    public static List<Appointment> getByDentistId(int dentistId) throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT a.id, a.patient_id, p.name as patient_name, a.dentist_id, " +
                          "a.appointment_date, a.appointment_time, a.status, a.reason, a.notes " +
                          "FROM appointments a JOIN patients p ON a.patient_id = p.id " +
                          "WHERE a.dentist_id = ? ORDER BY appointment_date, appointment_time";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, dentistId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                appointments.add(new Appointment(
                    rs.getInt("id"),
                    rs.getInt("patient_id"),
                    rs.getString("patient_name"),
                    rs.getInt("dentist_id"),
                    rs.getString("appointment_date"),
                    rs.getString("appointment_time"),
                    rs.getString("status"),
                    rs.getString("reason"),
                    rs.getString("notes")
                ));
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return appointments;
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
            String query = "INSERT INTO appointments (patient_id, dentist_id, appointment_date, " +
                         "appointment_time, status, reason, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, appointmentDate);
            stmt.setString(4, appointmentTime);
            stmt.setString(5, status);
            stmt.setString(6, reason);
            stmt.setString(7, notes);

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
            String query = "UPDATE appointments SET patient_id = ?, dentist_id = ?, " +
                         "appointment_date = ?, appointment_time = ?, status = ?, " +
                         "reason = ?, notes = ? WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, appointmentDate);
            stmt.setString(4, appointmentTime);
            stmt.setString(5, status);
            stmt.setString(6, reason);
            stmt.setString(7, notes);
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
            String query = "DELETE FROM appointments WHERE id = ?";
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
        return "Appointment [id=" + id + ", patient=" + patientName + ", date=" + appointmentDate + 
               ", time=" + appointmentTime + ", status=" + status + ", reason=" + reason + "]";
    }
}