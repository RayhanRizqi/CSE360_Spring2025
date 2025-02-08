package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.sql.SQLException;
import databasePart1.DatabaseHelper;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

/**
 * AdminPage class represents the user interface for the admin user.
 * This page displays a simple welcome message for the admin.
 */

public class DisplayUsersPage {
	
	/**
     * Displays the admin page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */
	
	private DatabaseHelper databaseHelper;
	
	public DisplayUsersPage() {
		databaseHelper = new DatabaseHelper();
		try {
			databaseHelper.connectToDatabase();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
    public void show(Stage primaryStage) {
	    // Gets an array of Users
	    ArrayList<User> users = databaseHelper.getAllUsers();
	    
	    // Makes the table
	    TableView tableView = new TableView();
	    
	    // Adding all the columns in the table
	    TableColumn<User, String> column1 = new TableColumn<>("Username");
	    TableColumn<User, String> column2 = new TableColumn<>("Name");
	    TableColumn<User, String> column3 = new TableColumn<>("Email");
	    TableColumn<User, String> column4 = new TableColumn<>("Role(s)");
	    
	    // Setting the attribute of user the column will be
	    column1.setCellValueFactory( new PropertyValueFactory<>("username"));
	    column2.setCellValueFactory( new PropertyValueFactory<>("name"));
	    column3.setCellValueFactory( new PropertyValueFactory<>("email"));
	    column4.setCellValueFactory( new PropertyValueFactory<>("roles"));
	    
	    for (User user: users) {
	    	tableView.getItems().add(user);
	    }
	    
	    // label for when there are no users in the system
	    Label emptyLabel = new Label("There are no users in the system"); 
	    emptyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    VBox layout = new VBox();
	    
	    if (users.isEmpty())
	    {
	    	layout = new VBox(emptyLabel);
	    }
	    else
	    {
	    	layout = new VBox(tableView);
	    }
    	
    	layout.setAlignment(Pos.CENTER);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    Scene usersControlCenterScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(usersControlCenterScene);
	    primaryStage.setTitle("Users Control Center");
    }
}