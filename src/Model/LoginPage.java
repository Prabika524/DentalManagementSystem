package Model;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginPage extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // GridPane for layout
        GridPane grid = new GridPane();
        grid.setVgap(15); // Vertical gap between rows
        grid.setHgap(15); // Horizontal gap between columns
        grid.setStyle("-fx-padding: 20px; -fx-alignment: center;");

        // Title label
        Label lblTitle = new Label("Login");
        lblTitle.setStyle("-fx-font-size: 65px; -fx-text-fill: brown; -fx-font-weight: bolder;"); // Changed to brown
        grid.add(lblTitle, 1, 0);

        // Toggle Buttons for login type
        ToggleGroup toggleGroup = new ToggleGroup();
        ToggleButton btnUser = new ToggleButton("User");  // Changed from Employee
        ToggleButton btnAdmin = new ToggleButton("Admin"); // Changed from HR
        btnUser.setToggleGroup(toggleGroup);
        btnAdmin.setToggleGroup(toggleGroup);
        btnUser.setSelected(true);

        HBox toggleBox = new HBox(10, btnUser, btnAdmin);
        grid.add(new Label("Select login type"), 1, 1);
        grid.add(toggleBox, 1, 2);

        // Input Username and Password Fields
        TextField txtUser = new TextField();
        txtUser.setPromptText("User name/Email");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPromptText("Password");

        grid.add(txtUser, 1, 3);
        grid.add(txtPassword, 1, 4);

        // Forgot Password Link
        Label forgotPassword = new Label("Forgot password?");
        forgotPassword.setStyle("-fx-text-fill: blue; -fx-underline: true;");
        grid.add(forgotPassword, 1, 5);

        // Login Button with Red Color
        Button btnLogin = new Button("Log in");
        btnLogin.setStyle("-fx-background-color: Blue; -fx-text-fill: white;"); // Changed to red
        btnLogin.setMinWidth(200);

        // Success Message Label (Initially Hidden)
        Label successMessage = new Label();
        successMessage.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

        // Event Handler for Login Button
        btnLogin.setOnAction((ActionEvent event) -> {
            successMessage.setText("Login successfully!");
        });

        grid.add(btnLogin, 1, 6);
        grid.add(successMessage, 1, 7); // Message appears below login button

        

        // Scene Setup
        Scene scene = new Scene(grid, 450, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

