package Model;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Dentist extends Application {

    private TextField dentistIDField;
    private PasswordField passwordField;
    private Button loginButton;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        // UI Elements
        dentistIDField = new TextField();
        passwordField = new PasswordField();
        loginButton = new Button("Login");
        statusLabel = new Label();

        // GridPane layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Adding UI elements to the grid
        grid.add(new Label("Dentist ID:"), 0, 0);
        grid.add(dentistIDField, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(statusLabel, 1, 3);

        // Button event handling
        loginButton.setOnAction(e -> handleLogin());

        // Scene setup
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Dentist Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Handle the dentist login
    private void handleLogin() {
        String dentistID = dentistIDField.getText();
        String password = passwordField.getText();

        if (dentistID.equals("dentist1") && password.equals("dentist123")) {  // Replace with actual validation logic
            statusLabel.setText("Login successful!");
            showDentistDashboard();
        } else {
            statusLabel.setText("Invalid credentials, try again.");
        }
    }

    // After login, show the Dentist Dashboard
    private void showDentistDashboard() {
        // Create another scene for Dentist Dashboard
        Label welcomeLabel = new Label("Welcome, Dentist!");
        Button viewPatientHistoryButton = new Button("View Patient History");
        Button addTreatmentButton = new Button("Add Treatment");

        viewPatientHistoryButton.setOnAction(e -> viewPatientHistory());
        addTreatmentButton.setOnAction(e -> addTreatment());

        GridPane dashboardGrid = new GridPane();
        dashboardGrid.setVgap(10);
        dashboardGrid.setHgap(10);
        dashboardGrid.setAlignment(Pos.CENTER);

        dashboardGrid.add(welcomeLabel, 0, 0);
        dashboardGrid.add(viewPatientHistoryButton, 0, 1);
        dashboardGrid.add(addTreatmentButton, 0, 2);

        Scene dashboardScene = new Scene(dashboardGrid, 400, 300);
        Stage dashboardStage = new Stage();
        dashboardStage.setTitle("Dentist Dashboard");
        dashboardStage.setScene(dashboardScene);
        dashboardStage.show();
    }

    // Dentist views patient history (Placeholder)
    private void viewPatientHistory() {
        System.out.println("Viewing patient history...");
        // Logic to view patient history goes here (could interact with a database)
    }

    // Dentist adds treatment (Placeholder)
    private void addTreatment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Treatment");
        dialog.setHeaderText("Enter treatment details:");
        dialog.setContentText("Treatment Description:");

        dialog.showAndWait().ifPresent(treatment -> {
            // Example treatment added
            System.out.println("Treatment added: " + treatment);
            // You can further implement logic to add this treatment to a patient's record
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}

