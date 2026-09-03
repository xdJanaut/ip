# Nexus

Nexus is a personal task-management chatbot with a JavaFX interface. It can
create, update, search, and delete tasks while saving them between sessions.

## Requirements

- JDK 25
- IntelliJ IDEA or another Java IDE with Gradle support

## Running Nexus

Open the project in IntelliJ IDEA, set the project SDK and language level to
JDK 25, and run `nexus.Launcher`.

Alternatively, launch the application from a terminal:

```shell
./gradlew run
```

To build and run the packaged application:

```shell
./gradlew shadowJar
java -jar build/libs/nexus.jar
```

## Commands

| Command | Description |
| --- | --- |
| `todo DESCRIPTION` | Adds a todo. |
| `deadline DESCRIPTION /by YYYY-MM-DD` | Adds a deadline. |
| `event DESCRIPTION /from YYYY-MM-DD /to YYYY-MM-DD` | Adds an event. |
| `list` | Lists all tasks. |
| `mark NUMBER` | Marks a task as completed. |
| `unmark NUMBER` | Marks a task as incomplete. |
| `delete NUMBER` | Deletes a task. |
| `find KEYWORD` | Finds tasks containing a keyword. |
| `bye` | Ends the conversation. |

Nexus stores its data in `data/nexus.txt`. The directory and file are created
automatically when a task is saved for the first time.

## Testing

Run the automated tests and coding-standard checks with:

```shell
./gradlew test checkstyleMain checkstyleTest
```
