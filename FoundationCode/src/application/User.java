package application;
import java.util.ArrayList;
import java.util.List;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String name; // added name 
    private String password;
    private ArrayList<String> roles; // changed to roles
    private String email; // added email

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String name, String password, ArrayList<String> roles, String email) { //changed to roles, added name and email
        this.userName = userName;
        this.name = name; // added name 
        this.password = password;
        this.roles = roles;
        this.email = email; // added email
    }

    public User( String userName, String name, String password, String singleRole, String email) { //changed to roles, added name and email
        this.userName = userName;
        this.name = name; // added name 
        this.password = password;
        this.roles = new ArrayList<>();
        this.roles.add(singleRole);
        this.email = email; // added email
    }
    
    @Override
    public String toString() {
    	return "Role: " + this.roles.toString();
    }
    
    // Sets the role of the user.
    public void setRole(ArrayList<String> roles) {
    	this.roles= roles;
    }

    public String getUserName() { return userName; }
    public String getName() { return name; } // added name 
    public String getPassword() { return password; }
    public ArrayList<String> getRoles() { return roles; }
    public String getEmail() { return email; } // added email
}