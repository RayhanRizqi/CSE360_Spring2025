package application;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class Answers {
    private List<Answer> answerList;
    private Connection connection;
    
    public Answers(Connection connection) {
        this.connection = connection;
        answerList = new ArrayList<>();
    }
    
    public Answers() {
        answerList = new ArrayList<>();
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public boolean addAnswer(Answer a) throws SQLException {
        String sql = "INSERT INTO answers (questionId, answerText, author, accepted) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, a.getQuestionID());
            pstmt.setString(2, a.getAnswerText());
            pstmt.setString(3, a.getAuthor());
            pstmt.setBoolean(4, a.isAccepted());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    a.setAnswerID(generatedKeys.getInt(1));
                } else {
                    return false;
                }
            }
            return true;
        }
    }
    
    public List<Answer> getAnswersForQuestion(int questionID) throws SQLException {
        List<Answer> list = new ArrayList<>();
        String sql = "SELECT * FROM answers WHERE questionId = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questionID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) {
                    int id = rs.getInt("id");
                    String answerText = rs.getString("answerText");
                    String author = rs.getString("author");
                    boolean accepted = rs.getBoolean("accepted");
                    Answer answer = new Answer(id, questionID, answerText, author);
                    answer.setAccepted(accepted);
                    list.add(answer);
                }
            }
        }
        return list;
    }
    
    public boolean updateAnswer(Answer a) throws SQLException {
        String sql = "UPDATE answers SET answerText = ?, accepted = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, a.getAnswerText());
            pstmt.setBoolean(2, a.isAccepted());
            pstmt.setInt(3, a.getAnswerID());
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }
    
    public boolean deleteAnswer(int id) throws SQLException {
        String sql = "DELETE FROM answers WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }
    
    public boolean acceptAnswer(int answerID) throws SQLException {
        String sql = "UPDATE answers SET accepted = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setBoolean(1, true);
            pstmt.setInt(2, answerID);
            int affected = pstmt.executeUpdate();
            return affected > 0;
        }
    }
}
