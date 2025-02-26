package application;
import java.util.ArrayList;
import java.time.LocalDateTime;

/**
 	The Question entity represents a question entity in the system
 	It contains the questionID, text, list answers, and userID
 **/
 
public class Question {
	private static int idCounter = 1;
	private int id;
	private String text;
	private String username;
	private LocalDateTime timestamp;
	private ArrayList<Integer> answers;
	private String status;
	private int officialAnswerId;
	
	public Question(String text, String username) {
		this.id = idCounter++;
		this.text = text;
		this.username = username;
		this.timestamp = LocalDateTime.now();
		this.answers = new ArrayList<>();
		this.status = "unresolved";
	}
	
	public Question(int id, String text, String username, LocalDateTime timestamp, ArrayList<Integer> answers, String status) {
		this.id = id;
		this.text = text;
		this.username = username;
		this.timestamp = timestamp;
		this.answers = answers;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	
	public String getText() {
		return text;
	}
	
	public String getUser() {
		return username;
	}
	
	public ArrayList<Integer> getAnswers() {
		return answers;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void addAnswer(Answer answer) {
		answers.add(answer.getId());
	}
	
	public void resolveQuestion() {
		this.status = "resolved";
	}
	
	public void reopenQuestion() {
		this.status = "unresolved";
	}
}