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
        Task[] tasks = new Task[100];
        int taskCount = 0;
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.print(divider + farewell + divider);
                break;
            }
            if (command.equals("list")) {
                System.out.print(divider);
                for (int index = 0; index < taskCount; index++) {
                    System.out.println((index + 1) + "." + tasks[index]);
                }
                System.out.print(divider);
            } else if (command.startsWith("mark ")) {
                Task task = tasks[Integer.parseInt(command.substring(5)) - 1];
                task.markAsDone();
                System.out.print(divider + "Nice! I've marked this task as done:\n"
                        + task + "\n" + divider);
            } else if (command.startsWith("unmark ")) {
                Task task = tasks[Integer.parseInt(command.substring(7)) - 1];
                task.unmark();
                System.out.print(divider + "OK, I've marked this task as not done yet:\n"
                        + task + "\n" + divider);
            } else {
                tasks[taskCount] = new Task(command);
                taskCount++;
                System.out.print(divider + "added: " + command + "\n" + divider);
            }
        }
    }
}
