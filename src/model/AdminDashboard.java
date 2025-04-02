package model;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AdminDashboard {

    public void start(Stage primaryStage) {
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");

        // Header with title and logout button
        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        headerBox.getChildren().addAll(titleLabel, btnLogout);

        Button btnManageUsers = new Button("Manage Users");
        btnManageUsers.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnManageUsers.setOnAction(e -> showManageUsersPage(primaryStage));

        Button btnSystemSettings = new Button("System Settings");
        btnSystemSettings.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnSystemSettings.setOnAction(e -> showSystemSettingsPage(primaryStage));

        Button btnGenerateReports = new Button("Generate Reports");
        btnGenerateReports.setStyle("-fx-font-size: 16px; -fx-min-width: 200px;");
        btnGenerateReports.setOnAction(e -> showReportsPage(primaryStage));

        mainLayout.getChildren().addAll(headerBox, btnManageUsers, btnSystemSettings, btnGenerateReports);
        mainLayout.setAlignment(Pos.CENTER);

        Scene mainScene = new Scene(mainLayout, 400, 350);
        primaryStage.setTitle("Admin Dashboard");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private void showManageUsersPage(Stage primaryStage) {
        VBox manageLayout = new VBox(15);
        manageLayout.setPadding(new Insets(20));
        manageLayout.setStyle("-fx-background-color: #e6f3ff;");

        // Header with title and logout button
        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label manageLabel = new Label("User Management");
        manageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        headerBox.getChildren().addAll(manageLabel, btnLogout);

        // User type filter
        ToggleGroup userTypeGroup = new ToggleGroup();
        RadioButton rbAll = new RadioButton("All");
        RadioButton rbPatients = new RadioButton("Patients");
        RadioButton rbDentists = new RadioButton("Dentists");
        RadioButton rbReceptionists = new RadioButton("Receptionists");
        RadioButton rbAdmins = new RadioButton("Admins");

        rbAll.setToggleGroup(userTypeGroup);
        rbPatients.setToggleGroup(userTypeGroup);
        rbDentists.setToggleGroup(userTypeGroup);
        rbReceptionists.setToggleGroup(userTypeGroup);
        rbAdmins.setToggleGroup(userTypeGroup);
        rbAll.setSelected(true);

        HBox radioBox = new HBox(10, rbAll, rbPatients, rbDentists, rbReceptionists, rbAdmins);
        radioBox.setAlignment(Pos.CENTER);

        // Search box
        HBox searchBox = new HBox(10);
        Label searchLabel = new Label("Search:");
        javafx.scene.control.TextField searchField = new javafx.scene.control.TextField();
        searchField.setPromptText("Enter name or ID");
        Button btnSearch = new Button("Search");
        btnSearch.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        searchBox.getChildren().addAll(searchLabel, searchField, btnSearch);
        searchBox.setAlignment(Pos.CENTER);

        // User table
        TableView<User> userTable = new TableView<>();
        
        // Create columns
        TableColumn<User, String> idCol = new TableColumn<>("ID");
        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        TableColumn<User, String> typeCol = new TableColumn<>("Type");
        TableColumn<User, String> statusCol = new TableColumn<>("Status");
        
        // Set column value factories (you'll need to implement these in your User class)
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        userTable.getColumns().addAll(idCol, nameCol, emailCol, typeCol, statusCol);
        
        // Action buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button btnAdd = new Button("Add User");
        btnAdd.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        
        Button btnRemove = new Button("Remove");
        btnRemove.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        
        Button btnUpdate = new Button("Update");
        btnUpdate.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black;");
        
        Button btnSave = new Button("Save Changes");
        btnSave.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        
        buttonBox.getChildren().addAll(btnAdd, btnRemove, btnUpdate, btnSave);
        
        // Add action handlers
        btnAdd.setOnAction(e -> {
            // Show add user dialog
            showAddUserDialog(userTable);
        });
        
        btnRemove.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                userTable.getItems().remove(selectedUser);
            } else {
                showAlert("No Selection", "Please select a user to remove.");
            }
        });
        
        btnUpdate.setOnAction(e -> {
            User selectedUser = userTable.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                showUpdateUserDialog(selectedUser, userTable);
            } else {
                showAlert("No Selection", "Please select a user to update.");
            }
        });
        
        btnSave.setOnAction(e -> {
            // Save changes to database
            saveUsersToDatabase(userTable.getItems());
            showAlert("Success", "Changes saved successfully!");
        });
        
        btnSearch.setOnAction(e -> {
            String searchTerm = searchField.getText().toLowerCase();
            if (searchTerm.isEmpty()) {
                // Show all users if search is empty
                // You would reload all users here
            } else {
                // Filter users based on search term
                // You would implement this filtering logic
            }
        });
        
        // Back button
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnBack.setOnAction(e -> start(primaryStage));
        
        // Load sample data (replace with actual database call)
        userTable.getItems().addAll(getSampleUsers());
        
        manageLayout.getChildren().addAll(headerBox, radioBox, searchBox, userTable, buttonBox, btnBack);
        primaryStage.setScene(new Scene(manageLayout, 800, 600));
    }

    // Helper methods for user management
    private void showAddUserDialog(TableView<User> userTable) {
        // Create a custom dialog
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Add New User");
        
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(20));
        
        // Create form fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Patient", "Dentist", "Receptionist", "Admin");
        typeCombo.setValue("Patient");
        
        // Add button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            // Create new user and add to table
            User newUser = new User(
            );
            userTable.getItems().add(newUser);
            dialog.close();
        });
        
        dialogVBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Type:"), typeCombo,
            addButton
        );
        
        Scene dialogScene = new Scene(dialogVBox, 300, 250);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void showUpdateUserDialog(User user, TableView<User> userTable) {
        // Similar to add dialog but pre-populated with user data
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Update User");
        
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(20));
        
        TextField nameField = new TextField(user.getUsername());
        TextField emailField = new TextField(user.getEmail());
        
        
        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("Patient", "Dentist", "Receptionist", "Admin");
        roleCombo.setValue(user.getRole());

        ComboBox<String> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll("Active", "Inactive");
        statusCombo.setValue(user.getStatus());

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            user.setUsername(nameField.getText());  // or setName() depending on your User class
            user.setEmail(emailField.getText());
            user.setRole(roleCombo.getValue());
            user.setStatus(statusCombo.getValue());
            userTable.refresh();
            dialog.close();
        });

        dialogVBox.getChildren().addAll(
            new Label("Name:"), nameField,
            new Label("Email:"), emailField,
            new Label("Type:"), roleCombo,
            new Label("Status:"), statusCombo,
            updateButton
        );
        
        Scene dialogScene = new Scene(dialogVBox, 300, 300);
        dialog.setScene(dialogScene);
        dialog.showAndWait();
    }

    private void saveUsersToDatabase(ObservableList<User> users) {
        // Implement database save logic here
        // This would connect to your database and save/update all users
    }

    private List<User> getSampleUsers() {
        // Return some sample users for demonstration
        // Replace with actual database call
        return Arrays.asList(
            new User()
            
        );
    }

    private String generateId() {
        // Implement ID generation logic
        return UUID.randomUUID().toString().substring(0, 8);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSystemSettingsPage(Stage primaryStage) {
        // Implementation for system settings
    	VBox manageLayout = new VBox(15);
        manageLayout.setPadding(new Insets(20));
        manageLayout.setStyle("-fx-background-color: #e6f3ff;");

        // Header with title and logout button
        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label manageLabel = new Label("System Settings");
        manageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        headerBox.getChildren().addAll(manageLabel, btnLogout);
    	
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnBack.setOnAction(e -> start(primaryStage));

        

        manageLayout.getChildren().addAll(headerBox, btnBack);
        primaryStage.setScene(new Scene(manageLayout, 600, 400));
    }
        
        
    private void showReportsPage(Stage primaryStage) {
        // Implementation for reports
    	VBox manageLayout = new VBox(15);
        manageLayout.setPadding(new Insets(20));
        manageLayout.setStyle("-fx-background-color: #e6f3ff;");

        // Header with title and logout button
        HBox headerBox = new HBox();
        headerBox.setSpacing(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        Label manageLabel = new Label("Reports");
        manageLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle("-fx-background-color: #ff6b6b; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            primaryStage.close();
            new LoginPage().start(new Stage());
        });

        headerBox.getChildren().addAll(manageLabel, btnLogout);
    	
        Button btnBack = new Button("Back");
        btnBack.setStyle("-fx-font-size: 14px; -fx-min-width: 150px;");
        btnBack.setOnAction(e -> start(primaryStage));

        

        manageLayout.getChildren().addAll(headerBox, btnBack);
        primaryStage.setScene(new Scene(manageLayout, 600, 400));
    }
}