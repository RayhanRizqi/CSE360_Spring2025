package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This page displays a simple welcome message for the user.
 */
public class UserHomePage {

	private User user;
	private DatabaseHelper databaseHelper;
	
	public UserHomePage(User user, DatabaseHelper databaseHelper) {
		this.user = user;
		this.databaseHelper = databaseHelper;
    }
	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label userLabel = new Label("Hello, User!");
	    userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    Button logOutButton = new Button("Log Out");
	    logOutButton.setOnAction(a -> {
	    	new UserLogout(databaseHelper, user.getUserName()).show(primaryStage);
	    });

	    layout.getChildren().addAll(userLabel, logOutButton);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}