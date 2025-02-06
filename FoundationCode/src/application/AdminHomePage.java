package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	VBox layout = new VBox(20);
    	
    	layout.setAlignment(Pos.CENTER);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!"); 
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    // HBox for Invite User and User Forgot Password buttons
	    HBox buttonBox = new HBox(20); // Add spacing
	    buttonBox.setAlignment(Pos.CENTER);
	    
	    // Button to invite a user to the system
	    Button inviteUserButton = new Button("Invite User");
//	    continueButton.setOnAction(a -> {
//	    		new InviteUserPage().show(primaryStage);
//	    }); //TODO: redirect to InviteUserPage
	    
	 // Button to navigate to the user's respective page based on their role
	    Button userForgotPasswordButton = new Button("User Forgot Password");
//	    continueButton.setOnAction(a -> {
//	    		new UserForgotPasswordPage().show(primaryStage);
//	    }); //TODO: redirect to UserForgotPasswordPage
	    
	    buttonBox.getChildren().addAll(inviteUserButton, userForgotPasswordButton);
	    
	 // Button to navigate to the user's respective page based on their role
	    Button displayUsersButton = new Button("Display Users");
//	    continueButton.setOnAction(a -> {
//	    		new userForgotPasswordPage().show(primaryStage);
//	    }); //TODO: redirect to DisplayUserPage

	    layout.getChildren().addAll(adminLabel, buttonBox, displayUsersButton);
	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
}
