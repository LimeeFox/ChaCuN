package ch.epfl.chacun.gui;

import ch.epfl.chacun.Base32;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Consumer;

public class ActionUI {
    /**
     * todo add javadoc pls
     *
     * @param base32Codes
     * @param handler
     * @return
     */
    public static Node create(ObservableValue<List<String>> base32Codes, Consumer<String> handler) {
        HBox root = new HBox();

        root.getStylesheets().add("actions.css");
        root.setId("actions");

        // Nouveau text contenant les codes en base 32 correspondants à châque action éffectué lors de la partie
        Text lastActionsText = new Text();
        lastActionsText.textProperty().bind(base32Codes.map((ActionUI::formatBase32Codes)));


        TextField actionField = new TextField();
        actionField.setTextFormatter(new TextFormatter<>(change -> {
            change.setText(change.getText().toUpperCase());
            return  change.getControlNewText().matches(STR."[\{Base32.ALPHABET}]*") ? change : null;
        }));

        actionField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handler.accept(actionField.getText());
                actionField.clear();
            }
        });

        root.getChildren().add(lastActionsText);
        root.getChildren().add(actionField);

        return root;
    }

    /**
     * Méthode d'aide qui permet d'afficher les 4 derniers codes en base32 d'une liste donnée selon le format:
     *          "numéro d'apparition du code : code en base32"
     *
     * @param codes
     *          liste des codes en base32 que l'on souhaite formatter pour ensuite afficher
     * @return une chaîne de charactèrs contenant les 4 dernières positions de la liste chacune associée à son code en
     *          base32
     */
    private static String formatBase32Codes(List<String> codes) {
        System.out.println("formatting...");
        StringBuilder codesBuilder = new StringBuilder();
        int startIndex = Math.max(0, codes.size() - 4);
        // On affiche que (au plus) les 4 derniers codes
        for (int i = startIndex; i < codes.size(); i++) {
            codesBuilder.append(i + 1).append(": ").append(codes.get(i).toUpperCase());
            if (i < codes.size() - 1) {
                codesBuilder.append(", ");
            }
        }
        System.out.println("ended");
        return codesBuilder.toString();
    }
}
