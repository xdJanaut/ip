package nexus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.FutureTask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/** Tests the observable behavior of the JavaFX controls. */
class GuiTest {
    @TempDir
    Path temporaryDirectory;

    @BeforeAll
    static void startJavaFx() throws InterruptedException {
        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);
        started.await();
    }

    @Test
    void dialogFactories_userAndNexusUseOppositeLayouts() throws Exception {
        runOnJavaFxThread(() -> {
            WritableImage image = new WritableImage(1, 1);
            DialogBox userDialog = DialogBox.getUserDialog("hello", image);
            DialogBox nexusDialog = DialogBox.getNexusDialog("hi", image);

            assertEquals(Pos.TOP_RIGHT, userDialog.getAlignment());
            assertInstanceOf(Label.class, userDialog.getChildren().get(0));
            assertInstanceOf(ImageView.class, userDialog.getChildren().get(1));
            assertEquals(Pos.TOP_LEFT, nexusDialog.getAlignment());
            assertInstanceOf(ImageView.class, nexusDialog.getChildren().get(0));
            assertInstanceOf(Label.class, nexusDialog.getChildren().get(1));
            return null;
        });
    }

    @Test
    void mainWindow_sendButtonAddsUserAndNexusDialogs() throws Exception {
        runOnJavaFxThread(() -> {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            MainWindow controller = loader.getController();
            controller.setNexus(new Nexus(temporaryDirectory.resolve("nexus.txt")));
            TextField userInput = (TextField) root.lookup("#userInput");
            Button sendButton = (Button) root.lookup("#sendButton");
            ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
            VBox dialogContainer = (VBox) scrollPane.getContent();

            userInput.setText("todo read book");
            sendButton.fire();

            assertEquals(3, dialogContainer.getChildren().size());
            assertEquals("", userInput.getText());
            return null;
        });
    }

    @Test
    void mainWindow_enterAddsUserAndNexusDialogs() throws Exception {
        runOnJavaFxThread(() -> {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane root = loader.load();
            MainWindow controller = loader.getController();
            controller.setNexus(new Nexus(temporaryDirectory.resolve("nexus-enter.txt")));
            TextField userInput = (TextField) root.lookup("#userInput");
            ScrollPane scrollPane = (ScrollPane) root.lookup("#scrollPane");
            VBox dialogContainer = (VBox) scrollPane.getContent();

            userInput.setText("todo write report");
            userInput.fireEvent(new ActionEvent());

            assertEquals(3, dialogContainer.getChildren().size());
            assertEquals("", userInput.getText());
            return null;
        });
    }

    /** Runs an assertion on the JavaFX application thread. */
    private static <T> T runOnJavaFxThread(Callable<T> assertion) throws Exception {
        FutureTask<T> task = new FutureTask<>(assertion);
        Platform.runLater(task);
        return task.get();
    }
}
