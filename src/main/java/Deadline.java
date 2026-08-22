/** A task to be completed by a stated time. */
public class Deadline extends Task {
    private final String by;

    /** Creates a deadline task. @param description the task description @param by the deadline text */
    public Deadline(String description, String by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toString() {
        return super.toString() + " (by: " + by + ")";
    }
}
