package application;

import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */

public class RoleSelectionPage {

	public void show(DatabaseHelper databaseHelper, Stage primaryStage, User user) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Choose your role!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    layout.getChildren().add(userLabel);
    	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
    	
    	HBox buttonBox = new HBox(20); 
	    buttonBox.setAlignment(Pos.CENTER);
    	List<String> roles = user.getRoles();
    	
    	if(roles.contains("admin")) {
    		Button adminButton = new Button("Admin");
    		adminButton.setOnAction(a -> {
    			welcomeLoginPage.show(primaryStage, user);
            });
            buttonBox.getChildren().add(adminButton);
    	}
    	if(roles.contains("user")) {
    		Button userButton = new Button("User");
    		userButton.setOnAction(a -> {
    			welcomeLoginPage.show(primaryStage, user);
            });
            buttonBox.getChildren().add(userButton);
    	}
    	if(roles.contains("student")) {
    		Button studentButton = new Button("Student");
    		studentButton.setOnAction(a -> {
    			welcomeLoginPage.show(primaryStage, user);
            });
            buttonBox.getChildren().add(studentButton);
    	}
    	if(roles.contains("instructor")) {
    		Button instructorButton = new Button("Instructor");
    		instructorButton.setOnAction(a -> {
    			welcomeLoginPage.show(primaryStage, user);
            });
            buttonBox.getChildren().add(instructorButton);
    	}
    	if(roles.contains("staff")) {
    		Button staffButton = new Button("staff");
    		staffButton.setOnAction(a -> {
    			welcomeLoginPage.show(primaryStage, user);
            });
    		buttonBox.getChildren().add(staffButton);
    	}
    	
    	
    	 /*
	    for(String role: roles)
	    {
	    	if("admin".equals(role)) {
	    		Button adminButton = new Button("Admin");
	    		adminButton.setOnAction(a -> {
	    			welcomeLoginPage.show(primaryStage, user);
	            });
	            buttonBox.getChildren().add(adminButton);
	    	}
	    	if("user".equals(role)) {
	    		Button userButton = new Button("User");
	    		userButton.setOnAction(a -> {
	    			welcomeLoginPage.show(primaryStage, user);
	            });
	            buttonBox.getChildren().add(userButton);
	    	}
	    	if("student".equals(role)) {
	    		Button studentButton = new Button("Student");
	    		studentButton.setOnAction(a -> {
	    			welcomeLoginPage.show(primaryStage, user);
	            });
	            buttonBox.getChildren().add(studentButton);
	    	}
	    	if("reviewer".equals(role)) {
	    		Button reviewerButton = new Button("Reviewer");
	    		reviewerButton.setOnAction(a -> {
	    			welcomeLoginPage.show(primaryStage, user);
	            });
	            buttonBox.getChildren().add(reviewerButton);	    	}
	    	if("instructor".equals(role)) {
	    		Button instructorButton = new Button("Instructor");
	    		instructorButton.setOnAction(a -> {
	    			welcomeLoginPage.show(primaryStage, user);
	            });
	            buttonBox.getChildren().add(instructorButton);
	    	}
	    	if("staff".equals(role)) {
	    		Button staffButton = new Button("staff");
	    		staffButton.setOnAction(a -> {
	    			welcomeLoginPage.show(primaryStage, user);
	            });
	    		buttonBox.getChildren().add(staffButton);
	    	}
	    }
	    */
        layout.getChildren().add(buttonBox);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("Role Selection Page");
    	
    }
}