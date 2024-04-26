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

/**
 * Gestion de l'interface graphique du tableau d'affichage de messages
 */
public class MessageBoardUI {

    /**
     * Création et mis à jour de l'affichage du tableau d'affichage de messages du jeu
     *
     * @param messages
     *          messages à faire afficher sur notre tableau d'affichage
     * @param tileIds
     *          identifiant des tuiles concernées par les messages (mis à jour ultérieurement)
     * @return un tableau d'affichage de messages défilable et mis à jour
     */
    public static Node create(ObservableValue<List<MessageBoard.Message>> messages,
                              ObjectProperty<Set<Integer>> tileIds) {

        // Initialisation de l'espace d'affichage de message
        VBox root = new VBox();

        // Initialisation du tableau défilable
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.getStylesheets().add("message-board.css");
        scrollPane.setId("message-board");

        // Mise en place de l'auditeur pour notre tableau d'affichage
        messages.addListener((observable, oldMessages, newMessages) -> {
            // Itération à travers les nouveaux messages
            for (MessageBoard.Message message : newMessages) {
                // On vérifie que le nouveau message concerné n'est pas déjà présent dans les anciens messages
                if (!oldMessages.contains(message)) {
                    // Initialisation d'un nouveau text, à la taille spécifié (256)
                    Text text = new Text(message.text());
                    text.setWrappingWidth(ImageLoader.LARGE_TILE_FIT_SIZE);
                    root.getChildren().add(text);

                    // Lorsque la souris survole un message, on ajoute l'identifiant des tuiles concernées aux
                    // identifiants des tuiles qu'on souhaite surligner (highlight)
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
