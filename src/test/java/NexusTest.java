import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

/**
 * Checks the startup message presented by the Nexus chatbot.
 */
public class NexusTest {
    /**
     * Runs the test without requiring an external testing library.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Path dataFile = Path.of("data", "nexus.txt");
        try {
            Files.deleteIfExists(dataFile);
            Files.deleteIfExists(dataFile.getParent());
        } catch (Exception exception) {
            throw new AssertionError("Unable to prepare the test data directory.", exception);
        }

        String startupOutput = "________________________________________\n"
                + " _   _                      \n"
                + "| \\ | | _____  ___   _ ___ \n"
                + "|  \\| |/ _ \\ \\/ / | | / __|\n"
                + "| |\\  |  __/>  <| |_| \\__ \\\n"
                + "|_| \\_|\\___/_/\\_\\__,_|___/\n"
                + "\n"
                + "Hello! I'm Nexus.\n"
                + "What can I do for you?\n"
                + "\n"
                + "________________________________________\n";
        String expectedOutput = startupOutput
                + "________________________________________\n"
                + "OOPS!!! The description of a todo cannot be empty.\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "Got it. I've added this task:\n[T][ ] read book\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "Got it. I've added this task:\n[T][ ] return book\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "Nice! I've marked this task as done:\n"
                + "[T][X] return book\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "OK, I've marked this task as not done yet:\n"
                + "[T][ ] return book\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "Noted. I've removed this task:\n"
                + "[T][ ] return book\n"
                + "Now you have 1 tasks in the list.\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "1.[T][ ] read book\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "Bye. Hope to see you again soon!\n"
                + "________________________________________\n";

        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        System.setIn(new ByteArrayInputStream("todo\ntodo read book\ntodo return book\nmark 2\nunmark 2\ndelete 2\nlist\nbye\n"
                .getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(capturedOutput));
        try {
            Nexus.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }

        String actualOutput = capturedOutput.toString();
        if (!expectedOutput.equals(actualOutput)) {
            throw new AssertionError("Expected Nexus to print its complete greeting.");
        }

        String secondRunOutput = runApp("list\nbye\n");
        if (!secondRunOutput.contains("1.[T][ ] read book")) {
            throw new AssertionError("Expected Nexus to load tasks saved by an earlier run.");
        }

        Deadline deadline = new Deadline("return book", "2019-12-02");
        if (!deadline.toString().contains("(by: Dec 02 2019)")) {
            throw new AssertionError("Expected deadlines to display parsed dates readably.");
        }

        try {
            Files.deleteIfExists(dataFile);
            Files.deleteIfExists(dataFile.getParent());
        } catch (Exception exception) {
            throw new AssertionError("Unable to clean up the test data directory.", exception);
        }
    }

    /** Runs Nexus with the given user input and returns its output. */
    private static String runApp(String input) {
        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
        System.setOut(new PrintStream(capturedOutput));
        try {
            Nexus.main(new String[0]);
        } finally {
            System.setIn(originalInput);
            System.setOut(originalOutput);
        }
        return capturedOutput.toString();
    }
}
