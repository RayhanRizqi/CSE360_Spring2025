package databasePart1;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import application.User;
import application.Question;
import application.Answer;


/**
 * The DatabaseHelper class is responsible for managing the connection to the database,
 * performing operations such as user registration, login validation, and handling invitation codes.
 */
public class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/FoundationDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			// You can use this command to clear the database and restart from fresh.
//			statement.execute("DROP ALL OBJECTS");

			createTables();  // Create the necessary tables if they don't exist
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "userName VARCHAR(255) UNIQUE, "
				+ "name VARCHAR(255), "
				+ "email VARCHAR(255), "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(255), "
				+ "oneTimePassword VARCHAR(255))";
		statement.execute(userTable);
		
		String questionTable = "CREATE TABLE IF NOT EXISTS questions ("
				+ "id INT, "
				+ "text VARCHAR(255), "
				+ "username VARCHAR(255), "
				+ "timestamp VARCHAR(255), "
				+ "answers VARCHAR(255), "
				+ "status VARCHAR(255))";
		statement.execute(questionTable);
		
		String answerTable = "CREATE TABLE IF NOT EXISTS answers ("
				+ "id INT, "
				+ "questionID INT, "
				+ "text VARCHAR(255), "
				+ "username VARCHAR(255), "
				+ "timestamp VARCHAR(255), "
				+ "subAnswers VARCHAR(255), "
				+ "votes INT)";
		statement.execute(answerTable);
		
		// Create the invitation codes table
	    String invitationCodesTable = "CREATE TABLE IF NOT EXISTS InvitationCodes ("
	            + "code VARCHAR(10) PRIMARY KEY, "
	            + "isUsed BOOLEAN DEFAULT FALSE)";
	    statement.execute(invitationCodesTable);
	}


	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	// Registers a new user in the database.
	public void register(User user) throws SQLException {
		String insertUser = "INSERT INTO cse360users (userName, password, role, name, email) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			String roles = user.getRoles().toString().replace("[", "").replace("]", "");
			System.out.println("Roles: " + roles);
			pstmt.setString(3, roles);
			pstmt.setString(4, user.getName());
			pstmt.setString(5, user.getEmail());
			pstmt.executeUpdate();
		}
	}

	// Validates a user's login credentials.
	public boolean login(User user) throws SQLException {
		String oneTimePasswordQuery = "SELECT * FROM cse360users WHERE userName = ? AND oneTimePassword = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(oneTimePasswordQuery)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return true;
				}
			}
		}
		
		String query = "SELECT * FROM cse360users WHERE userName = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.setString(2, user.getPassword());
			String roles = user.getRoles().toString().replace("[", "").replace("]", "");
			System.out.println(roles);
			pstmt.setString(3, roles);
			//pstmt.setString(3, user.getRole());
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	// Checks if a user already exists in the database based on their userName.
	public boolean doesUserExist(String userName) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}
	
	// Retrieves the role of a user from the database using their UserName.
	public ArrayList<String> getUserRole(String userName) {
	    ArrayList<String> roles = new ArrayList<>();
		String query = "SELECT role FROM cse360users WHERE userName = ?";
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	        	String rolesStr = rs.getString("role");
	            if (rolesStr != null && !rolesStr.isEmpty()) {
	            	for (String role: rolesStr.split(",")) {
	            		roles.add(role.trim());
	            	}
	            }
	        }
	        else
	        {
	        	return null;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return roles; // If no user exists or an error occurs
	}
	
	// Get user
	public User getUser(String username) {
		String query = "SELECT * FROM cse360users WHERE userName = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1,  username);
			ResultSet rs = pstmt.executeQuery();
		
			if (rs.next()) {
				String name = rs.getString("name");
				String password = "";
				String email = rs.getString("email");
				ArrayList<String> roles = new ArrayList<>(
						Arrays.asList(rs.getString("role").split(", "))
						);
				return new User(username, name, password, roles, email);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	// Generates a new invitation code and inserts it into the database.
	public String generateInvitationCode() {
	    String code = UUID.randomUUID().toString().substring(0, 4); // Generate a random 4-character code
	    String query = "INSERT INTO InvitationCodes (code) VALUES (?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return code;
	}
	
	// Validates an invitation code to check if it is unused.
	public boolean validateInvitationCode(String code) {
	    String query = "SELECT * FROM InvitationCodes WHERE code = ? AND isUsed = FALSE";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            // Mark the code as used
	            markInvitationCodeAsUsed(code);
	            return true;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	
	// Marks the invitation code as used in the database.
	private void markInvitationCodeAsUsed(String code) {
	    String query = "UPDATE InvitationCodes SET isUsed = TRUE WHERE code = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, code);
	        pstmt.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	// Gets all users in cse360
	public ArrayList<User> getAllUsers() {
		String query = "SELECT * FROM cse360users";
		ArrayList<User> users = new ArrayList<User>();
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			// Selects all the information about a user
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String username = rs.getString("userName");
				String name = rs.getString("name");
				String email = rs.getString("email");
				String roles = rs.getString("role");
				String password = "";
				
				users.add(new User(username, name, password, roles, email));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return users;	
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
	
	// add user roles
	public boolean addRole(String username, String role) throws SQLException {
		ArrayList<String> currentRoles = getUserRole(username);
		if (currentRoles.contains(role)) {
			return false;
		}
		
		currentRoles.add(role);
		return updateUserRoles(username, currentRoles);
	}
	
	// remove user roles
	public boolean removeRole(String username, String role) throws SQLException {
		if (role.contains("admin") && isLastAdmin(username)) {
			return false;
		}
		
		ArrayList<String> currentRoles = getUserRole(username);
		if (!currentRoles.contains(role)) {
			System.out.println("currentroles doesnt contain role");
			System.out.println(currentRoles.toString());
			return false;
		}
		
		currentRoles.remove(role);
		
		return updateUserRoles(username, currentRoles);
	}
	
	// Updates user roles
	private boolean updateUserRoles(String username, ArrayList<String> roles) throws SQLException {
		String query = "UPDATE cse360users SET role = ? WHERE userName = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			String rolesStr = String.join(",", roles);
			pstmt.setString(1, rolesStr);
			pstmt.setString(2, username);
			
			return pstmt.executeUpdate() > 0;
		}
	}

	// Closes the database connection and statement.
	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
	
	public String getPassword(String username) {
		String query = "SELECT password FROM cse360users WHERE username = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, username);
			
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString("password");
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
		
		return null;
	}
	
	// Reset Function for One Time Password
	public void setOneTimePassword(String userName, String tempPassword) throws SQLException {
		String query = "UPDATE cse360users SET oneTimePassword = ? WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, tempPassword);
			pstmt.setString(2, userName);
			pstmt.executeUpdate();
		}
		
		query = "UPDATE cse360users SET password = ? WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, "");
			pstmt.setString(2, userName);
		}
	}
	
	// Checks if oneTimePassword is valid
	public boolean isOneTimePasswordValid(String userName, String password) throws SQLException {
	    String query = "SELECT oneTimePassword FROM cse360users WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            String storedTempPass = rs.getString("oneTimePassword");
	            return storedTempPass != null && storedTempPass.equals(password);
	        }
	    }
	    return false;
	}
	
	public void clearOneTimePassword(String userName) throws SQLException {
		String query = "UPDATE cse360users SET oneTimePassword = NULL WHERE userName = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, userName);
	        pstmt.executeUpdate();
	    }
	}
	
	public void updatePassword(String userName, String newPassword) throws SQLException {
	    String query = "UPDATE cse360users SET password = ? WHERE userName = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, newPassword);
	        pstmt.setString(2, userName);
	        pstmt.executeUpdate();
	    }
	}
	
	public void removeUser(User user) throws SQLException {
		String query = "DELETE FROM cse360users WHERE username = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, user.getUserName());
			pstmt.execute();
		}
	}

	public void addQuestion(Question question) {
		String query = "INSERT INTO questions (id, text, username, timestamp, answers, status) VALUES (?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, question.getId());
			pstmt.setString(2, question.getText());
			pstmt.setString(3, question.getUser());
			pstmt.setString(4, question.getTimestamp().toString());
			pstmt.setString(5, question.getAnswers().toString().replace("[", "").replace("]", ""));
			pstmt.setString(6, question.getStatus());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addAnswer(Answer answer)  {
		String query = "INSERT INTO answers (id, questionID, text, username, timestamp, subAnswers, votes) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, answer.getId());
			pstmt.setInt(2, answer.getQuestionID());
			pstmt.setString(3, answer.getText());
			pstmt.setString(4, answer.getUser());
			pstmt.setString(5, answer.getTimestamp().toString());
			pstmt.setString(6, answer.getSubAnswers().toString().replace("[", "").replace("]", ""));
			pstmt.setInt(7, 0);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Answer getAnswer(int id) {
		String query = "SELECT * FROM answers WHERE id = ?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				System.out.println("ResultSet: " + rs.getInt("id"));
				int questionID = rs.getInt("questionID");
				String text = rs.getString("text");
				String username = rs.getString("username");
				LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"));
				ArrayList<Integer> subAnswers = new ArrayList<>(Arrays.stream(rs.getString("subAnswers").split(","))
						.map(String::trim)
						.map(Integer::parseInt)
						.collect(Collectors.toList()));
				int votes = rs.getInt("votes");
				return new Answer(id, questionID, text, username, timestamp, subAnswers, votes);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<Question> getQuestions() {
		String query = "SELECT * FROM questions";

		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			ResultSet rs = pstmt.executeQuery();
			ArrayList<Question> questions = new ArrayList<>();
			
			while (rs.next()) {
				int id = rs.getInt("id");
				String text = rs.getString("text");
				String username = rs.getString("username");
				LocalDateTime timestamp = LocalDateTime.parse(rs.getString("timestamp"));
				
				ArrayList<Integer> answers = new ArrayList<>();
				String answersText = rs.getString("answers");
				
				if (!answersText.isEmpty()) {
					for (String answerID: rs.getString("answers").split(", ")) {
						answers.add(Integer.parseInt(answerID));
					}
				}
				
				String status = rs.getString("status");
				questions.add(new Question(id, text, username, timestamp, answers, status));
			}
			return questions;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}