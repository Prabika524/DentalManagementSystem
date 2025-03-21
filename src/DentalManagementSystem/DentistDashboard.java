package DentalManagementSystem;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DentistDashboard extends Application {

    private Dentist dentist;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        dentist = new Dentist(1, "Dr. Smith");

        Pane root = new Pane();

        // Labels
        Label lblPatientId = new Label("Patient ID:");
        lblPatientId.setLayoutX(20);
        lblPatientId.setLayoutY(20);
        ComboBox<Integer> cmbPatientId = new ComboBox<>();
        cmbPatientId.getItems().addAll(101, 102, 103); // Sample patient IDs
        cmbPatientId.setLayoutX(120);
        cmbPatientId.setLayoutY(20);

        Label lblTreatment = new Label("Treatment:");
        lblTreatment.setLayoutX(20);
        lblTreatment.setLayoutY(60);
        ComboBox<String> cmbTreatment = new ComboBox<>();
        cmbTreatment.getItems().addAll("cleaning", "Filling", "checkup");
        cmbTreatment.setLayoutX(120);
        cmbTreatment.setLayoutY(60);

        Label lblMedicine = new Label("Medicine:");
        lblMedicine.setLayoutX(20);
        lblMedicine.setLayoutY(100);
        ComboBox<String> cmbMedicine = new ComboBox<>();
        cmbMedicine.getItems().addAll("Ibuprofen", "Amoxicillin", "Paracetamol");
        cmbMedicine.setLayoutX(120);
        cmbMedicine.setLayoutY(100);

        // Buttons
        Button btnViewAppointments = new Button("View Appointments");
        btnViewAppointments.setLayoutX(20);
        btnViewAppointments.setLayoutY(150);
        btnViewAppointments.setOnAction(e -> dentist.viewAppointments());

        Button btnUpdateTreatment = new Button("Update Treatment");
        btnUpdateTreatment.setLayoutX(160);
        btnUpdateTreatment.setLayoutY(150);
        btnUpdateTreatment.setOnAction(e -> {
            Integer patientId = cmbPatientId.getValue();
            String treatmentDetails = cmbTreatment.getValue();
            if (patientId != null && treatmentDetails != null) {
                dentist.updateTreatment(patientId, treatmentDetails);
            } else {
                showAlert("Error", "Please select a Patient ID and Treatment.");
            }
        });

        Button btnPrescribeMedicine = new Button("Prescribe Medicine");
        btnPrescribeMedicine.setLayoutX(20);
        btnPrescribeMedicine.setLayoutY(200);
        btnPrescribeMedicine.setOnAction(e -> {
            Integer patientId = cmbPatientId.getValue();
            String medicine = cmbMedicine.getValue();
            if (patientId != null && medicine != null) {
                dentist.prescribeMedicine(patientId, medicine);
            } else {
                showAlert("Error", "Please select a Patient ID and Medicine.");
            }
        });

        // Add elements to Pane
        root.getChildren().addAll(lblPatientId, cmbPatientId,
                lblTreatment, cmbTreatment,
                lblMedicine, cmbMedicine,
                btnViewAppointments, btnUpdateTreatment, btnPrescribeMedicine);

        // Scene and Stage setup
        Scene scene = new Scene(root, 400, 270);
        primaryStage.setTitle("Dentist Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to show alert messages
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

// Dentist Class
class Dentist {
    private int id;
    private String name;

    public Dentist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void viewAppointments() {
        System.out.println("Viewing appointments for Dr. " + name);
    }

    public void updateTreatment(int patientId, String treatmentDetails) {
        System.out.println("Updated treatment for patient " + patientId + ": " + treatmentDetails);
    }

    public void prescribeMedicine(int patientId, String medicine) {
        System.out.println("Prescribed medicine to patient " + patientId + ": " + medicine);
    }
}
