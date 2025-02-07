package application;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import databasePart1.DatabaseHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRoleManagementPage extends VBox {
    private DatabaseHelper dbHelper;
    private TableView<User> userTable;
    private User currentAdmin;
    private final String[] AVAILABLE_ROLES = {"admin", "student", "instructor", "staff", "reviewer"}; //we change/edit more roles here.

    public UserRoleManagementPage(User admin) {
        this.currentAdmin = admin;
        this.dbHelper = new DatabaseHelper();
        
        // Basic styling
        setPadding(new Insets(20));
        setSpacing(15);
        getStyleClass().add("role-management-page");
        
        setupUI();
    }

    private void setupUI() {
        //header
        Label headerLabel = new Label("User Role Management");
        headerLabel.getStyleClass().add("page-header");

        //creating table
        userTable = new TableView<>();
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //creating columns
        TableColumn<User, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> data.getValue().usernameProperty());

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<User, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(data -> data.getValue().emailProperty());

        TableColumn<User, String> rolesCol = new TableColumn<>("Current Roles");
        rolesCol.setCellValueFactory(data -> data.getValue().rolesProperty());

        //role management column here
        TableColumn<User, Void> actionCol = new TableColumn<>("Manage Roles");
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button manageButton = new Button("Modify Roles");

            {
                manageButton.getStyleClass().add("manage-button");
                manageButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    showRoleManagementDialog(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    manageButton.setDisable(user.getUsername().equals(currentAdmin.getUsername()));
                    setGraphic(manageButton);
                }
            }
        });

        userTable.getColumns().addAll(usernameCol, nameCol, emailCol, rolesCol, actionCol);

        //buttons (control)

        //refresh
        HBox buttonBar = new HBox(10);
        Button refreshButton = new Button("Refresh List");
        refreshButton.setOnAction(e -> refreshUserList());
        //back
        Button backButton = new Button("Back to Admin Home");
        backButton.setOnAction(e -> goBackToAdminHome());

        buttonBar.getChildren().addAll(refreshButton, backButton);

        //adding all the components to the page.
        getChildren().addAll(headerLabel, userTable, buttonBar);
        
        //loading initial data..
        refreshUserList();
    }

    private void showRoleManagementDialog(User user) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Manage Roles - " + user.getUsername());
        dialog.setHeaderText("Current roles: " + String.join(", ", user.getRoles()));

        //dialog box
        VBox content = new VBox(10);
        content.setPadding(new Insets(20));

        //select role
        ComboBox<String> roleComboBox = new ComboBox<>(
            FXCollections.observableArrayList(AVAILABLE_ROLES)
        );
        roleComboBox.setPromptText("Select a role");

        //buttons (action)

        //add_Role btn
        Button addButton = new Button("Add Role");
        //remove_Role btn
        Button removeButton = new Button("Remove Role");

        addButton.setOnAction(e -> {
            String selectedRole = roleComboBox.getValue();
            if (selectedRole != null) {
                handleAddRole(user, selectedRole);
                dialog.setHeaderText("Current roles: " + String.join(", ", user.getRoles()));
            }
        });

        removeButton.setOnAction(e -> {
            String selectedRole = roleComboBox.getValue();
            if (selectedRole != null) {
                handleRemoveRole(user, selectedRole);
                dialog.setHeaderText("Current roles: " + String.join(", ", user.getRoles()));
            }
        });

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(addButton, removeButton);

        content.getChildren().addAll(
            new Label("Select Role:"),
            roleComboBox,
            buttonBox
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        dialog.showAndWait();
    }

    private void handleAddRole(User user, String role) {
        try {
            if (dbHelper.addRole(user.getUsername(), role)) {
                showAlert("Success", "Role added successfully", Alert.AlertType.INFORMATION);
                refreshUserList();
            } else {
                showAlert("Error", "Role already exists or cannot be added", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            showAlert("Error", "Database error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleRemoveRole(User user, String role) {
        try {
            if (role.equals("admin") && user.getRoles().size() == 1) {
                showAlert("Error", "Cannot remove the last admin role", Alert.AlertType.ERROR);
                return;
            }

            if (dbHelper.removeRole(user.getUsername(), role)) {
                showAlert("Success", "Role removed successfully", Alert.AlertType.INFORMATION);
                refreshUserList();
            } else {
                showAlert("Error", "Role cannot be removed", Alert.AlertType.ERROR);
            }
        } catch (SQLException ex) {
            showAlert("Error", "Database error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void refreshUserList() {
        try {
            ObservableList<User> users = dbHelper.getAllUsers();
            userTable.setItems(users);
        } catch (SQLException ex) {
            showAlert("Error", "Failed to load users: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void goBackToAdminHome() {
        /*creating a new home page for admin and seting it as the current stage
        (i dont know how to send the admin back to original admin hompage saving the changes, so this is kindof workaround lmk if you have a better solu.)*/
        AdminHomePage adminHome = new AdminHomePage(currentAdmin);
        getScene().setRoot(adminHome);
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}



//Database Helper fxns for UserRoleManagementPage (to change user roles as an admin)
//other helper fxns for database

/* check if these imports are already there, if not please impt these
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;
import java.util.List;
*/

private List<String> getUserRoles(String username) throws SQLException {
    List<String> roles = new ArrayList<>();
    String query = "SELECT role FROM cse360users WHERE userName = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();
        
        if (rs.next()) {
            String rolesStr = rs.getString("role");
            if (rolesStr != null && !rolesStr.isEmpty()) {
                for (String role : rolesStr.split(",")) {
                    roles.add(role.trim());
                }
            }
        }
    }
    return roles;
}

public boolean addRole(String username, String role) throws SQLException {
    List<String> currentRoles = getUserRoles(username);
    if (currentRoles.contains(role)) {
        return false;
    }
    currentRoles.add(role);
    return updateUserRoles(username, currentRoles);
}

public boolean removeRole(String username, String role) throws SQLException {
    if (role.equals("admin") && isLastAdmin(username)) {
        return false;
    }
    List<String> currentRoles = getUserRoles(username);
    if (!currentRoles.remove(role)) {
        return false;
    }
    return updateUserRoles(username, currentRoles);
}

private boolean updateUserRoles(String username, List<String> roles) throws SQLException {
    String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
    
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        String rolesStr = String.join(",", roles);
        pstmt.setString(1, rolesStr);
        pstmt.setString(2, username);
        
        return pstmt.executeUpdate() > 0;
    }
}

private boolean isLastAdmin(String username) throws SQLException {
    String query = "SELECT COUNT(*) FROM cse360users WHERE role LIKE '%admin%'";
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) <= 1;
        }
        return true;
    }
}

public ObservableList<User> getAllUsers() throws SQLException {
    ObservableList<User> users = FXCollections.observableArrayList();
    String query = "SELECT userName, role FROM cse360users";
    
    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            String userName = rs.getString("userName");
            String role = rs.getString("role");
            User user = new User(userName, "", role); //password empty -> security (will not display pswd when displaying user list)
            users.add(user);
        }
    }
    return users;
}

//adminHomepage

package application;

private User currentAdmin;

public AdminHomePage(User admin) {
    this.currentAdmin = admin;
    setupUI();
}

public class AdminHomePage extends VBox {

    private void setupUI() {
        //existing ui.

        //role management button -> UserRoleManagement
        Button roleManagementButton = new Button("Manage User Roles");
        roleManagementButton.getStyleClass().add("primary-button");
        roleManagementButton.setOnAction(e -> openRoleManagement());

        //button for the layout
        getChildren().add(roleManagementButton);
    }

    private void openRoleManagement() {
        UserRoleManagementPage rolePage = new UserRoleManagementPage(currentAdmin);
        getScene().setRoot(rolePage);
    }
}