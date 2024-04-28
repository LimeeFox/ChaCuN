package ch.epfl.chacun;

import ch.epfl.chacun.gui.MessageBoardUI;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageBoardDisplayApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        SimpleObjectProperty<List<MessageBoard.Message>> observableMessages = new SimpleObjectProperty<>(new ArrayList<>());


        SimpleObjectProperty<Set<Integer>> highlightedTiles = new SimpleObjectProperty<>(new HashSet<>());


        Node messageBoard = MessageBoardUI.create(observableMessages, highlightedTiles);


        Button addMessageButton = new Button("Add Message");
        addMessageButton.setOnAction(event -> {
            List<MessageBoard.Message> currentMessages = new ArrayList<>(observableMessages.get());

            int nextId = currentMessages.size() + 1;
            Set<Integer> tileIds = new HashSet<>();
            tileIds.add(nextId);

            MessageBoard.Message newMessage = new MessageBoard.Message("Message " + nextId,0,Set.of(PlayerColor.RED) ,tileIds);
            currentMessages.add(newMessage);
            observableMessages.set(currentMessages);
        });


        BorderPane root = new BorderPane();
        root.setCenter(messageBoard);
        root.setBottom(addMessageButton);


        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Message Board Display Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }


}