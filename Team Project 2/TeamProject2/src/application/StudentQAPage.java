package application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;

/**
 * The StudentQAPage class represents the HW2 Q&A interface for students.
 * It allows students to navigate to either the question management or answer management screens,
 * and includes a back button to return to the welcome page.
 */
public class StudentQAPage {

    private DatabaseHelper databaseHelper;
    private User currentUser;

    // Constructor takes the database helper and current user for proper navigation.
    public StudentQAPage(DatabaseHelper databaseHelper, User currentUser) {
        this.databaseHelper = databaseHelper;
        this.currentUser = currentUser;
    }

    public void show(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        // Button to navigate to the Question Management Page
        Button manageQuestionsButton = new Button("Manage Questions");
        manageQuestionsButton.setOnAction(e -> {
            // Updated: Passing databaseHelper and currentUser to the show method.
            new QuestionManagementPage().show(primaryStage, databaseHelper, currentUser);
        });
        
        // Button to navigate to the Answer Management Page
        Button manageAnswersButton = new Button("Manage Answers");
        manageAnswersButton.setOnAction(e -> {
            // Updated: Passing databaseHelper and currentUser to the show method.
            new AnswerManagementPage().show(primaryStage, databaseHelper, currentUser);
        });
        
        // Back button to return to WelcomeLoginPage
        Button backButton = new Button("Back to Welcome");
        backButton.setOnAction(e -> {
            new WelcomeLoginPage(databaseHelper).show(primaryStage, currentUser);
        });
        
        root.getChildren().addAll(manageQuestionsButton, manageAnswersButton, backButton);
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Q&A Management");
        primaryStage.show();
    }
}
