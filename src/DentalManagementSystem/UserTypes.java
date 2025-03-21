package DentalManagementSystem;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class UserTypes extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Main layout
        BorderPane root = new BorderPane();
        
//hhh
        // Title label
        Label titleLabel = new Label("Welcome to the Dental Management System");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Subtitle label
        Label subtitleLabel = new Label("User Types");
        subtitleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        // GridPane for center content
        GridPane centerGrid = new GridPane();
        centerGrid.setAlignment(Pos.CENTER);
        centerGrid.setVgap(15); // Vertical spacing between rows

        // Add title and subtitle
        centerGrid.add(titleLabel, 0, 0);
        centerGrid.add(subtitleLabel, 0, 1);

        // Buttons
        Button receptionistBtn = new Button("Receptionist");
        Button adminBtn = new Button("Admin");
        Button dentistBtn = new Button("Dentist");
        Button patientBtn = new Button("Patient");

        // Style for buttons
        String buttonStyle = "-fx-min-width: 200px; -fx-min-height: 30px; -fx-background-color: white; "
                + "-fx-text-fill: black; -fx-font-weight: bold;";

        receptionistBtn.setStyle(buttonStyle);
        adminBtn.setStyle(buttonStyle);
        dentistBtn.setStyle(buttonStyle);
        patientBtn.setStyle(buttonStyle);

        // Add buttons to grid
        centerGrid.add(receptionistBtn, 0, 2);
        centerGrid.add(adminBtn, 0, 3);
        centerGrid.add(dentistBtn, 0, 4);
        centerGrid.add(patientBtn, 0, 5);

        // Add the grid to center of BorderPane
        root.setCenter(centerGrid);

        // Scene and stage
        Scene scene = new Scene(root, 500, 400, Color.WHITE);
        primaryStage.setTitle("Dental Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
