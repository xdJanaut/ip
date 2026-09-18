package nexus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests Nexus responses through the interface shared by the console and GUI. */
class NexusTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void getCommandResponse_invalidCommand_marksResponseAsError() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        Response response = nexus.getCommandResponse("nonsense");

        assertTrue(response.isError());
        assertFalse(response.shouldExit());
        assertTrue(response.text().contains("I don't recognise"));
    }

    @Test
    void getCommandResponse_bye_marksResponseForExit() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        Response response = nexus.getCommandResponse("bye");

        assertFalse(response.isError());
        assertTrue(response.shouldExit());
        assertEquals("You're all set. See you next time!", response.text());
    }

    @Test
    void getGreeting_returnsNexusIntroduction() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("Hey! I'm Nexus, your calm productivity sidekick.\n"
                + "What's our next move?", nexus.getGreeting());
    }

    @Test
    void getResponse_taskWorkflow_returnsResponsesAndPersistsTasks() {
        Path dataFile = temporaryDirectory.resolve("nexus.txt");
        Nexus nexus = new Nexus(dataFile);

        assertEquals("Locked in! I've added this task:\n[T][ ] read book",
                nexus.getResponse("todo read book"));
        assertEquals("Nice progress! I've marked this task as done:\n[T][X] read book",
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
        assertEquals("Cleared from your path:\n[T][ ] submit report\n"
                + "Now you have 1 task in the list.", nexus.getResponse("delete 2"));
    }

    @Test
    void getResponse_sort_ordersTasksAlphabeticallyAndPersistsOrder() {
        Path dataFile = temporaryDirectory.resolve("nexus.txt");
        Nexus nexus = new Nexus(dataFile);
        nexus.getResponse("todo write report");
        nexus.getResponse("todo Buy milk");
        nexus.getResponse("todo attend meeting");

        assertEquals("Here are your tasks sorted alphabetically:\n"
                        + "1.[T][ ] attend meeting\n"
                        + "2.[T][ ] Buy milk\n"
                        + "3.[T][ ] write report",
                nexus.getResponse("sort"));
        assertEquals("Here are the tasks in your list:\n"
                        + "1.[T][ ] attend meeting\n"
                        + "2.[T][ ] Buy milk\n"
                        + "3.[T][ ] write report",
                new Nexus(dataFile).getResponse("list"));
    }

    @Test
    void getResponse_datedTasksAndUnmark_preservesEveryTaskType() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("Locked in! I've added this task:\n[D][ ] return book (by: Dec 02 2019)",
                nexus.getResponse("deadline return book /by 2019-12-02"));
        assertEquals("Locked in! I've added this task:\n"
                + "[E][ ] project meeting (from: Dec 03 2019 to: Dec 04 2019)",
                nexus.getResponse("event project meeting /from 2019-12-03 /to 2019-12-04"));
        nexus.getResponse("mark 1");
        assertEquals("No pressure—I've marked this task as not done yet:\n"
                + "[D][ ] return book (by: Dec 02 2019)", nexus.getResponse("unmark 1"));
    }

    @Test
    void getResponse_invalidInput_returnsReadableErrors() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("The description of a todo cannot be empty.",
                nexus.getResponse("todo"));
        assertEquals("Choose a task number from 1 to 0.", nexus.getResponse("mark 1"));
        assertTrue(nexus.getResponse("deadline return book /by tomorrow")
                .startsWith("I couldn't read that date. Please use YYYY-MM-DD."));
    }

    @Test
    void getCommandResponse_whitespaceAroundCommand_acceptsCommand() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));
        nexus.getResponse("todo read book");

        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book",
                nexus.getResponse("   list   "));
    }

    @Test
    void getCommandResponse_blankInput_returnsSpecificError() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        Response response = nexus.getCommandResponse("   ");

        assertTrue(response.isError());
        assertEquals("Please enter a command. Type list to see your tasks.", response.text());
    }

    @Test
    void getCommandResponse_emptyFindKeyword_returnsSpecificError() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("Please include a keyword after find.",
                nexus.getCommandResponse("find   ").text());
    }

    @Test
    void getCommandResponse_invalidTaskIndices_returnsSpecificError() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));
        nexus.getResponse("todo read book");

        assertEquals("Choose a task number from 1 to 1.", nexus.getResponse("mark 0"));
        assertEquals("Choose a task number from 1 to 1.", nexus.getResponse("delete -1"));
        assertEquals("Choose a task number from 1 to 1.", nexus.getResponse("unmark 2"));
        assertEquals("Task numbers must be whole numbers.", nexus.getResponse("mark first"));
    }

    @Test
    void getCommandResponse_impossibleDate_returnsDateError() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        Response response = nexus.getCommandResponse("deadline report /by 2026-02-30");

        assertTrue(response.isError());
        assertEquals("I couldn't read that date. Please use YYYY-MM-DD.", response.text());
    }

    @Test
    void getResponse_bye_returnsFarewell() {
        Nexus nexus = new Nexus(temporaryDirectory.resolve("nexus.txt"));

        assertEquals("You're all set. See you next time!", nexus.getResponse("bye"));
    }
}
