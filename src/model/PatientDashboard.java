package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import myjdbc.DatabaseConnection;

public class PatientDashboard extends Application {

    private Patient patient;
    private int patientId;

    @Override
    public void start(Stage primaryStage) {
        // In a real app, this would come from login
        patientId = 1; // Would be set after login
        loadPatientData(patientId);

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");

        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Patient Dashboard - " + (patient != null ? patient.getName() : ""));
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        // Add logout button to header
        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            // Close current dashboard and show login page
            primaryStage.close();
            new LoginPage().start(new Stage());
        });
        
        headerBox.getChildren().addAll(titleLabel, btnLogout);

        Button btnBookAppointment = new Button("Book Appointment");
        btnBookAppointment.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnBookAppointment.setOnAction(e -> showBookAppointmentPage(primaryStage));

        Button btnViewReport = new Button("View Medical History");
        btnViewReport.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnViewReport.setOnAction(e -> showReportsPage(primaryStage));

        mainLayout.getChildren().addAll(headerBox, btnBookAppointment, btnViewReport);
        mainLayout.setAlignment(javafx.geometry.Pos.CENTER);

        Scene mainScene = new Scene(mainLayout, 500, 350);  // Slightly larger to accommodate header
        primaryStage.setTitle("Patient Dashboard");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
    
    private void showReportsPage(Stage primaryStage) {
        VBox reportsLayout = new VBox(15);
        reportsLayout.setPadding(new Insets(20));
        reportsLayout.setStyle("-fx-background-color: #e6f3ff;");

        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label reportsLabel = new Label("Patient Report");
        reportsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });
        
        headerBox.getChildren().addAll(reportsLabel, btnLogout);

        // Create a GridPane for the patient details
        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));

        // Add patient details labels and values
        Label lblPatientId = new Label("Patient ID:");
        Label lblPatientIdValue = new Label(patient != null ? String.valueOf(patient.getId()) : "");
        lblPatientIdValue.setStyle("-fx-font-weight: bold;");
        
        Label lblPatientName = new Label("Patient Name:");
        Label lblPatientNameValue = new Label(patient != null ? patient.getName() : "");
        lblPatientNameValue.setStyle("-fx-font-weight: bold;");
        
        Label lblContactNumber = new Label("Contact Number:");
        Label lblContactNumberValue = new Label(patient != null ? patient.getPhone() : "");
        lblContactNumberValue.setStyle("-fx-font-weight: bold;");
        
        // These would need to come from the database - you'll need to add queries for these
        Label lblDentistId = new Label("Dentist ID:");
        Label lblDentistIdValue = new Label(""); // You would populate this from database
        lblDentistIdValue.setStyle("-fx-font-weight: bold;");
        
        Label lblTreatment = new Label("Treatment:");
        Label lblTreatmentValue = new Label(""); // You would populate this from database
        lblTreatmentValue.setStyle("-fx-font-weight: bold;");
        
        Label lblPrescribedMedicine = new Label("Prescribed Medicine:");
        Label lblPrescribedMedicineValue = new Label(""); // You would populate this from database
        lblPrescribedMedicineValue.setStyle("-fx-font-weight: bold;");

        // Add components to grid
        detailsGrid.addRow(0, lblPatientId, lblPatientIdValue);
        detailsGrid.addRow(1, lblPatientName, lblPatientNameValue);
        detailsGrid.addRow(2, lblContactNumber, lblContactNumberValue);
        detailsGrid.addRow(3, lblDentistId, lblDentistIdValue);
        detailsGrid.addRow(4, lblTreatment, lblTreatmentValue);
        detailsGrid.addRow(5, lblPrescribedMedicine, lblPrescribedMedicineValue);

        // Add medical history section
        Label lblMedicalHistory = new Label("Medical History:");
        lblMedicalHistory.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");
        
        TextArea medicalHistoryArea = new TextArea();
        medicalHistoryArea.setEditable(false);
        medicalHistoryArea.setWrapText(true);
        medicalHistoryArea.setPrefHeight(150);
        
        if (patient != null) {
            medicalHistoryArea.setText(patient.getMedicalHistory());
        } else {
            medicalHistoryArea.setText("No patient data available");
        }

        // Add buttons at the bottom
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        
        Button btnReply = new Button("Reply");
        btnReply.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-min-width: 100px;");
        
        Button btnExit = new Button("Exit");
        btnExit.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-min-width: 100px;");
        btnExit.setOnAction(e -> start(primaryStage));
        
        buttonBox.getChildren().addAll(btnReply, btnExit);

        reportsLayout.getChildren().addAll(
            headerBox, 
            detailsGrid, 
            lblMedicalHistory, 
            medicalHistoryArea, 
            buttonBox
        );
        
        Scene reportsScene = new Scene(reportsLayout, 500, 500);
        primaryStage.setScene(reportsScene);
    }

    private void showBookAppointmentPage(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #e6f3ff;");

        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Book Appointment");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        // Add logout button to appointment page header
        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });
        
        headerBox.getChildren().addAll(titleLabel, btnLogout);

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

     // Dentist selection
        Label lblDentistInfo = new Label("Select Dentist:");
        Label lblDentist = new Label("Dentist:");
        ComboBox<String> cbDentist = new ComboBox<>();
        try {
            cbDentist.getItems().addAll(getDentists());
        } catch (SQLException e) {
            showAlert("Error", "Failed to load dentists: " + e.getMessage());
        }

     // Date picker
        Label lblDateInfo = new Label("Select Appointment Date:");
        Label lblDate = new Label("Date:");
        DatePicker datePicker = new DatePicker();

        // Time selection
        Label lblTimeInfo = new Label("Select Appointment Time:");
        Label lblTime = new Label("Time:");
        ComboBox<String> cbTime = new ComboBox<>();
        cbTime.getItems().addAll("09:00", "10:00", "11:00", "13:00", "14:00", "15:00");

        // Reason
        Label lblReasonInfo = new Label("Enter Appointment Reason:");
        Label lblReason = new Label("Reason:");
        TextArea taReason = new TextArea();
        taReason.setPrefRowCount(3);

        // Add components to grid with info labels
        formGrid.addRow(0, lblDentistInfo);
        formGrid.addRow(1, lblDentist, cbDentist);
        formGrid.addRow(2, lblDateInfo);
        formGrid.addRow(3, lblDate, datePicker);
        formGrid.addRow(4, lblTimeInfo);
        formGrid.addRow(5, lblTime, cbTime);
        formGrid.addRow(6, lblReasonInfo);
        formGrid.addRow(7, lblReason, taReason);
        
        // Style the info labels
        lblDentistInfo.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 5 0;");
        lblDateInfo.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");
        lblTimeInfo.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");
        lblReasonInfo.setStyle("-fx-font-weight: bold; -fx-padding: 10 0 5 0;");

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button btnOk = new Button("Book");
        btnOk.setOnAction(e -> {
            if (cbDentist.getValue() == null || datePicker.getValue() == null || 
                cbTime.getValue() == null || taReason.getText().isEmpty()) {
                showAlert("Error", "Please fill all fields");
                return;
            }

            try {
                int dentistId = Integer.parseInt(cbDentist.getValue().split(" - ")[0]);
                bookAppointment(patientId, dentistId, datePicker.getValue().toString(), 
                               cbTime.getValue(), taReason.getText());
                showAlert("Success", "Appointment booked successfully!");
                start(primaryStage);
            } catch (SQLException ex) {
                showAlert("Error", "Failed to book appointment: " + ex.getMessage());
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid dentist selection");
            }
        });

        Button btnCancel = new Button("Cancel");
        btnCancel.setOnAction(e -> start(primaryStage));

        buttonBox.getChildren().addAll(btnOk, btnCancel);
        layout.getChildren().addAll(headerBox, formGrid, buttonBox);

        primaryStage.setScene(new Scene(layout, 500, 400));
    }

   
    
    private void loadPatientData(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM patients WHERE id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
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
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load patient data: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                DatabaseConnection.closeConnection(conn);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private ObservableList<String> getDentists() throws SQLException {
        ObservableList<String> dentists = FXCollections.observableArrayList();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT id, name, specialization FROM dentists";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                dentists.add(rs.getInt("id") + " - " + rs.getString("name") + 
                           " (" + rs.getString("specialization") + ")");
            }
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
        return dentists;
    }

    private void bookAppointment(int patientId, int dentistId, String date, String time, String reason) 
            throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO appointments (patient_id, dentist_id, appointment_date, " +
                         "appointment_time, reason, status) VALUES (?, ?, ?, ?, ?, 'Scheduled')";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            stmt.setInt(2, dentistId);
            stmt.setString(3, date);
            stmt.setString(4, time);
            stmt.setString(5, reason);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}