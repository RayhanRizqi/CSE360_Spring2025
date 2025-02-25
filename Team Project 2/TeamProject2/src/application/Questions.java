package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.*;

public class Questions {
    private List<Question> questionList;
    private Connection connection;
    
    // Constructor that accepts a Connection so that persistence methods work.
    public Questions(Connection connection) {
        this.connection = connection;
        questionList = new ArrayList<>();
    }
    
    // Optional: default constructor (you then must set connection manually)
    public Questions() {
        questionList = new ArrayList<>();
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public boolean addQuestion(Question q) throws SQLException {
        String sql = "INSERT INTO questions (title, description, author) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, q.getTitle());
            pstmt.setString(2, q.getDescription());
            pstmt.setString(3, q.getAuthor());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    q.setQuestionID(generatedKeys.getInt(1));
                } else {
                    return false;
                }
            }
            return true;
        }
    }
    
    // Overloaded updateQuestion method to match the call in QuestionManagementPage
    public boolean updateQuestion(int id, String newTitle, String newDescription) throws SQLException {
        String sql = "UPDATE questions SET title = ?, description = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newDescription);
            pstmt.setInt(3, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }
    
    // You can still keep an updateQuestion method that accepts a Question object if needed.
    
    public List<Question> getAllQuestions() throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String author = rs.getString("author");
                list.add(new Question(id, title, description, author));
            }
        }
        return list;
    }
    
    public boolean deleteQuestion(int id) throws SQLException {
        String sql = "DELETE FROM questions WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }
    
    // Updated searchQuestions method to query the database
    public List<Question> searchQuestions(String keyword) throws SQLException {
        List<Question> list = new ArrayList<>();
        String sql = "SELECT * FROM questions WHERE lower(title) LIKE ? OR lower(description) LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String search = "%" + keyword.toLowerCase() + "%";
            pstmt.setString(1, search);
            pstmt.setString(2, search);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()){
                    int id = rs.getInt("id");
                    String title = rs.getString("title");
                    String description = rs.getString("description");
                    String author = rs.getString("author");
                    list.add(new Question(id, title, description, author));
                }
            }
        }
        return list;
    }
}
