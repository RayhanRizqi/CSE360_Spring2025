package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class ResetPassword {

	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private User user;
	private DatabaseHelper databaseHelper;
	
	public ResetPassword(User user, DatabaseHelper databaseHelper) {
		this.user = user;
		this.databaseHelper = databaseHelper;
    }
	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox(20);
    	layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

    	// Password input text
    	PasswordField passwordField = new PasswordField();
    	passwordField.setPromptText("New Password");
    	passwordField.setMaxWidth(250);
    	
    	// Label to display error messages
    	Label errorLabel = new Label();
    	errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

    	Button setNewPasswordButton = new Button("Set Password");
    	
    	setNewPasswordButton.setOnAction(a -> {
    		// Retrieve user input
    		String password = passwordField.getText();
    		
    		// Check if new password is the same as current password
    		if (password.equals(user.getPassword())) {
    			// Display error label
    			errorLabel.setText("New password cannot be the same as the last password");
    		}
    		else if (password.isEmpty()) {
    			// Display error label
    			errorLabel.setText("Password cannot be blank");
    		}
    	});
    	
    	layout.getChildren().addAll(passwordField);
	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
}