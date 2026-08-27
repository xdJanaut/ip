package nexus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests task-list operations. */
class TaskListTest {
    @Test
    void delete_secondTask_returnsItAndReducesSize() {
        TaskList tasks = new TaskList(List.of(new Todo("read"), new Todo("write")));

        Task deletedTask = tasks.delete(2);

        assertEquals("[T][ ] write", deletedTask.toString());
        assertEquals(1, tasks.size());
    }
}
