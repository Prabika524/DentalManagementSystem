package DentalManagementSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class PatientDashboard extends Application {

    Patient patient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        patient = new Patient(1, "Prabika Rai", "prabik@gmail.com", "9815056013");

        Pane root = new Pane();

        // Labels and TextFields for Patient Information
        Label lblName = new Label("Name:");
        lblName.setLayoutX(20);
        lblName.setLayoutY(20);
        TextField txtName = new TextField(patient.name);
        txtName.setLayoutX(100);
        txtName.setLayoutY(20);

        Label lblEmail = new Label("Email:");
        lblEmail.setLayoutX(20);
        lblEmail.setLayoutY(60);
        TextField txtEmail = new TextField(patient.email);
        txtEmail.setLayoutX(100);
        txtEmail.setLayoutY(60);

        Label lblPhone = new Label("Phone:");
        lblPhone.setLayoutX(20);
        lblPhone.setLayoutY(100);
        TextField txtPhone = new TextField(patient.phone);
        txtPhone.setLayoutX(100);
        txtPhone.setLayoutY(100);

        // Buttons for Actions
        Button btnViewHistory = new Button("View History");
        btnViewHistory.setLayoutX(20);
        btnViewHistory.setLayoutY(150);
        btnViewHistory.setOnAction(e -> patient.viewAppointmentHistory());

        Button btnCancelAppointment = new Button("Cancel Appointment");
        btnCancelAppointment.setLayoutX(120);
        btnCancelAppointment.setLayoutY(150);
        btnCancelAppointment.setOnAction(e -> patient.cancelAppointment(101));

        // Add all nodes to Pane
        root.getChildren().addAll(lblName, txtName, lblEmail, txtEmail, lblPhone, txtPhone, btnViewHistory, btnCancelAppointment);

        // Scene and Stage setup
        Scene scene = new Scene(root, 350, 250);
        primaryStage.setTitle("Patient Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

// Patient Class (Original)
class Patient {
    int id;
    String name, email, phone;

    public Patient(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public void viewAppointmentHistory() {
        System.out.println("Viewing appointment history for: " + name);
    }

    public void cancelAppointment(int appointmentId) {
        System.out.println("Appointment " + appointmentId + " canceled for: " + name);
    }
}