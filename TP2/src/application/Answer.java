package application;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 	The Answer entity represents an answer entity in the system
 	It contains the questionID, answerID, text, list of subanswers, and userID
 **/

public class Answer {
	private static int idCounter = 1;
	private int id;
	private int questionID;
	private String text;
	private String username;
	private LocalDateTime timestamp;
	private ArrayList<Integer> subAnswers;
	private int votes;
	
	public Answer(int questionId, String text, String username) {
		this.id = idCounter++;
		this.questionID = questionID;
		this.text = text;
		this.username = username;
		this.timestamp = LocalDateTime.now();
		this.subAnswers = new ArrayList<>();
		this.votes = 0;
	}
	
	public Answer(int id, int questionId, String text, String username, LocalDateTime timestamp, ArrayList<Integer> subAnswers, int votes) {
		this.id = id;
		this.questionID = questionID;
		this.text = text;
		this.username = username;
		this.timestamp = timestamp;
		this.subAnswers = subAnswers;
		this.votes = votes;
	}
	
	public String toString() {
		return String.valueOf(id);
	}
	
	public int getId() {
		return id;
	}
	
	public int getQuestionID() {
		return questionID;
	}
	
	public String getText() {
		return text;
	}
	
	public String getUser() {
		return username;
	}
	
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public ArrayList<Integer> getSubAnswers() {
		return subAnswers;
	}
	
	public int getVotes() {
		return votes;
	}
}