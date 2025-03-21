package Model;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Admin extends Application {

    private TextField userIDField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // UI Elements
        userIDField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");
        statusLabel = new Label();

        // GridPane layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Adding UI elements to the grid
        grid.add(new Label("User ID:"), 0, 0);
        grid.add(userIDField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(statusLabel, 1, 3);

        // Button event handling
        loginButton.setOnAction(e -> handleLogin());

        // Scene setup
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Admin Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle the admin login
    private void handleLogin() {
        String userID = userIDField.getText();
        String password = passwordField.getText();

        if (userID.equals("admin") && password.equals("admin123")) {  // Replace with actual validation logic
            statusLabel.setText("Login successful!");
            showAdminDashboard();
        } else {
            statusLabel.setText("Invalid credentials, try again.");
        }
    }

    // After login, show Admin Dashboard
    private void showAdminDashboard() {
        // Create another scene for Admin Dashboard
        Label welcomeLabel = new Label("Welcome Admin!");
        Button manageUsersButton = new Button("Manage Users");
        Button manageAppointmentsButton = new Button("Manage Appointments");

        manageUsersButton.setOnAction(e -> manageUsers());
        manageAppointmentsButton.setOnAction(e -> manageAppointments());

        GridPane dashboardGrid = new GridPane();
        dashboardGrid.setVgap(10);
        dashboardGrid.setHgap(10);
        dashboardGrid.setAlignment(Pos.CENTER);

        dashboardGrid.add(welcomeLabel, 0, 0);
        dashboardGrid.add(manageUsersButton, 0, 1);
        dashboardGrid.add(manageAppointmentsButton, 0, 2);

        Scene dashboardScene = new Scene(dashboardGrid, 400, 300);
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Admin Dashboard");
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();
    }

    // Admin manages users (Placeholder)
    private void manageUsers() {
        System.out.println("Managing users...");
        // Logic to manage users goes here (like adding, removing users)
    }

    // Admin manages appointments (Placeholder)
    private void manageAppointments() {
        System.out.println("Managing appointments...");
        // Logic to manage appointments goes here
    }

    public static void main(String[] args) {
        launch(args);
    }
}
