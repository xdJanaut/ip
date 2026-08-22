import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

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
        String expectedBanner = " _   _                      \n"
                + "| \\ | | _____  ___   _ ___ \n"
                + "|  \\| |/ _ \\ \\/ / | | / __|\n"
                + "| |\\  |  __/>  <| |_| \\__ \\\n"
                + "|_| \\_|\\___/_/\\_\\__,_|___/\n";

        ByteArrayOutputStream capturedOutput = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;
        System.setOut(new PrintStream(capturedOutput));
        try {
            Nexus.main(new String[0]);
        } finally {
            System.setOut(originalOutput);
        }

        String actualBanner = capturedOutput.toString();
        if (!expectedBanner.equals(actualBanner)) {
            throw new AssertionError("Expected Nexus to print its banner.");
        }
    }
}
