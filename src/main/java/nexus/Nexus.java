package nexus;

import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Processes commands for the Nexus chatbot.
 */
public class Nexus {
    private static final Path DATA_FILE = Path.of("data", "nexus.txt");
    private static final String BANNER = " _   _                      \n"
            + "| \\ | | _____  ___   _ ___ \n"
            + "|  \\| |/ _ \\ \\/ / | | / __|\n"
            + "| |\\  |  __/>  <| |_| \\__ \\\n"
            + "|_| \\_|\\___/_/\\_\\__,_|___/\n";

    private final Storage storage;
    private final TaskList tasks;

    /** Creates Nexus using the default data file. */
    public Nexus() {
        this(DATA_FILE);
    }

    /**
     * Creates Nexus using a specified data file.
     *
     * @param dataFile file used to persist tasks
     */
    Nexus(Path dataFile) {
        storage = new Storage(dataFile);
        tasks = new TaskList(loadTasks());
    }

    /**
     * Starts the console interface for Nexus.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        Nexus nexus = new Nexus();
        Ui ui = new Ui();
        ui.showWelcome(BANNER, nexus.getGreeting());

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            ui.showResponse(nexus.getResponse(input));
            if (input.equals("bye")) {
                break;
            }
        }
    }

    /**
     * Returns the greeting shown when Nexus starts.
     *
     * @return the two-line startup greeting
     */
    public String getGreeting() {
        return Ui.formatLines("Hello! I'm Nexus.", "What can I do for you?");
    }

    /**
     * Processes one user command and returns Nexus's response.
     *
     * @param input command entered by the user
     * @return a user-facing response
     */
    public String getResponse(String input) {
        try {
            if (input.equals("bye")) {
                return "Bye. Hope to see you again soon!";
            }
            if (input.equals("list")) {
                return showTasks(tasks.getTasks(), "Here are the tasks in your list:");
            }
            if (input.startsWith("find ")) {
                return findTasks(input.substring(5));
            }
            if (input.startsWith("mark ")) {
                return updateTask(input.substring(5), true);
            }
            if (input.startsWith("unmark ")) {
                return updateTask(input.substring(7), false);
            }
            if (input.startsWith("delete ")) {
                return deleteTask(input.substring(7));
            }
            return addTask(input);
        } catch (NexusException exception) {
            return "OOPS!!! " + exception.getMessage();
        } catch (DateTimeParseException exception) {
            return "OOPS!!! Please use a date in YYYY-MM-DD format.";
        } catch (NumberFormatException | IndexOutOfBoundsException exception) {
            return "OOPS!!! That task number is invalid.";
        }
    }

    /** Returns matching tasks with one-based match numbers. */
    private String findTasks(String keyword) {
        List<Task> matchingTasks = new ArrayList<>();
        String normalizedKeyword = keyword.toLowerCase(Locale.ROOT);
        for (Task task : tasks.getTasks()) {
            if (task.getDescription().toLowerCase(Locale.ROOT).contains(normalizedKeyword)) {
                matchingTasks.add(task);
            }
        }
        return showTasks(matchingTasks, "Here are the matching tasks in your list:");
    }

    /** Returns a numbered display of the supplied tasks. */
    private String showTasks(List<Task> tasksToShow, String heading) {
        List<String> lines = new ArrayList<>();
        lines.add(heading);
        for (int index = 0; index < tasksToShow.size(); index++) {
            lines.add((index + 1) + "." + tasksToShow.get(index));
        }
        return Ui.formatLines(lines.toArray(String[]::new));
    }

    /** Creates, saves, and describes a new task. */
    private String addTask(String input) throws NexusException {
        Task task = Parser.createTask(input);
        tasks.add(task);
        saveTasks();
        return Ui.formatLines("Got it. I've added this task:", task.toString());
    }

    /** Updates and saves a task's completion state. */
    private String updateTask(String indexText, boolean isDone) throws NexusException {
        Task task = tasks.get(Integer.parseInt(indexText));
        if (isDone) {
            task.markAsDone();
            saveTasks();
            return Ui.formatLines("Nice! I've marked this task as done:", task.toString());
        }

        task.unmark();
        saveTasks();
        return Ui.formatLines("OK, I've marked this task as not done yet:", task.toString());
    }

    /** Deletes, saves, and describes a task. */
    private String deleteTask(String indexText) throws NexusException {
        Task task = tasks.delete(Integer.parseInt(indexText));
        saveTasks();
        return Ui.formatLines("Noted. I've removed this task:", task.toString(),
                "Now you have " + tasks.size() + " tasks in the list.");
    }

    /** Loads saved tasks, using an empty list when reading fails. */
    private List<Task> loadTasks() {
        try {
            return storage.load();
        } catch (IOException exception) {
            return new ArrayList<>();
        }
    }

    /** Saves all current tasks or reports the failure as invalid input. */
    private void saveTasks() throws NexusException {
        try {
            storage.save(tasks.getTasks());
        } catch (IOException exception) {
            throw new NexusException("Unable to save tasks: " + exception.getMessage());
        }
    }
}
