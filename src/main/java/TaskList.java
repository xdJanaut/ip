import java.util.ArrayList;
import java.util.List;

/** Manages the tasks in the current Nexus session. */
public class TaskList {
    private final List<Task> tasks;

    /** Creates a task list containing the supplied tasks. */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /** Adds a task to the list. */
    public void add(Task task) {
        tasks.add(task);
    }

    /** Returns a task using its one-based display index. */
    public Task get(int index) {
        return tasks.get(index - 1);
    }

    /** Removes and returns a task using its one-based display index. */
    public Task delete(int index) {
        return tasks.remove(index - 1);
    }

    /** Returns the number of tasks in the list. */
    public int size() {
        return tasks.size();
    }

    /** Returns all tasks for display or storage. */
    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }
}
