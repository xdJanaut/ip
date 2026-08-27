package nexus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Stores tasks in a text file between Nexus sessions. */
public class Storage {
    /** Separator between fields in the persisted text format. */
    private static final String SEPARATOR = " | ";
    /** File used to preserve task data between sessions. */
    private final Path dataFile;

    /** Creates storage that uses the supplied data file. */
    public Storage(Path dataFile) {
        this.dataFile = dataFile;
    }

    /** Loads the saved tasks, returning an empty list when no file exists yet. */
    public List<Task> load() throws IOException {
        List<Task> tasks = new ArrayList<>();
        if (!Files.exists(dataFile)) {
            return tasks;
        }

        for (String line : Files.readAllLines(dataFile)) {
            Task task = parseTask(line);
            if (task != null) {
                tasks.add(task);
            }
        }
        return tasks;
    }

    /** Saves every task to disk. */
    public void save(List<Task> tasks) throws IOException {
        Path parent = dataFile.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(formatTask(task));
        }
        Files.write(dataFile, lines);
    }

    private Task parseTask(String line) {
        String[] parts = line.split(" \\| ", 4);
        if (parts.length < 3) {
            return null;
        }

        Task task;
        switch (parts[0]) {
        case "T":
            task = new Todo(parts[2]);
            break;
        case "D":
            if (parts.length != 4) {
                return null;
            }
            task = new Deadline(parts[2], parts[3]);
            break;
        case "E":
            if (parts.length != 4) {
                return null;
            }
            String[] eventTimes = parts[3].split(" \\| ", 2);
            if (eventTimes.length != 2) {
                return null;
            }
            task = new Event(parts[2], eventTimes[0], eventTimes[1]);
            break;
        default:
            return null;
        }

        if (parts[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    private String formatTask(Task task) {
        String status = task.isDone() ? "1" : "0";
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return "D" + SEPARATOR + status + SEPARATOR + task.getDescription()
                    + SEPARATOR + deadline.getBy();
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return "E" + SEPARATOR + status + SEPARATOR + task.getDescription()
                    + SEPARATOR + event.getFrom() + SEPARATOR + event.getTo();
        }
        return "T" + SEPARATOR + status + SEPARATOR + task.getDescription();
    }
}
