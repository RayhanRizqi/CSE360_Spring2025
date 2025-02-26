//This user home page has the logout button feature and also the switch role feature. 
//The switch role feature is not working now since it only redirects it back to the welcome page
//We need it to redirect to to a new page where all the roles are but for now it is there as a place holder

package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.util.ArrayList;

/**
 * This page displays a simple welcome message for the user.
 */
public class UserLogout {
    private final DatabaseHelper databaseHelper;
    private final String userName;
    
    public UserLogout(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }
    
    public void show(Stage primaryStage) {
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Label to display Hello user
        Label userLabel = new Label("Hello, " + userName + "!");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Button for switching roles
        Button switchRoleButton = new Button("Switch Role");
        switchRoleButton.setOnAction(a -> {
            try {
                // Place holder for switch role
                ArrayList<String> roles = databaseHelper.getUserRole(userName);
            	User user = new User(userName, "", "", roles, "");
                RoleSelectionPage roleSelection = new RoleSelectionPage();
                roleSelection.show(databaseHelper, primaryStage, user);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        // Button for logging out
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(a -> {
            UserLoginPage loginPage = new UserLoginPage(databaseHelper);
            loginPage.show(primaryStage);
        });
        
        layout.getChildren().addAll(userLabel, switchRoleButton, logoutButton);
        Scene userScene = new Scene(layout, 800, 400);
        
        // Set the scene to primary stage
        primaryStage.setScene(userScene);
        primaryStage.setTitle("User Page");
    }
}