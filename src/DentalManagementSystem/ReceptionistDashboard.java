package DentalManagementSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ReceptionistDashboard extends Application {

    Receptionist receptionist;
    Patient patient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        receptionist = new Receptionist(1, "Prabika Rai");
        patient = new Patient(1, "prabika", "rai@gmail.com", "123456789");

        Pane root = new Pane();

        // Labels and TextFields for Patient Information
        Label lblPatientName = new Label("Patient Name:");
        lblPatientName.setLayoutX(20);
        lblPatientName.setLayoutY(20);
        TextField txtPatientName = new TextField(patient.name);
        txtPatientName.setLayoutX(120);
        txtPatientName.setLayoutY(20);

        Label lblPatientEmail = new Label("Patient Email:");
        lblPatientEmail.setLayoutX(20);
        lblPatientEmail.setLayoutY(60);
        TextField txtPatientEmail = new TextField(patient.email);
        txtPatientEmail.setLayoutX(120);
        txtPatientEmail.setLayoutY(60);

        Label lblPatientPhone = new Label("Patient Phone:");
        lblPatientPhone.setLayoutX(20);
        lblPatientPhone.setLayoutY(100);
        TextField txtPatientPhone = new TextField(patient.phone);
        txtPatientPhone.setLayoutX(120);
        txtPatientPhone.setLayoutY(100);

        

        // Button to Add Patient
        Button btnAddPatient = new Button("Add Patient");
        btnAddPatient.setLayoutX(20);
        btnAddPatient.setLayoutY(190);
        btnAddPatient.setOnAction(e -> {
            // Create Patient object from entered data
            Patient newPatient = new Patient(0, txtPatientName.getText(), txtPatientEmail.getText(), txtPatientPhone.getText());
            receptionist.addPatient(newPatient);
        });

        // Button to Book Appointment
        Button btnBookAppointment = new Button("Verify Patient Deatils");
        btnBookAppointment.setLayoutX(120);
        btnBookAppointment.setLayoutY(190);
        btnBookAppointment.setOnAction(e -> {
        	// Create Patient object from entered data
            Patient newPatient = new Patient(0, txtPatientName.getText(), txtPatientEmail.getText(), txtPatientPhone.getText());
            receptionist.addPatient(newPatient);
        	
        });

        // Add all elements to Pane
        root.getChildren().addAll(lblPatientName, txtPatientName,
                                  lblPatientEmail, txtPatientEmail,
                                  lblPatientPhone, txtPatientPhone,
                                  btnAddPatient, btnBookAppointment);

        // Scene and Stage setup
        Scene scene = new Scene(root, 350, 270);
        primaryStage.setTitle("Receptionist ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

// Receptionist Class (Original with JavaFX Integration)
class Receptionist {
    int id;
    String name;

    public Receptionist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addPatient(Patient p) {
        System.out.println("Patient " + p.name + " added by " + name);
    }

    public void bookAppointment(int appointmentId) {
        System.out.println("Verify Ptient Details " + appointmentId + " booked by " + name);
    }
}

// Patient Class for Reference
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

