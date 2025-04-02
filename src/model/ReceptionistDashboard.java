package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import myjdbc.DatabaseConnection;

public class ReceptionistDashboard extends Application {

    @Override
    public void start(Stage primaryStage) {
    	VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");

        // Header with title and logout button
        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Receptionist Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        headerBox.getChildren().addAll(titleLabel, btnLogout);

        Button btnRegisterPatient = new Button("Register New Patient");
        btnRegisterPatient.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnRegisterPatient.setOnAction(e -> showRegisterPatientPage(primaryStage));

        Button btnViewPatients = new Button("View Patients");
        btnViewPatients.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnViewPatients.setOnAction(e -> showViewPatientsPage(primaryStage));

        Button btnManageAppointments = new Button("Manage Appointments");
        btnManageAppointments.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnManageAppointments.setOnAction(e -> showManageAppointmentsPage(primaryStage));

        mainLayout.getChildren().addAll(headerBox, btnRegisterPatient, btnViewPatients, btnManageAppointments);
        mainLayout.setAlignment(javafx.geometry.Pos.CENTER);

        Scene mainScene = new Scene(mainLayout, 400, 350);
        primaryStage.setTitle("Receptionist Dashboard");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void showRegisterPatientPage(Stage primaryStage) {
        VBox registerLayout = new VBox(15);
        registerLayout.setPadding(new Insets(20));
        registerLayout.setStyle("-fx-background-color: #e6f3ff;");

        Label registerLabel = new Label("Register New Patient");
        registerLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        TextField txtName = new TextField();
        TextField txtEmail = new TextField();
        TextField txtPhone = new TextField();
        TextField txtAddress = new TextField();
        DatePicker dpDob = new DatePicker();
        ComboBox<String> cbGender = new ComboBox<>();
        cbGender.getItems().addAll("Male", "Female", "Other");

        formGrid.addRow(0, new Label("Full Name:"), txtName);
        formGrid.addRow(1, new Label("Email:"), txtEmail);
        formGrid.addRow(2, new Label("Phone:"), txtPhone);
        formGrid.addRow(3, new Label("Address:"), txtAddress);
        formGrid.addRow(4, new Label("Date of Birth:"), dpDob);
        formGrid.addRow(5, new Label("Gender:"), cbGender);

        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        Button btnRegister = new Button("Register");
        btnRegister.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnRegister.setOnAction(e -> {
            if (validatePatientForm(txtName, txtEmail, txtPhone, txtAddress, dpDob, cbGender)) {
                try {
                    registerPatient(
                        txtName.getText(),
                        txtEmail.getText(),
                        txtPhone.getText(),
                        txtAddress.getText(),
                        dpDob.getValue().toString(),
                        cbGender.getValue()
                    );
                    showAlert("Success", "Patient registered successfully!");
                    start(primaryStage);
                } catch (SQLException ex) {
                    showAlert("Error", "Failed to register patient: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnBack.setOnAction(e -> start(primaryStage));

        buttonBox.getChildren().addAll(btnRegister, btnBack);
        registerLayout.getChildren().addAll(registerLabel, formGrid, buttonBox);

        Scene registerScene = new Scene(registerLayout, 450, 450);
        primaryStage.setScene(registerScene);
    }

    private void showViewPatientsPage(Stage primaryStage) {
        VBox viewLayout = new VBox(15);
        viewLayout.setPadding(new Insets(20));
        viewLayout.setStyle("-fx-background-color: #e6f3ff;");

        Label viewLabel = new Label("View Patients");
        viewLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TODO: Add actual patient viewing functionality here
        Label placeholder = new Label("Patient list will be displayed here");
        placeholder.setStyle("-fx-font-size: 16px;");

        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnBack.setOnAction(e -> start(primaryStage));

        viewLayout.getChildren().addAll(viewLabel, placeholder, btnBack);
        Scene viewScene = new Scene(viewLayout, 600, 400);
        primaryStage.setScene(viewScene);
    }

    private void showManageAppointmentsPage(Stage primaryStage) {
        VBox manageLayout = new VBox(15);
        manageLayout.setPadding(new Insets(20));
        manageLayout.setStyle("-fx-background-color: #e6f3ff;");

        Label manageLabel = new Label("Manage Appointments");
        manageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // TODO: Add actual appointment management functionality here
        Label placeholder = new Label("Appointment management will be displayed here");
        placeholder.setStyle("-fx-font-size: 16px;");

        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnBack.setOnAction(e -> start(primaryStage));

        manageLayout.getChildren().addAll(manageLabel, placeholder, btnBack);
        Scene manageScene = new Scene(manageLayout, 600, 400);
        primaryStage.setScene(manageScene);
    }

    private boolean validatePatientForm(TextField name, TextField email, TextField phone, 
                                      TextField address, DatePicker dob, ComboBox<String> gender) {
        if (name.getText().isEmpty() || email.getText().isEmpty() || phone.getText().isEmpty() ||
            address.getText().isEmpty() || dob.getValue() == null || gender.getValue() == null) {
            showAlert("Error", "Please fill all fields");
            return false;
        }
        return true;
    }

    private void registerPatient(String name, String email, String phone, String address, 
                               String dob, String gender) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String query = "INSERT INTO patients (name, email, phone, address, dob, gender) " +
                         "VALUES (?, ?, ?, ?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, address);
            stmt.setString(5, dob);
            stmt.setString(6, gender);
            stmt.executeUpdate();
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
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