package ch.epfl.chacun;

import ch.epfl.chacun.gui.ActionUI;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.List;

public class ActionApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SimpleObjectProperty<List<String>> actionList = new SimpleObjectProperty<>(List.of());
        BorderPane root = new BorderPane();

        // Utilisation de la méthode create de ActionUI
        root.setCenter(ActionUI.create(actionList, action -> {
            System.out.println(STR."Action reçue: \{action}");
            List<String> actionListString = new ArrayList<>(actionList.getValue());
            actionListString.add(action);
            actionList.set(actionListString);
        }));

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Action Interface");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}