package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * This page displays a simple welcome message for the user.
 */
public class ViewQuestionsPage {

	private User user;
	private DatabaseHelper databaseHelper;
	
	public ViewQuestionsPage(User user, DatabaseHelper databaseHelper) {
		this.user = user;
		this.databaseHelper = databaseHelper;
    }
	
    public void show(Stage primaryStage) {
    	VBox layout = new VBox();
	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
	    
	    // Label to display Hello user
	    Label allQuestionsLabel = new Label("All Questions");
	    allQuestionsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
	    layout.getChildren().addAll(allQuestionsLabel);
	    
	    ArrayList<Question> questions = databaseHelper.getQuestions();
	    
	    for (Question question : questions) {
	    	HBox questionBox = new HBox();
	    	questionBox.setStyle("-fx-spacing: 10; -fx-padding: 10; -fx-alignment: center;");
	    	
	    	Label questionText = new Label("Question: " + question.getText());
	    	Label usernameLabel = new Label("User: " + question.getUser());
	    	Label timestampLabel = new Label("Posted on: " + question.getTimestamp().toString());
	    	
	    	questionBox.setOnMouseClicked((MouseEvent event) -> {
	    		new ViewQuestionPage(user, databaseHelper, question).show(primaryStage);
	    	});
	    	
	    	questionBox.getChildren().addAll(questionText, usernameLabel, timestampLabel);
	    	layout.getChildren().add(questionBox);
	    }
	    
	    Scene userScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(userScene);
	    primaryStage.setTitle("User Page");
    	
    }
}