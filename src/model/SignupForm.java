package model;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class SignupForm extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Title label
        Label mainTitleLabel = new Label("Dental Management System");
        mainTitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        mainTitleLabel.setTextFill(Color.DARKBLUE);
        root.setTop(mainTitleLabel);
        BorderPane.setAlignment(mainTitleLabel, Pos.CENTER);

        Label titleLabel = new Label("Patient SignUp");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.DARKBLUE);

        // GridPane for center content
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setHgap(15);
        centerGrid.setVgap(15);
        centerGrid.setPadding(new Insets(20));

        // Personal Information Fields
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameField = new TextField();

        Label dobLabel = new Label("Date of Birth:");
        DatePicker dobPicker = new DatePicker();
        dobPicker.setValue(LocalDate.now().minusYears(20));

        Label genderLabel = new Label("Gender:");
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setValue("Male");

        // Contact Information Fields
        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label addressLabel = new Label("Address:");
        TextArea addressArea = new TextArea();
        addressArea.setPrefRowCount(2);
        addressArea.setWrapText(true);

        // Account Information Fields
        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Label confirmPasswordLabel = new Label("Confirm Password:");
        PasswordField confirmPasswordField = new PasswordField();

        // Medical Information
        Label medicalHistoryLabel = new Label("Medical History:");
        TextArea medicalHistoryArea = new TextArea();
        medicalHistoryArea.setPrefRowCount(3);
        medicalHistoryArea.setWrapText(true);

        // Buttons
        Button signupButton = new Button("SingUp");
        signupButton.setOnAction(e -> {
            if (validateFields(firstNameField, lastNameField, emailField, phoneField, 
                    usernameField, passwordField, confirmPasswordField)) {
                registerPatient(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    dobPicker.getValue().toString(),
                    genderCombo.getValue(),
                    phoneField.getText(),
                    emailField.getText(),
                    addressArea.getText(),
                    usernameField.getText(),
                    passwordField.getText(),
                    medicalHistoryArea.getText()
                );
            }
        });

        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> {
            // Close current window and open login page
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        // Style for buttons
        String buttonStyle = "-fx-min-width: 200px; -fx-min-height: 30px; "
                + "-fx-background-color: #2196F3; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-font-size: 14px;";
        signupButton.setStyle(buttonStyle);
        
        String backButtonStyle = "-fx-min-width: 150px; -fx-min-height: 30px; "
                + "-fx-background-color: #f44336; -fx-text-fill: white; "
                + "-fx-font-weight: bold; -fx-font-size: 14px;";
        backButton.setStyle(backButtonStyle);

        // Button container
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton, signupButton);

        // Add components to grid
        int row = 0;
        centerGrid.add(titleLabel, 0, row++, 2, 1);
        
        // Personal Info
        centerGrid.add(new Label("Personal Information:"), 0, row++, 2, 1);
        centerGrid.add(firstNameLabel, 0, row);
        centerGrid.add(firstNameField, 1, row++);
        centerGrid.add(lastNameLabel, 0, row);
        centerGrid.add(lastNameField, 1, row++);
        centerGrid.add(dobLabel, 0, row);
        centerGrid.add(dobPicker, 1, row++);
        centerGrid.add(genderLabel, 0, row);
        centerGrid.add(genderCombo, 1, row++);
        
        // Contact Info
        centerGrid.add(new Label("Contact Information:"), 0, row++, 2, 1);
        centerGrid.add(phoneLabel, 0, row);
        centerGrid.add(phoneField, 1, row++);
        centerGrid.add(emailLabel, 0, row);
        centerGrid.add(emailField, 1, row++);
        centerGrid.add(addressLabel, 0, row);
        centerGrid.add(addressArea, 1, row++);
        
        // Account Info
        centerGrid.add(new Label("Account Information:"), 0, row++, 2, 1);
        centerGrid.add(usernameLabel, 0, row);
        centerGrid.add(usernameField, 1, row++);
        centerGrid.add(passwordLabel, 0, row);
        centerGrid.add(passwordField, 1, row++);
        centerGrid.add(confirmPasswordLabel, 0, row);
        centerGrid.add(confirmPasswordField, 1, row++);
        
        // Medical Info
        centerGrid.add(new Label("Medical Information:"), 0, row++, 2, 1);
        centerGrid.add(medicalHistoryLabel, 0, row);
        centerGrid.add(medicalHistoryArea, 1, row++);
        
        // Buttons
        centerGrid.add(buttonBox, 0, row++, 2, 1);

        // Add grid to center of BorderPane
        ScrollPane scrollPane = new ScrollPane(centerGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        root.setCenter(scrollPane);

        // Scene and stage
        Scene scene = new Scene(root, 700, 600);
        primaryStage.setTitle("Patient Registration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean validateFields(TextField firstName, TextField lastName, TextField email, 
            TextField phone, TextField username, PasswordField password, PasswordField confirmPassword) {
        
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || 
            email.getText().isEmpty() || phone.getText().isEmpty() || 
            username.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert("Error", "All fields are required!");
            return false;
        }
        
        if (!password.getText().equals(confirmPassword.getText())) {
            showAlert("Error", "Passwords do not match!");
            return false;
        }
        
        if (password.getText().length() < 6) {
            showAlert("Error", "Password must be at least 6 characters long!");
            return false;
        }
        
        if (!email.getText().contains("@") || !email.getText().contains(".")) {
            showAlert("Error", "Please enter a valid email address!");
            return false;
        }
        
        return true;
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void registerPatient(String firstName, String lastName, String dob, String gender, 
            String phone, String email, String address, String username, String password, String medicalHistory) {
        
        String url = "jdbc:mysql://localhost:3306/dental_clinic";
        String user = "root";
        String pass = "Bhush@n11";

        String patientQuery = "INSERT INTO patients (first_name, last_name, dob, gender, phone, email, address, medical_history) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        String userQuery = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, 'patient')";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // Start transaction
            conn.setAutoCommit(false);
            
            try {
                // Insert into patients table
                PreparedStatement patientStmt = conn.prepareStatement(patientQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                patientStmt.setString(1, firstName);
                patientStmt.setString(2, lastName);
                patientStmt.setString(3, dob);
                patientStmt.setString(4, gender);
                patientStmt.setString(5, phone);
                patientStmt.setString(6, email);
                patientStmt.setString(7, address);
                patientStmt.setString(8, medicalHistory);
                patientStmt.executeUpdate();
                
                // Insert into users table
                PreparedStatement userStmt = conn.prepareStatement(userQuery);
                userStmt.setString(1, username);
                userStmt.setString(2, password);
                userStmt.setString(3, email);
                userStmt.executeUpdate();
                
                // Commit transaction
                conn.commit();
                
                showAlert("Success", "Patient registered successfully!");
                
            } catch (SQLException e) {
                // Rollback transaction if any error occurs
                conn.rollback();
                showAlert("Error", "Registration failed: " + e.getMessage());
                e.printStackTrace();
            }
            
        } catch (SQLException e) {
            showAlert("Error", "Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}