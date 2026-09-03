package nexus;

import java.util.Objects;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Controls the main Nexus window declared in FXML.
 */
public class MainWindow extends AnchorPane {
    private static final Duration EXIT_DELAY = Duration.seconds(1);

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private final Image userImage = loadImage("/images/DaUser.png");
    private final Image nexusImage = loadImage("/images/DaDuke.png");
    private Nexus nexus;

    /** Keeps the most recent dialog visible when the conversation grows. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the chatbot engine used to answer user commands.
     *
     * @param nexus chatbot engine for this window
     */
    public void setNexus(Nexus nexus) {
        this.nexus = nexus;
        dialogContainer.getChildren().add(
                DialogBox.getNexusDialog(nexus.getGreeting(), nexusImage));
    }

    /** Adds the user command and Nexus response to the conversation. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = nexus.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getNexusDialog(response, nexusImage));
        userInput.clear();

        if (input.equals("bye")) {
            PauseTransition exitDelay = new PauseTransition(EXIT_DELAY);
            exitDelay.setOnFinished(event -> Platform.exit());
            exitDelay.play();
        }
    }

    /** Loads an image resource required by the conversation view. */
    private Image loadImage(String resourcePath) {
        return new Image(Objects.requireNonNull(
                getClass().getResourceAsStream(resourcePath), "Missing resource " + resourcePath));
    }
}
