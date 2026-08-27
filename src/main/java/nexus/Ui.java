package nexus;

/** Handles Nexus console messages. */
public class Ui {
    private static final String DIVIDER = "________________________________________\n";

    /** Shows an error message using Nexus's standard format. */
    public void showError(String message) {
        System.out.print(DIVIDER + "OOPS!!! " + message + "\n" + DIVIDER);
    }

    /** Shows a task-added message using Nexus's standard format. */
    public void showAdded(Task task) {
        System.out.print(DIVIDER + "Got it. I've added this task:\n" + task + "\n" + DIVIDER);
    }
}
