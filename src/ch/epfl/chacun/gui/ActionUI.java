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
import java.util.function.Consumer;

/**
 *
 */
public class ActionUI {
    /**
     * Génère l'interface graphique permettant le jeu à distance grâce aux codes en base 32
     *
     * @param base32Codes
     *          valeur observable de la liste des chaînes de charactèrs des codes en base 32 correspondants aux actions
     *          effectuées lors de la partie, dans l'ordre chronologique
     * @param handler
     *          gestionnaire d'événement qui permet d'effectuer l'action correspondant à un code en base32 valid
     * @return l'interface graphique correspondant de jeu à distance, avec les codes en base 32 avec une boîte de texte
     *          pour y entrer des codes valides
     */
    public Node create(ObservableValue<List<String>> base32Codes, Consumer<String> handler) {
        HBox root = new HBox();

        root.getStylesheets().add("actions.css");
        root.setId("actions");

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

        actionField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handler.accept(actionField.getText());
                actionField.clear();
            }
        });

        root.getChildren().add(lastactionsText);

        return root;
    }

    /**
     * Méthode d'aide qui permet d'afficher les 4 derniers codes en base32 d'une liste donnée selon le format :
     *          "numéro d'apparition du code : code en base32"
     *
     * @param codes
     *          liste des codes en base32 que l'on souhaite formatter pour ensuite afficher
     * @return une chaîne de charactèrs contenant les 4 dernières positions de la liste chacune associée à son code en
     *          base32
     */
    private String formatBase32Codes(List<String> codes) {
        StringBuilder codesBuilder = new StringBuilder();
        int startIndex = Math.max(0, codes.size() - 4);
        // On affiche que (au plus) les 4 derniers codes
        for (int i = startIndex; i < codes.size(); i++) {
            codesBuilder.append(i + 1).append(": ").append(codes.get(i).toUpperCase());
            if (i < codes.size() - 1) {
                codesBuilder.append(", ");
            }
        }
        return  codesBuilder.toString();
    }
}
