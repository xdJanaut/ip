import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Starts the Nexus chatbot.
 */
public class Nexus {
    private static final Path DATA_FILE = Path.of("data", "nexus.txt");
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
        List<Task> tasks = loadTasks();
        while (scanner.hasNextLine()) {
            String command = scanner.nextLine();
            if (command.equals("bye")) {
                System.out.print(divider + farewell + divider);
                break;
            }
            if (command.equals("list")) {
                System.out.print(divider);
                for (int index = 0; index < tasks.size(); index++) {
                    System.out.println((index + 1) + "." + tasks.get(index));
                }
                System.out.print(divider);
            } else if (command.startsWith("mark ")) {
                Task task = tasks.get(Integer.parseInt(command.substring(5)) - 1);
                task.markAsDone();
                saveTasks(tasks);
                System.out.print(divider + "Nice! I've marked this task as done:\n"
                        + task + "\n" + divider);
            } else if (command.startsWith("unmark ")) {
                Task task = tasks.get(Integer.parseInt(command.substring(7)) - 1);
                task.unmark();
                saveTasks(tasks);
                System.out.print(divider + "OK, I've marked this task as not done yet:\n"
                        + task + "\n" + divider);
            } else if (command.startsWith("delete ")) {
                Task task = tasks.remove(Integer.parseInt(command.substring(7)) - 1);
                saveTasks(tasks);
                System.out.print(divider + "Noted. I've removed this task:\n" + task + "\n"
                        + "Now you have " + tasks.size() + " tasks in the list.\n" + divider);
            } else {
                try {
                    Task task = createTask(command);
                    tasks.add(task);
                    saveTasks(tasks);
                    System.out.print(divider + "Got it. I've added this task:\n"
                            + task + "\n" + divider);
                } catch (NexusException exception) {
                    System.out.print(divider + "OOPS!!! " + exception.getMessage() + "\n" + divider);
                }
            }
        }
    }

    /** Creates the appropriate task subtype for a task command. */
    private static Task createTask(String command) throws NexusException {
        if (command.equals("todo")) {
            throw new NexusException("The description of a todo cannot be empty.");
        }
        if (command.startsWith("todo ")) {
            return new Todo(command.substring(5));
        }
        if (command.startsWith("deadline ")) {
            String[] parts = command.substring(9).split(" /by ", 2);
            if (parts.length != 2) {
                throw new NexusException("A deadline needs a /by time.");
            }
            return new Deadline(parts[0], parts[1]);
        }
        if (command.startsWith("event ")) {
            String[] parts = command.substring(6).split(" /from | /to ", 3);
            if (parts.length != 3) {
                throw new NexusException("An event needs /from and /to times.");
            }
            return new Event(parts[0], parts[1], parts[2]);
        }
        throw new NexusException("I'm sorry, but I don't know what that means :-(");
    }

    private static List<Task> loadTasks() {
        try {
            return new Storage(DATA_FILE).load();
        } catch (IOException exception) {
            return new ArrayList<>();
        }
    }

    private static void saveTasks(List<Task> tasks) {
        try {
            new Storage(DATA_FILE).save(tasks);
        } catch (IOException exception) {
            System.out.println("Unable to save tasks: " + exception.getMessage());
        }
    }
}
