package DentalManagementSystem;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AppointmentBooking extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create the layout
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        // Create components
        Label patientIdLabel = new Label("Patient ID:");
        TextField patientIdField = new TextField();

        Label dateLabel = new Label("Select Date:");
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        Label treatmentLabel = new Label("Select Treatment:");
        ComboBox<String> treatmentComboBox = new ComboBox<>();
        treatmentComboBox.getItems().addAll("Cleaning", "Filling", "Checkup");

        Button bookButton = new Button("Book Appointment");
        Button cancelButton = new Button("Cancel Appointment");

        // Add components to grid
        grid.add(patientIdLabel, 0, 0);
        grid.add(patientIdField, 1, 0);
        grid.add(dateLabel, 0, 1);
        grid.add(datePicker, 1, 1);
        grid.add(treatmentLabel, 0, 2);
        grid.add(treatmentComboBox, 1, 2);
        grid.add(bookButton, 1, 3);
        grid.add(cancelButton, 0, 3);

        // Event handler for booking appointment
        bookButton.setOnAction(event -> {
            String patientId = patientIdField.getText();
            LocalDate appointmentDate = datePicker.getValue();
            String treatment = treatmentComboBox.getValue();
            if (patientId.isEmpty() || treatment == null) {
                System.out.println("Please fill all fields.");
            } else {
                System.out.println("Appointment booked for Patient ID: " + patientId +
                        " on " + appointmentDate + " with treatment: " + treatment);
            }
        });

        // Event handler for canceling appointment
        cancelButton.setOnAction(event -> {
            patientIdField.clear();
            datePicker.setValue(LocalDate.now());
            treatmentComboBox.setValue(null);
            System.out.println("Appointment canceled.");
        });

        // Scene setup
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setTitle("Book Appointment");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
