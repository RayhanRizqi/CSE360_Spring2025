package application;

/**
 * The User class represents a user entity in the system.
 * It contains the user's details such as userName, password, and role.
 */
public class User {
    private String userName;
    private String password;
    private String[] roles; // changed to roles

    // Constructor to initialize a new User object with userName, password, and role.
    public User( String userName, String password, String[] roles) { //changed to roles
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }
    
    // Sets the role of the user.
    public void setRole(String[] roles) {
    	this.roles=roles;
    }

    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getRole() { return roles; }
}
