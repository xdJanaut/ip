# Nexus User Guide

Nexus is a task-management chatbot that keeps track of todos, deadlines, and
events. Your tasks are saved automatically and restored the next time Nexus
starts.

## Adding tasks

- `todo DESCRIPTION` adds a task without a date.
- `deadline DESCRIPTION /by YYYY-MM-DD` adds a task with a deadline.
- `event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD` adds an event.

For example, `deadline submit report /by 2026-09-18` adds a deadline and shows
the saved task.

## Viewing and finding tasks

- `list` displays every task with its task number.
- `find KEYWORD` displays tasks whose descriptions contain the keyword. The
  search is not case-sensitive.

## Sorting tasks

Enter `sort` to arrange all tasks alphabetically by description. Capital and
lowercase letters are treated alike, and the new order is saved automatically.

For example, tasks named `write report`, `Buy milk`, and `attend meeting` are
shown in this order after sorting:

```text
Here are your tasks sorted alphabetically:
1.[T][ ] attend meeting
2.[T][ ] Buy milk
3.[T][ ] write report
```

## Updating tasks

- `mark NUMBER` marks the numbered task as completed.
- `unmark NUMBER` marks the numbered task as incomplete.
- `delete NUMBER` removes the numbered task.

Use the number currently shown by `list`, `find`, or `sort`.

## Exiting Nexus

Enter `bye` to end the conversation. Nexus saves changes whenever a command
modifies the task list.
