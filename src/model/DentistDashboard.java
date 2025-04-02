package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import myjdbc.DatabaseConnection;

public class DentistDashboard extends Application {
    private Dentist dentist;
    private int dentistId;
    private Stage primaryStage;

    public DentistDashboard(int dentistId) {
        this.dentistId = dentistId;
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            if (!loadDentistData(dentistId)) {
                showAlert("Error", "Failed to load dentist data. Please contact administrator.");
                primaryStage.close();
                new LoginPage().start(new Stage());
                return;
            }

            VBox mainLayout = createMainDashboard();
            Scene mainScene = new Scene(mainLayout, 400, 300);
            primaryStage.setTitle("Dentist Dashboard - Dr. " + dentist.getName());
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to initialize dashboard: " + e.getMessage());
            new LoginPage().start(new Stage());
        }
    }

    private VBox createMainDashboard() {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");

        Label titleLabel = new Label("Dentist Dashboard - Dr. " + dentist.getName());
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnViewAppointments = new Button("View Appointments");
        btnViewAppointments.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnViewAppointments.setOnAction(e -> showAppointmentsPage());

        Button btnViewReports = new Button("View Reports");
        btnViewReports.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnViewReports.setOnAction(e -> showReportsPage());

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        mainLayout.getChildren().addAll(titleLabel, btnViewAppointments, btnViewReports, btnLogout);
        mainLayout.setAlignment(Pos.CENTER);
        return mainLayout;
    }

    private boolean loadDentistData(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM dentists WHERE id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
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
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAppointmentsPage() {
        VBox appointmentsLayout = new VBox(15);
        appointmentsLayout.setPadding(new Insets(20));
        appointmentsLayout.setStyle("-fx-background-color: #e6f3ff;");

        Label appointmentsLabel = new Label("Today's Appointments");
        appointmentsLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TableView<Appointment> tableView = new TableView<>();

        TableColumn<Appointment, String> patientCol = new TableColumn<>("Patient");
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        
        TableColumn<Appointment, String> timeCol = new TableColumn<>("Time");  
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTime"));
        
        TableColumn<Appointment, String> reasonCol = new TableColumn<>("Reason");
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("reason"));

        tableView.getColumns().addAll(patientCol, timeCol, reasonCol);

        try {
            tableView.getItems().addAll(getTodaysAppointments(dentistId));
        } catch (SQLException e) {
            showAlert("Error", "Failed to load appointments: " + e.getMessage());
            e.printStackTrace();
        }

        HBox buttonBox = new HBox(20);  
        buttonBox.setAlignment(Pos.CENTER);

        Button btnDetails = new Button("View Details");  
        btnDetails.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");  
        btnDetails.setOnAction(e -> {  
            Appointment selected = tableView.getSelectionModel().getSelectedItem();  
            if (selected != null) {  
                showAppointmentDetails(selected);
            } else {  
                showAlert("Error", "Please select an appointment");  
            }
        });

        Button btnComplete = new Button("Complete");  
        btnComplete.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");  
        btnComplete.setOnAction(e -> {  
            Appointment selected = tableView.getSelectionModel().getSelectedItem();  
            if (selected != null) {  
                completeAppointment(selected.getId());
                tableView.getItems().remove(selected);  
                showAlert("Success", "Appointment marked as completed");  
            } else {  
                showAlert("Error", "Please select an appointment");  
            }
        });  

        Button btnBack = new Button("Back to Dashboard");  
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");  
        btnBack.setOnAction(e -> start(primaryStage));

        buttonBox.getChildren().addAll(btnDetails, btnComplete, btnBack);

        appointmentsLayout.getChildren().addAll(appointmentsLabel, tableView, buttonBox);

        Scene appointmentsScene = new Scene(appointmentsLayout, 600, 400);
        primaryStage.setScene(appointmentsScene);
    }

    private void showAppointmentDetails(Appointment appointment) {
        VBox detailsLayout = new VBox(15);
        detailsLayout.setPadding(new Insets(20));
        detailsLayout.setStyle("-fx-background-color: #e6f3ff;");

        Label titleLabel = new Label("Appointment Details");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane detailsGrid = new GridPane();
        detailsGrid.setHgap(10);
        detailsGrid.setVgap(10);
        detailsGrid.setPadding(new Insets(10));
        
        Label lblDentistId = new Label("Dentist ID:");
        Label lblDentistIdValue = new Label(String.valueOf(dentistId));
        
        Label lblPatientId = new Label("Patient ID:");
        Label lblPatientIdValue = new Label();
        
        Label lblPatientName = new Label("Patient Name:");
        Label lblPatientNameValue = new Label(appointment.getPatientName());
        
        Label lblContactNumber = new Label("Contact Number:");
        Label lblContactNumberValue = new Label();
        
        try {
            Patient patient = lookupPatientByAppointment(appointment);
            if (patient != null) {
                lblPatientIdValue.setText(String.valueOf(patient.getId()));
                lblContactNumberValue.setText(patient.getPhone());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        detailsGrid.addRow(0, lblDentistId, lblDentistIdValue);
        detailsGrid.addRow(1, lblPatientId, lblPatientIdValue);
        detailsGrid.addRow(2, lblPatientName, lblPatientNameValue);
        detailsGrid.addRow(3, lblContactNumber, lblContactNumberValue);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnConfirm = new Button("Confirm Appointment");
        btnConfirm.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnConfirm.setOnAction(e -> {
            try {
                confirmAppointment(appointment.getId());
                showAlert("Success", "Appointment confirmed successfully");
                showAppointmentsPage();
            } catch (SQLException ex) {
                showAlert("Error", "Failed to confirm appointment: " + ex.getMessage());
            }
        });

        Button btnExit = new Button("Exit");
        btnExit.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnExit.setOnAction(e -> showAppointmentsPage());

        buttonBox.getChildren().addAll(btnConfirm, btnExit);

        detailsLayout.getChildren().addAll(titleLabel, detailsGrid, buttonBox);
        Scene detailsScene = new Scene(detailsLayout, 400, 300);
        primaryStage.setScene(detailsScene);
    }

    private Patient lookupPatientByAppointment(Appointment appointment) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT p.* FROM patients p JOIN appointments a ON p.id = a.patient_id WHERE a.id = ?")) {
            stmt.setInt(1, appointment.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
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
            }
        }
        return null;
    }

    private void confirmAppointment(int appointmentId) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE appointments SET status = 'Confirmed' WHERE id = ?")) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        }
    }

    private ObservableList<Appointment> getTodaysAppointments(int dentistId) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "SELECT a.id, p.name as patient_name, a.appointment_time, a.reason " +
                 "FROM appointments a JOIN patients p ON a.patient_id = p.id " +
                 "WHERE a.dentist_id = ? AND a.appointment_date = CURDATE() AND a.status = 'Scheduled'")) {
            stmt.setInt(1, dentistId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("appointment_time"),
                        rs.getString("reason")
                    ));
                }
            }
        }
        return appointments;
    }

    private void completeAppointment(int appointmentId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "UPDATE appointments SET status = 'Completed' WHERE id = ?")) {
            stmt.setInt(1, appointmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update appointment: " + e.getMessage());
        }
    }

    private void showReportsPage() {
        VBox reportsLayout = new VBox(15);
        reportsLayout.setPadding(new Insets(20));
        reportsLayout.setStyle("-fx-background-color: #e6f3ff;");
        reportsLayout.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Patient Reports");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane reportGrid = new GridPane();
        reportGrid.setHgap(10);
        reportGrid.setVgap(10);
        reportGrid.setPadding(new Insets(10));
        reportGrid.setAlignment(Pos.CENTER);

        Label lblDentistId = new Label("Dentist ID:");
        Label lblDentistIdValue = new Label(String.valueOf(dentistId));
        lblDentistIdValue.setStyle("-fx-font-weight: bold;");

        Label lblPatientId = new Label("Patient ID:");
        TextField tfPatientId = new TextField();
        tfPatientId.setPromptText("Enter Patient ID");

        Label lblPatientName = new Label("Patient Name:");
        Label lblPatientNameValue = new Label();
        lblPatientNameValue.setStyle("-fx-font-weight: bold;");

        Label lblTreatment = new Label("Treatment:");
        TextField tfTreatment = new TextField();
        tfTreatment.setPromptText("Enter Treatment");

        Label lblMedicine = new Label("Medicine:");
        TextField tfMedicine = new TextField();
        tfMedicine.setPromptText("Enter Prescribed Medicine");

        reportGrid.addRow(0, lblDentistId, lblDentistIdValue);
        reportGrid.addRow(1, lblPatientId, tfPatientId);
        reportGrid.addRow(2, lblPatientName, lblPatientNameValue);
        reportGrid.addRow(3, lblTreatment, tfTreatment);
        reportGrid.addRow(4, lblMedicine, tfMedicine);

        Button btnLookup = new Button("Lookup Patient");
        btnLookup.setOnAction(e -> {
            try {
                int patientId = Integer.parseInt(tfPatientId.getText());
                Patient patient = lookupPatient(patientId);
                if (patient != null) {
                    lblPatientNameValue.setText(patient.getName());
                } else {
                    showAlert("Error", "Patient not found");
                }
            } catch (NumberFormatException ex) {
                showAlert("Error", "Please enter a valid Patient ID");
            }
        });
        reportGrid.add(btnLookup, 2, 1);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        Button btnSend = new Button("Send");
        btnSend.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-min-width: 100px;");
        btnSend.setOnAction(e -> {
            if (tfPatientId.getText().isEmpty() || lblPatientNameValue.getText().isEmpty() ||
                tfTreatment.getText().isEmpty() || tfMedicine.getText().isEmpty()) {
                showAlert("Error", "Please fill all fields");
                return;
            }

            try {
                savePatientReport(
                    Integer.parseInt(tfPatientId.getText()),
                    tfTreatment.getText(),
                    tfMedicine.getText()
                );
                showAlert("Success", "Report saved successfully");
                start(primaryStage);
            } catch (SQLException ex) {
                showAlert("Error", "Failed to save report: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Button btnExit = new Button("Exit");
        btnExit.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-min-width: 100px;");
        btnExit.setOnAction(e -> start(primaryStage));

        buttonBox.getChildren().addAll(btnSend, btnExit);

        reportsLayout.getChildren().addAll(titleLabel, reportGrid, buttonBox);
        Scene reportsScene = new Scene(reportsLayout, 500, 350);
        primaryStage.setScene(reportsScene);
    }

    private Patient lookupPatient(int patientId) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM patients WHERE id = ?")) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void savePatientReport(int patientId, String treatment, String medicine) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                 "INSERT INTO patient_reports (dentist_id, patient_id, treatment, prescribed_medicine, report_date) " +
                 "VALUES (?, ?, ?, ?, CURDATE())")) {
            stmt.setInt(1, dentistId);
            stmt.setInt(2, patientId);
            stmt.setString(3, treatment);
            stmt.setString(4, medicine);
            stmt.executeUpdate();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}