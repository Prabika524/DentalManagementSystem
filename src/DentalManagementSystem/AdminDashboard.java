package DentalManagementSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AdminDashboard extends Application {

    Admin admin;//ssss

    public static void main(String[] args) {
        launch(args);
    }

    
    @Override
    public void start(Stage primaryStage) {
        // Create an Admin instance
        admin = new Admin(1, "Michael");

        // Create the Pane layout
        Pane root = new Pane();

        

        // Manage Users Button
        Button btnManageUsers = new Button("Manage Users");
        btnManageUsers.setLayoutX(20);
        btnManageUsers.setLayoutY(70);
        btnManageUsers.setOnAction(e -> admin.manageUsers());

        // Update Clinic Settings Button
        Button btnUpdateSettings = new Button("Update Clinic Settings");
        btnUpdateSettings.setLayoutX(20);
        btnUpdateSettings.setLayoutY(120);
        btnUpdateSettings.setOnAction(e -> admin.updateClinicSettings());

        // View Reports Button
        Button btnViewReports = new Button("View Reports");
        btnViewReports.setLayoutX(20);
        btnViewReports.setLayoutY(170);
        btnViewReports.setOnAction(e -> admin.viewReports());

        // Add all elements to Pane
        root.getChildren().addAll( btnManageUsers, btnUpdateSettings, btnViewReports);

        // Setup the Scene and Stage
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

// Admin Class (Original with JavaFX Integration)
class Admin {
    int id;
    String name;

    public Admin(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void manageUsers() {
        System.out.println("Managing users by Admin " + name);
    }

    public void updateClinicSettings() {
        System.out.println("Clinic settings updated by Admin " + name);
    }

    public void viewReports() {
        System.out.println("Viewing reports by Admin " + name);
    }
}


