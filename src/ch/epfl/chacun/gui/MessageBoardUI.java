package ch.epfl.chacun.gui;

import ch.epfl.chacun.MessageBoard;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javafx.application.Platform.runLater;

public class MessageBoardUI {

    public static Node create(ObservableValue<List<MessageBoard.Message>> messages,
                              ObjectProperty<Set<Integer>> tileIds) {

        VBox root = new VBox();

        ScrollPane scrollPane = new ScrollPane(root);
        //todo add path as string
        scrollPane.getStylesheets()
                .add("message-board.css");
        //todo add thing next to hashtag
        scrollPane.setId("message-board");

        messages.addListener((observable, oldMessages, newMessages) -> {
            for (MessageBoard.Message message : newMessages) {
                if (!oldMessages.contains(message)) {
                    Text text = new Text(message.text());
                    text.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                    //todo add children
                    root.getChildren().add(text);

                    // Set on mouse entered event
                    text.setOnMouseEntered(event -> {
                        tileIds.set(message.tileIds());
                    });

                    // Set on mouse exited event
                    text.setOnMouseExited(event -> {
                        tileIds.set(new HashSet<>());
                    });
                }
            }
            runLater(() -> scrollPane.setVvalue(1));
        });
        return scrollPane;
    }
}
