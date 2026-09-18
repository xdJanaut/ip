package nexus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Tests task-command parsing and validation. */
class ParserTest {
    @Test
    void createTask_extraCommandWhitespace_normalizesDescription() throws Exception {
        Task task = Parser.createTask("todo     read   book");

        assertEquals("[T][ ] read book", task.toString());
    }

    @Test
    void createTask_deadlineWithRepeatedBy_throwsReadableError() {
        String command = "deadline submit /by 2026-09-18 /by 2026-09-19";
        NexusException exception = assertThrows(NexusException.class, () -> Parser.createTask(command));

        assertEquals("Use exactly one /by followed by a date in YYYY-MM-DD format.",
                exception.getMessage());
    }

    @Test
    void createTask_eventWhoseEndIsNotAfterStart_throwsReadableError() {
        String command = "event workshop /from 2026-09-18 /to 2026-09-18";
        NexusException exception = assertThrows(NexusException.class, () -> Parser.createTask(command));

        assertEquals("An event's /to date must be after its /from date.",
                exception.getMessage());
    }

    @Test
    void createTask_eventWithReversedMarkers_throwsReadableError() {
        String command = "event workshop /to 2026-09-19 /from 2026-09-18";
        NexusException exception = assertThrows(NexusException.class, () -> Parser.createTask(command));

        assertEquals("Use /from before /to, with one date after each marker.",
                exception.getMessage());
    }

    @Test
    void createTask_taskWithMissingDescription_throwsReadableError() {
        String command = "deadline /by 2026-09-18";
        NexusException exception = assertThrows(NexusException.class, () -> Parser.createTask(command));

        assertEquals("A deadline needs a description before /by.",
                exception.getMessage());
    }
}
