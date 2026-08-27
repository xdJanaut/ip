package nexus;

/** Converts user commands into tasks. */
public class Parser {
    /** Creates the appropriate task subtype for a task command. */
    public static Task createTask(String command) throws NexusException {
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
}
