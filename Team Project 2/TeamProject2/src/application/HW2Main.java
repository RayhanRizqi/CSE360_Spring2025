package application;

import databasePart1.DatabaseHelper;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.ArrayList;

public class HW2Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a dummy DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper();
        try {
            dbHelper.connectToDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create a dummy User with the "student" role for testing
        ArrayList<String> roles = new ArrayList<>();
        roles.add("student");
        User dummyUser = new User("dummyUser", "Dummy User", "dummyPass", roles, "dummy@example.com");

        VBox root = new VBox(10);
        root.setStyle("-fx-alignment: center; -fx-padding: 20;");
        
        Button questionManagementButton = new Button("Manage Questions");
        questionManagementButton.setOnAction(e -> {
            new QuestionManagementPage().show(primaryStage, dbHelper, dummyUser);
        });
        
        Button answerManagementButton = new Button("Manage Answers");
        answerManagementButton.setOnAction(e -> {
            new AnswerManagementPage().show(primaryStage, dbHelper, dummyUser);
        });
        
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e -> {
            primaryStage.close();
        });
        
        root.getChildren().addAll(questionManagementButton, answerManagementButton, exitButton);
        
        Scene scene = new Scene(root, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("HW2 Q&A Management");
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
