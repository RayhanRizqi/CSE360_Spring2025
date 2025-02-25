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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

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
	private TableView tableView = new TableView();
	
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
	    
	    for (User user: users) {
	    	System.out.println(user.toString());
	    }
	    
	    // Adding all the columns in the table
	    TableColumn<User, String> column1 = new TableColumn<>("UserName");
	    TableColumn<User, String> column2 = new TableColumn<>("Name");
	    TableColumn<User, String> column3 = new TableColumn<>("Email");
	    TableColumn<User, String> column4 = new TableColumn<>("Role(s)");
	    
	    // Setting the attribute of user the column will be
	    column1.setCellValueFactory( data -> new SimpleStringProperty(data.getValue().getUserName()));
	    column2.setCellValueFactory( data -> new SimpleStringProperty(data.getValue().getName()));
	    column3.setCellValueFactory( data -> new SimpleStringProperty(data.getValue().getEmail()));
	    column4.setCellValueFactory( data -> new SimpleStringProperty(data.getValue().getRoles().toString().replace("[", "").replace("]", "")));
	    
	    // Add columns to tableview
	    tableView.getColumns().add(column1);
	    tableView.getColumns().add(column2);
	    tableView.getColumns().add(column3);
	    tableView.getColumns().add(column4);
	    
	    TableColumn<User, Void> removeCol = new TableColumn<>("Remove");
	    removeCol.setCellFactory(param -> new TableCell<>() {
	    	private final Button removeButton = new Button("Remove User");
	    	
	    	{
	    		removeButton.getStyleClass().add("remove-button");
	    		removeButton.setOnAction(event -> {
	    			User user = getTableView().getItems().get(getIndex());
	    			removeUser(user);
	    		});
	    	}
	    	
	    	@Override
	    	protected void updateItem(Void item, boolean empty) {
	    		super.updateItem(item, empty);
	    		if (empty) {
	    			setGraphic(null);
	    		} else {
	    			User user = getTableView().getItems().get(getIndex());
	    			setGraphic(removeButton);
	    		}
	    	}
	    });
	    
	    tableView.getColumns().add(removeCol);
	    
	    for (User user: users) {
	    	tableView.getItems().add(user);
	    }
	    
	    // label for when there are no users in the system
	    Label emptyLabel = new Label("There are no users in the system"); 
	    emptyLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    
	    // refresh
	    Button refreshButton = new Button("Refresh List");
	    refreshButton.setOnAction(e -> refreshUserList());
	    
	    VBox layout = new VBox();
	    
	    if (users.isEmpty())
	    {
	    	layout = new VBox(emptyLabel);
	    }
	    else
	    {
	    	layout = new VBox(tableView, refreshButton);
	    }
    	
    	layout.setAlignment(Pos.CENTER);
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    Scene usersControlCenterScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(usersControlCenterScene);
	    primaryStage.setTitle("Users Control Center");
    }
    
    private void removeUser(User user) {
    	try {
    		databaseHelper.removeUser(user);
    	} catch (SQLException ex) {
    		ex.printStackTrace();
    	}
    }
    
    private void refreshUserList() {
    	ArrayList<User> users = databaseHelper.getAllUsers();
    	tableView.setItems(FXCollections.observableArrayList(users));
    }
}