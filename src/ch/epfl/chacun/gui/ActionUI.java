package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ActionUI {

    //todo handler is WIP name for argument
    public Node create(ObservableValue<List<String>> base32Codes, Consumer<String> handler) {
        HBox root = new HBox();

        root.getStylesheets().add("board.css");
        root.setId("board");

        // Nouveau text contenant les codes en base 32 correspondants à châque action éffectué lors de la partie
        Text lastactionsText = new Text();
        lastactionsText.textProperty().bind(Bindings.createStringBinding(() ->
            formatBase32Codes(base32Codes.getValue()), base32Codes));


        TextField actionField = new TextField();
        actionField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().toUpperCase());
            if (change.toString().chars().allMatch(c -> Base32.isValid(String.valueOf(c)))) {
                return change;
            }
            return null;
        }));

        root.getChildren().add(lastactionsText);

        return root;
    }

    private String formatBase32Codes(List<String> codes) {
        StringBuilder codesBuilder = new StringBuilder();
        int startIndex = Math.max(0, codes.size() - 4);
        for (int i = startIndex; i < codes.size(); i++) {
            codesBuilder.append(i + 1).append(": ").append(codes.get(i));
            if (i < codes.size() - 1) {
                codesBuilder.append(", ");
            }
        }
        return  codesBuilder.toString();
    }
}
