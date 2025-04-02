package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import myjdbc.DatabaseConnection;

public class LoginPage extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-alignment: center;");

        Label lblTitle = new Label("Dental Clinic Login");
        lblTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        grid.add(lblTitle, 0, 0, 2, 1);

        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton rbPatient = new RadioButton("Patient");
        RadioButton rbDentist = new RadioButton("Dentist");
        RadioButton rbReceptionist = new RadioButton("Receptionist");
        RadioButton rbAdmin = new RadioButton("Admin");

        rbPatient.setToggleGroup(toggleGroup);
        rbDentist.setToggleGroup(toggleGroup);
        rbReceptionist.setToggleGroup(toggleGroup);
        rbAdmin.setToggleGroup(toggleGroup);
        rbPatient.setSelected(true);

        HBox toggleBox = new HBox(10, rbPatient, rbDentist, rbReceptionist, rbAdmin);

        grid.add(new Label("Login As:"), 0, 1);
        grid.add(toggleBox, 1, 1);

        Label lblUsername = new Label("Username:");
        TextField txtUsername = new TextField();
        txtUsername.setPromptText("Enter username");
        grid.add(lblUsername, 0, 2);
        grid.add(txtUsername, 1, 2);

        Label lblPassword = new Label("Password:");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Enter password");
        grid.add(lblPassword, 0, 3);
        grid.add(txtPassword, 1, 3);

        Button btnLogin = new Button("Login");
        btnLogin.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnLogin.setMinWidth(200);

        Label lblStatus = new Label();
        lblStatus.setStyle("-fx-text-fill: red;");

        grid.add(btnLogin, 1, 4);
        grid.add(lblStatus, 1, 5);

        btnLogin.setOnAction(e -> {
            String username = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String userType = "";
            
            if (rbPatient.isSelected()) userType = "patient";
            else if (rbDentist.isSelected()) userType = "dentist";
            else if (rbReceptionist.isSelected()) userType = "receptionist";
            else if (rbAdmin.isSelected()) userType = "admin";

            if (username.isEmpty() || password.isEmpty()) {
                lblStatus.setText("Please enter both username and password");
                return;
            }

            try {
                int userId = authenticateUser(username, password, userType);
                if (userId > 0) {
                    lblStatus.setText("Login successful!");
                    lblStatus.setStyle("-fx-text-fill: green;");

                    switch (userType) {
                        case "patient":
                            new PatientDashboard().start(new Stage());
                            break;
                        case "dentist":
                            int dentistId = getDentistIdByUsername(username);
                            if (dentistId > 0) {
                                Platform.runLater(() -> {
                                    DentistDashboard dentistDashboard = new DentistDashboard(dentistId);
                                    Stage dentistStage = new Stage();
                                    dentistDashboard.start(dentistStage);
                                    primaryStage.close();
                                });
                            } else {
                                lblStatus.setText("Dentist profile not found");
                                lblStatus.setStyle("-fx-text-fill: red;");
                            }
                            break;
                        case "receptionist":
                            new ReceptionistDashboard().start(new Stage());
                            break;
                        case "admin":
                            new AdminDashboard().start(new Stage());
                            break;
                    }
                } else {
                    lblStatus.setText("Invalid username or password");
                    lblStatus.setStyle("-fx-text-fill: red;");
                }
            } catch (SQLException ex) {
                lblStatus.setText("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Label lblForgotPassword = new Label("Forgot Password");
        lblForgotPassword.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        lblForgotPassword.setOnMouseClicked(e -> openResetPasswordWindow());
        grid.add(lblForgotPassword, 1, 6);

        Label lblSignup = new Label("Don't have an account? Sign Up");
        lblSignup.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        lblSignup.setOnMouseClicked(e -> {
            new SignupForm().start(new Stage());
            primaryStage.close();
        });
        grid.add(lblSignup, 0, 7, 2, 1);

        Scene scene = new Scene(grid, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private int authenticateUser(String username, String password, String userType) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "SELECT id, password FROM users WHERE username = ? AND role = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, userType);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (password.equals(storedPassword)) {
                    return rs.getInt("id"); // Return user ID
                }
            }
            return -1; // Return -1 for failed authentication
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    private int getDentistIdByUsername(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT d.id FROM dentists d JOIN users u ON d.user_id = u.id WHERE u.username = ?")) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        return -1; // Return -1 if not found
    }

    private void openResetPasswordWindow() {
        Stage resetStage = new Stage();
        resetStage.setTitle("Reset Password");

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(15);
        grid.setPadding(new Insets(20));
        grid.setStyle("-fx-alignment: center;");

        Label lblTitle = new Label("Reset Password");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        grid.add(lblTitle, 0, 0, 2, 1);

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Enter your email");
        grid.add(lblEmail, 0, 1);
        grid.add(txtEmail, 1, 1);

        Label lblNewPassword = new Label("New Password:");
        PasswordField txtNewPassword = new PasswordField();
        grid.add(lblNewPassword, 0, 2);
        grid.add(txtNewPassword, 1, 2);

        Label lblConfirmPassword = new Label("Confirm Password:");
        PasswordField txtConfirmPassword = new PasswordField();
        grid.add(lblConfirmPassword, 0, 3);
        grid.add(txtConfirmPassword, 1, 3);

        Button btnReset = new Button("Reset Password");
        btnReset.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Label lblStatus = new Label();
        grid.add(btnReset, 1, 4);
        grid.add(lblStatus, 1, 5);

        btnReset.setOnAction(e -> {
            String email = txtEmail.getText().trim();
            String newPassword = txtNewPassword.getText();
            String confirmPassword = txtConfirmPassword.getText();

            if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                lblStatus.setText("Please fill all fields");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                lblStatus.setText("Passwords do not match");
                lblStatus.setStyle("-fx-text-fill: red;");
                return;
            }

            try {
                if (resetPassword(email, newPassword)) {
                    lblStatus.setText("Password reset successfully!");
                    lblStatus.setStyle("-fx-text-fill: green;");
                } else {
                    lblStatus.setText("Email not found in system");
                    lblStatus.setStyle("-fx-text-fill: red;");
                }
            } catch (SQLException ex) {
                lblStatus.setText("Database error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        resetStage.setScene(scene);
        resetStage.show();
    }

    private boolean resetPassword(String email, String newPassword) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            String query = "UPDATE users SET password = ? WHERE email = ?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, newPassword);
            stmt.setString(2, email);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } finally {
            if (stmt != null) stmt.close();
            DatabaseConnection.closeConnection(conn);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}