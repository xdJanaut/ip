/** A task with stated start and end times. */
public class Event extends Task {
    private final String from;
    private final String to;

    /** Creates an event task. @param description the event description @param from start text @param to end text */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
