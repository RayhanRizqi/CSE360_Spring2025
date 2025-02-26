package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This page displays a simple welcome message for the user.
 */
public class ViewQuestionPage {

	private User user;
	private DatabaseHelper databaseHelper;
	private Question question;
	
	public ViewQuestionPage(User user, DatabaseHelper databaseHelper, Question question) {
		this.user = user;
		this.databaseHelper = databaseHelper;
		this.question = question;
    }
	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Display the question
        Label questionTextLabel = new Label("Question: " + question.getText());
        questionTextLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        Label usernameLabel = new Label("Asked by: " + question.getUser());
        Label timestampLabel = new Label("Posted on: " + question.getTimestamp().toString());
        
        layout.getChildren().addAll(questionTextLabel, usernameLabel, timestampLabel);

        // Display answers
        Label answersLabel = new Label("Answers:");
        layout.getChildren().add(answersLabel);
        
        ArrayList<Integer> answerIds = question.getAnswers();
        ArrayList<Answer> answers = new ArrayList<>();
        
        for (int answerId : answerIds) {
        	System.out.println("answerId: " + answerId);
        	answers.add(databaseHelper.getAnswer(answerId));
        }
        
        for (Answer answer : answers) {
            Label answerLabel = new Label(answer.getText() + " - by " + answer.getUser());
            layout.getChildren().add(answerLabel);
        }
	    
        // TextArea for new answer input
        TextArea answerInput = new TextArea();
        answerInput.setPromptText("Write your answer here...");
        answerInput.setMaxWidth(250);
        answerInput.setMaxHeight(10);

        Button submitButton = new Button("Submit Answer");
        submitButton.setOnAction(e -> {
            String answerText = answerInput.getText();
            if (!answerText.isEmpty()) {
                Answer newAnswer = new Answer(question.getId(), user.getUserName(), answerText);
                question.addAnswer(newAnswer);
                databaseHelper.addAnswer(newAnswer);
                show(primaryStage); // Refresh the page
            }
        });

        layout.getChildren().addAll(answerInput, submitButton);
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}