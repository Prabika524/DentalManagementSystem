package Model;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import myjdbc.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import java.sql.*;

public class MainApp extends Application {

    private TableView<Appointment> tableView;

    @Override
    public void start(Stage primaryStage) {
        // Create a TableView
        tableView = new TableView<>();

        // Create columns for the TableView
        TableColumn<Appointment, String> patientNameCol = new TableColumn<>("Patient Name");
        patientNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatientName()));

        TableColumn<Appointment, String> appointmentDateCol = new TableColumn<>("Appointment Date");
        appointmentDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAppointmentDate().toString()));

        tableView.getColumns().add(patientNameCol);
        tableView.getColumns().add(appointmentDateCol);

        // Create a VBox and add the TableView to it
        VBox vbox = new VBox(tableView);

        // Fetch appointments for a specific patient
        fetchAppointments(1); // Assume patientID = 1

        // Set up the Scene and Stage
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Dental Management System");
        primaryStage.show();
    }

    private void fetchAppointments(int patientID) {
        try (Connection conn = DatabaseConnection.connect()) {
            String query = "SELECT p.patientName, a.appointmentDate FROM Appointments a JOIN Patients p ON a.patientID = p.patientID WHERE a.patientID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, patientID);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String patientName = rs.getString("patientName");
                    Date appointmentDate = rs.getDate("appointmentDate");

                    // Add the result to the TableView
                    tableView.getItems().add(new Appointment(patientName, appointmentDate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static class Appointment {
        private final SimpleStringProperty patientName;
        private final SimpleObjectProperty<Date> appointmentDate;

        public Appointment(String patientName, Date appointmentDate) {
            this.patientName = new SimpleStringProperty(patientName);
            this.appointmentDate = new SimpleObjectProperty<>(appointmentDate);
        }

        public String getPatientName() {
            return patientName.get();
        }

        public Date getAppointmentDate() {
            return appointmentDate.get();
        }
    }
}

