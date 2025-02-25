package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import databasePart1.DatabaseHelper;
import java.sql.SQLException;
import java.util.List;

public class AnswerManagementPage {
    private Answers answersModel;
    private TableView<Answer> tableView;
    private ObservableList<Answer> answerData;
    
    public AnswerManagementPage() {
        answerData = FXCollections.observableArrayList();
    }
    
    public void show(Stage primaryStage, DatabaseHelper databaseHelper, User currentUser) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        
        try {
            answersModel = new Answers(databaseHelper.getConnection());
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        
        tableView = new TableView<>();
        tableView.setPrefHeight(300);
        
        TableColumn<Answer, Number> idCol = new TableColumn<>("Answer ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getAnswerID()));
        
        TableColumn<Answer, Number> questionIdCol = new TableColumn<>("Question ID");
        questionIdCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getQuestionID()));
        
        TableColumn<Answer, String> answerTextCol = new TableColumn<>("Answer Text");
        answerTextCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAnswerText()));
        
        TableColumn<Answer, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAuthor()));
        
        TableColumn<Answer, String> acceptedCol = new TableColumn<>("Accepted");
        acceptedCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isAccepted() ? "Yes" : "No"));
        
        tableView.getColumns().addAll(idCol, questionIdCol, answerTextCol, authorCol, acceptedCol);
        tableView.setItems(answerData);
        
        TextField questionIdField = new TextField();
        questionIdField.setPromptText("Question ID");
        
        TextArea answerTextArea = new TextArea();
        answerTextArea.setPromptText("Answer Text");
        
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        
        HBox formBox = new HBox(10,
            new Label("Question ID:"), questionIdField,
            new Label("Answer:"), answerTextArea,
            new Label("Author:"), authorField
        );
        
        Button addButton = new Button("Add Answer");
        Button updateButton = new Button("Update Selected");
        Button deleteButton = new Button("Delete Selected");
        Button acceptButton = new Button("Accept Selected");
        Button searchButton = new Button("Search by Question ID");
        TextField searchField = new TextField();
        searchField.setPromptText("Question ID");
        
        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton, acceptButton, searchField, searchButton);
        
      
        addButton.setOnAction(e -> {
            String qIdStr = questionIdField.getText();
            String answerText = answerTextArea.getText();
            String author = authorField.getText();
            int qId;
            try {
                qId = Integer.parseInt(qIdStr);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid Question ID");
                return;
            }
            if (answerText.trim().isEmpty()) {
                showAlert("Error", "Answer text cannot be empty.");
                return;
            }
            Answer newAnswer = new Answer(0, qId, answerText, author);
            try {
                if (answersModel.addAnswer(newAnswer)) {
                    answerData.add(newAnswer);
                    clearFields(questionIdField, answerTextArea, authorField);
                } else {
                    showAlert("Error", "Failed to add answer.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error while adding answer.");
            }
        });
        
        updateButton.setOnAction(e -> {
            Answer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "No answer selected.");
                return;
            }
            String newAnswerText = answerTextArea.getText();
            if (newAnswerText.trim().isEmpty()) {
                showAlert("Error", "Answer text cannot be empty.");
                return;
            }
            selected.setAnswerText(newAnswerText);
            try {
                if (answersModel.updateAnswer(selected)) {
                    tableView.refresh();
                    clearFields(questionIdField, answerTextArea, authorField);
                } else {
                    showAlert("Error", "Failed to update answer.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error while updating answer.");
            }
        });
        
        deleteButton.setOnAction(e -> {
            Answer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "No answer selected.");
                return;
            }
            try {
                if (answersModel.deleteAnswer(selected.getAnswerID())) {
                    answerData.remove(selected);
                } else {
                    showAlert("Error", "Failed to delete answer.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error while deleting answer.");
            }
        });
        
        acceptButton.setOnAction(e -> {
            Answer selected = tableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Error", "No answer selected.");
                return;
            }
            try {
                if (answersModel.acceptAnswer(selected.getAnswerID())) {
                    selected.setAccepted(true);
                    tableView.refresh();
                } else {
                    showAlert("Error", "Failed to accept answer.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error while accepting answer.");
            }
        });
        
        searchButton.setOnAction(e -> {
            String searchText = searchField.getText();
            int searchQId;
            try {
                searchQId = Integer.parseInt(searchText);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid Question ID for search.");
                return;
            }
            try {
                List<Answer> results = answersModel.getAnswersForQuestion(searchQId);
                tableView.setItems(FXCollections.observableArrayList(results));
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert("Error", "Database error while searching answers.");
            }
        });
        
        Button backButton = new Button("Back to Student Menu");
        backButton.setOnAction(e -> {
            new StudentQAPage(databaseHelper, currentUser).show(primaryStage);
        });
        
        root.getChildren().addAll(formBox, buttonBox, tableView, backButton);
        
        Scene scene = new Scene(root, 900, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Answer Management");
        primaryStage.show();
    }
    
    private void clearFields(TextField questionIdField, TextArea answerTextArea, TextField authorField) {
        questionIdField.clear();
        answerTextArea.clear();
        authorField.clear();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
