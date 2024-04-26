package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;

public class MessageBoardUI {

    public static void create(ObservableValue<List<MessageBoard.Message>> messages,
                              ObjectProperty<Set<Integer>> tileIds) {
        ListView<MessageBoard.Message> messageListView = new ListView<>();

        VBox root = new VBox(messageListView);
        //todo modify size accordingly
        Scene scene = new Scene(root, 400, 300);

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();

        messages.addListener((observable, oldMessages ,newMessages) -> {
            ObservableList<MessageBoard.Message> observableMessages = FXCollections.observableArrayList(newMessages);
            messageListView.setItems(observableMessages);
        });
    }
}
