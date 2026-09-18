package nexus;

import java.time.format.DateTimeParseException;

/** Converts user commands into tasks. */
public class Parser {
    /** Creates the appropriate task subtype for a task command. */
    public static Task createTask(String command) throws NexusException {
        command = normalizeWhitespace(command);
        if (command.equals("todo")) {
            throw new NexusException("The description of a todo cannot be empty.");
        }
        if (command.startsWith("todo ")) {
            return new Todo(command.substring(5));
        }
        if (command.startsWith("deadline ")) {
            String details = command.substring(9);
            if (details.startsWith("/by ")) {
                throw new NexusException("A deadline needs a description before /by.");
            }
            if (countOccurrences(details, " /by ") != 1) {
                throw new NexusException(
                        "Use exactly one /by followed by a date in YYYY-MM-DD format.");
            }
            String[] parts = details.split(" /by ", -1);
            if (parts[0].isBlank()) {
                throw new NexusException("A deadline needs a description before /by.");
            }
            if (parts[1].isBlank()) {
                throw new NexusException("A deadline needs a date after /by.");
            }
            return new Deadline(parts[0], parts[1]);
        }
        if (command.startsWith("event ")) {
            String details = command.substring(6);
            int fromIndex = details.indexOf(" /from ");
            int toIndex = details.indexOf(" /to ");
            boolean hasOneFrom = countOccurrences(details, " /from ") == 1;
            boolean hasOneTo = countOccurrences(details, " /to ") == 1;
            if (!hasOneFrom || !hasOneTo) {
                throw new NexusException(
                        "Use exactly one /from and one /to, each followed by a date.");
            }
            if (fromIndex < 1 || toIndex <= fromIndex) {
                throw new NexusException(
                        "Use /from before /to, with one date after each marker.");
            }

            String description = details.substring(0, fromIndex);
            String from = details.substring(fromIndex + 7, toIndex);
            String to = details.substring(toIndex + 5);
            if (description.isBlank()) {
                throw new NexusException("An event needs a description before /from.");
            }
            if (from.isBlank() || to.isBlank()) {
                throw new NexusException(
                        "Use /from before /to, with one date after each marker.");
            }
            try {
                return new Event(description, from, to);
            } catch (DateTimeParseException exception) {
                throw exception;
            } catch (IllegalArgumentException exception) {
                throw new NexusException(exception.getMessage());
            }
        }
        throw new NexusException("I don't recognise that command. "
                + "Try todo, deadline, event, list, find, sort, mark, unmark, delete, or bye.");
    }

    /** Trims input and collapses repeated whitespace to a single space. */
    private static String normalizeWhitespace(String text) {
        return text == null ? "" : text.trim().replaceAll("\\s+", " ");
    }

    /** Counts non-overlapping appearances of a command marker. */
    private static int countOccurrences(String text, String marker) {
        int count = 0;
        int nextIndex = 0;
        while ((nextIndex = text.indexOf(marker, nextIndex)) >= 0) {
            count++;
            nextIndex += marker.length();
        }
        return count;
    }
}
