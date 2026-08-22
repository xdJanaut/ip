import java.util.Scanner;

/**
 * Starts the Nexus chatbot.
 */
public class Nexus {
    /**
     * Prints the Nexus startup greeting and exits.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        String banner = " _   _                      \n"
                + "| \\ | | _____  ___   _ ___ \n"
                + "|  \\| |/ _ \\ \\/ / | | / __|\n"
                + "| |\\  |  __/>  <| |_| \\__ \\\n"
                + "|_| \\_|\\___/_/\\_\\__,_|___/\n";
        String divider = "________________________________________\n";
        String greeting = "Hello! I'm Nexus.\nWhat can I do for you?\n";
        String farewell = "Bye. Hope to see you again soon!\n";
        System.out.print(divider + banner + "\n" + greeting + "\n" + divider);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.print(divider + farewell + divider);
                break;
            }
            System.out.print(divider + command + "\n" + divider);
        }
    }
}
