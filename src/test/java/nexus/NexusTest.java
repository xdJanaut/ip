package nexus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests Nexus responses through the interface shared by the console and GUI. */
class NexusTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getGreeting_returnsNexusIntroduction() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("Hello! I'm Nexus.\nWhat can I do for you?", nexus.getGreeting());
    }

    @Test
    void getResponse_taskWorkflow_returnsResponsesAndPersistsTasks() {
        Path dataFile = temporaryDirectory.resolve("nexus.txt");
        Nexus nexus = new Nexus(dataFile);

        assertEquals("Got it. I've added this task:\n[T][ ] read book",
                nexus.getResponse("todo read book"));
        assertEquals("Nice! I've marked this task as done:\n[T][X] read book",
                nexus.getResponse("mark 1"));
        assertEquals("Here are the tasks in your list:\n1.[T][X] read book",
                new Nexus(dataFile).getResponse("list"));
    }

    @Test
    void getResponse_findAndDelete_returnsExpectedTaskDetails() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));
        nexus.getResponse("todo read book");
        nexus.getResponse("todo submit report");

        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book",
                nexus.getResponse("find book"));
        assertEquals("Noted. I've removed this task:\n[T][ ] submit report\n"
                + "Now you have 1 tasks in the list.", nexus.getResponse("delete 2"));
    }

    @Test
    void getResponse_datedTasksAndUnmark_preservesEveryTaskType() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("Got it. I've added this task:\n[D][ ] return book (by: Dec 02 2019)",
                nexus.getResponse("deadline return book /by 2019-12-02"));
        assertEquals("Got it. I've added this task:\n"
                + "[E][ ] project meeting (from: Dec 03 2019 to: Dec 04 2019)",
                nexus.getResponse("event project meeting /from 2019-12-03 /to 2019-12-04"));
        nexus.getResponse("mark 1");
        assertEquals("OK, I've marked this task as not done yet:\n"
                + "[D][ ] return book (by: Dec 02 2019)", nexus.getResponse("unmark 1"));
    }

    @Test
    void getResponse_invalidInput_returnsReadableErrors() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("OOPS!!! The description of a todo cannot be empty.",
                nexus.getResponse("todo"));
        assertEquals("OOPS!!! That task number is invalid.", nexus.getResponse("mark 1"));
        assertTrue(nexus.getResponse("deadline return book /by tomorrow")
                .startsWith("OOPS!!! Please use a date in YYYY-MM-DD format."));
    }

    @Test
    void getResponse_bye_returnsFarewell() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("Bye. Hope to see you again soon!", nexus.getResponse("bye"));
    }
}
