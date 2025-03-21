package Model;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class DentistDashboard extends Application {

    private Button viewPatientHistoryButton;
    private Button addTreatmentButton;
    private Button viewAppointmentsButton;
    private Button logoutButton;

    @Override
    public void start(Stage primaryStage) {
        // Buttons for different dashboard functionalities
        viewPatientHistoryButton = new Button("View Patient History");
        addTreatmentButton = new Button("Add Treatment");
        viewAppointmentsButton = new Button("View Appointments");
        logoutButton = new Button("Logout");

        // Set button actions
        viewPatientHistoryButton.setOnAction(e -> viewPatientHistory());
        addTreatmentButton.setOnAction(e -> addTreatment());
        viewAppointmentsButton.setOnAction(e -> viewAppointments());
        logoutButton.setOnAction(e -> logout(primaryStage));

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        // Add buttons to the grid
        grid.add(new Label("Welcome, Dentist!"), 0, 0);
        grid.add(viewPatientHistoryButton, 0, 1);
        grid.add(addTreatmentButton, 0, 2);
        grid.add(viewAppointmentsButton, 0, 3);
        grid.add(logoutButton, 0, 4);

        // Scene setup
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Dentist Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // View Patient History (Placeholder)
    private void viewPatientHistory() {
        // For now, this is a placeholder. You can replace it with actual functionality.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Patient History");
        alert.setHeaderText("Patient History");
        alert.setContentText("Viewing patient history...");
        alert.showAndWait();
    }

    // Add Treatment (Placeholder)
    private void addTreatment() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Treatment");
        dialog.setHeaderText("Enter treatment details:");
        dialog.setContentText("Treatment Description:");

        dialog.showAndWait().ifPresent(treatment -> {
            // Example treatment added
            System.out.println("Treatment added: " + treatment);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Treatment Added");
            alert.setHeaderText("Treatment Added Successfully");
            alert.setContentText("Treatment details: " + treatment);
            alert.showAndWait();
        });
    }

    // View Appointments (Placeholder)
    private void viewAppointments() {
        // For now, this is a placeholder. You can replace it with actual functionality.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointments");
        alert.setHeaderText("View Appointments");
        alert.setContentText("Viewing appointments...");
        alert.showAndWait();
    }

    // Logout from the Dashboard
    private void logout(Stage stage) {
        // Close the current dashboard and return to the login screen
        stage.close();
        System.out.println("Logged out!");
        // Optionally, you can add the login screen back if you want to prompt for login again.
        // New Login window can be opened here if necessary.
    }

    public static void main(String[] args) {
        launch(args);
    }
}
