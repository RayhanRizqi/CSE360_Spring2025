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
 * ForgotPassword class provides the interface for users to request
 * a password reset through a one-time password.
 */
public class ForgotPasswordPage {
    private DatabaseHelper dbHelper;
    
    public ForgotPasswordPage() {
        dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void show(Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Title for the page
        Label titleLabel = new Label("Password Reset");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Password Reset Section
        VBox resetSection = createPasswordResetSection(primaryStage);
        
        // Back button to return to login
        Button backButton = new Button("Back to Login");
        backButton.setOnAction(e -> {
            UserLoginPage loginPage = new UserLoginPage(dbHelper);
            loginPage.show(primaryStage);
        });
        
        layout.getChildren().addAll(titleLabel, resetSection, backButton);
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Forgot Password");
    }
    
    private VBox createPasswordResetSection(Stage primaryStage) {
        VBox resetSection = new VBox(10);
        resetSection.setStyle("-fx-padding: 20; -fx-border-color: #cccccc; -fx-border-radius: 5;");
        
        Label instructionLabel = new Label("Enter your username to receive a one-time password");
        instructionLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
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
        
        resetSection.getChildren().addAll(instructionLabel, userNameField, resetButton, resultLabel);
        return resetSection;
    }
}