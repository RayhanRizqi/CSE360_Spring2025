package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.UUID;


/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class AdminHomePage {
	private DatabaseHelper dbHelper;
	
	public AdminHomePage() {
		dbHelper = new DatabaseHelper();
		try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
    	
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // label to display the welcome message for the admin
	    Label adminLabel = new Label("Hello, Admin!");
	    
	    adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // Password Reset
	    VBox resetSection = createPasswordResetSection();

	    layout.getChildren().addAll(adminLabel, resetSection);
	    Scene adminScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(adminScene);
	    primaryStage.setTitle("Admin Page");
    }
    
    // Password Reset Functions
    private VBox createPasswordResetSection() {
        VBox resetSection = new VBox(10);
        resetSection.setStyle("-fx-padding: 20; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        
        Label titleLabel = new Label("Reset User Password");
        titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter username");
        userNameField.setMaxWidth(300);
        
        Button resetButton = new Button("Generate One-Time Password");
        Label resultLabel = new Label();
        resultLabel.setWrapText(true);
        
        resetButton.setOnAction(e -> {
            String userName = userNameField.getText().trim();
            
            if (userName.isEmpty()) {
                resultLabel.setText("Please enter a username");
                resultLabel.setStyle("-fx-text-fill: red;");
                return;
            }
            
            try {
                if (!dbHelper.doesUserExist(userName)) {
                    resultLabel.setText("User does not exist");
                    resultLabel.setStyle("-fx-text-fill: red;");
                    return;
                }
                
                // Generate a random one-time password
                String tempPassword = UUID.randomUUID().toString().substring(0, 8);
                dbHelper.setOneTimePassword(userName, tempPassword);
                
                resultLabel.setText("One-time password generated for " + userName + ": " + tempPassword);
                resultLabel.setStyle("-fx-text-fill: green;");
                
            } catch (SQLException ex) {
                resultLabel.setText("Error occurred while resetting password");
                resultLabel.setStyle("-fx-text-fill: red;");
                ex.printStackTrace();
            }
        });
        
        resetSection.getChildren().addAll(titleLabel, userNameField, resetButton, resultLabel);
        return resetSection;
    }
}
