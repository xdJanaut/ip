import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
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
                + "1.[T][ ] read book\n"
                + "2.[T][ ] return book\n"
                + "________________________________________\n"
                + "________________________________________\n"
                + "Bye. Hope to see you again soon!\n"
                + "________________________________________\n";

        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        InputStream originalInput = System.in;
        PrintStream originalOutput = System.out;
        System.setIn(new ByteArrayInputStream("todo\ntodo read book\ntodo return book\nmark 2\nunmark 2\nlist\nbye\n"
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
    }
}
