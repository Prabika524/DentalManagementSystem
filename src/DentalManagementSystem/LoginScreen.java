package DentalManagementSystem;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login user type");

        // Create a BorderPane as the root layout
        BorderPane root = new BorderPane();

        // Create a GridPane for the form
        GridPane formPane = new GridPane();
        formPane.setPadding(new Insets(20)); // Padding around the form
        formPane.setHgap(10); // Horizontal gap between columns
        formPane.setVgap(10); // Vertical gap between rows

        // Username Label and TextField
        Label userLabel = new Label("Username:");
        TextField usernameField = new TextField();

        // Password Label and PasswordField
        Label passLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        // Login Button
        Button loginButton = new Button("Login");

        // Status Label
        Label statusLabel = new Label();

        // Add components to GridPane (column, row)
        formPane.add(userLabel, 0, 0);
        formPane.add(usernameField, 1, 0);
        formPane.add(passLabel, 0, 1);
        formPane.add(passwordField, 1, 1);
        formPane.add(loginButton, 1, 2);
        formPane.add(statusLabel, 1, 3);

        // Set GridPane to center of BorderPane
        root.setCenter(formPane);

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Sample login check
            if (username.equals("recept01") && password.equals("pass123")) {
                statusLabel.setText("Logged in as Receptionist");
            } else if (username.equals("prabika") && password.equals("123")) {
                statusLabel.setText("Logged in as Patient");
            } else if (username.equals("dr_John") && password.equals("456")) {
                statusLabel.setText("Logged in as Dentist");
                
            } else if (username.equals("prati") && password.equals("789")) {
                statusLabel.setText("Logged in as Receptionist");
            }else{
                statusLabel.setText("login");
            }
        });

        // Scene setup
        Scene scene = new Scene(root, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
