# Terminal To-Do List Application (Java)

Terminal-based Task Management application built in Java. 

---



Each task record contains the following fields:  
`[S.No, Hour (0-24), Day (1-31), Month (1-12), Year, Completed (true/false), Task Description]`

- `createfile()`: Initializes a new binary data file (`todo.dat`) or resets the existing one. Prompts for the number of tasks to create and performs boundary and validation checks before writing.
- `displaying()`: Reads all records from `todo.dat` and renders them in a clean box-drawn grid table with headers (`S.No`, `Hour`, `Day`, `Month`, `Year`, `completed`, `Task`).
- `searching()`: Allows querying records using two search modes:
  - By Serial Number (`sno`): Finds and prints the specific record.
  - By Date (`day`, `month`, `year`): Filters and displays all matching tasks for that day.
- updating(): Modifies an existing task record. Allows leaving inputs blank to retain existing values, changes completion status (`yes`/`no`), and saves the updated state.
- `deleting()`: Displays current tasks, prompts for the target serial number, and safely removes the entry from the file.
- `adding()`: Appends a new task record to the existing file with automated serial number generation and input validation.
- `menu()`: The central command loop providing interactive navigation across all operations, complete with prompt pacing and user input handling.

---



The project is built entirely with standard Java SE libraries—zero third-party dependencies required:

- `java.io.Serializable`: Implemented by the internal `TaskRecord` data model to allow object state to be serialized directly to disk.
- `java.io.ObjectOutputStream` & `java.io.ObjectInputStream`: Powers binary persistence to save and reload tasks from `todo.dat`.
- `java.io.File`, `FileInputStream`, `FileOutputStream`: Manages underlying file system I/O and checks for file existence.
- `java.util.Scanner`: Captures user input interactively from standard input (`System.in`).
- `java.util.List` & `java.util.ArrayList`: Handles dynamic in-memory collections of task records for searching, sorting, and editing.
- `printFancyTable()`: A custom terminal table renderer built using Unicode box-drawing characters (`╒`, `═`, `╤`, `│`, etc.) to dynamically calculate column widths and produce neat, readable tables.
- `Thread.sleep()`: Introduces subtle pauses between menu options to ensure a smooth, readable terminal user experience.

---



```java
static class TaskRecord implements Serializable {
    int sno;            // Serial Number
    int time;           // Scheduled Hour (1-24)
    int day;            // Date (1-31)
    int month;          // Month (1-12)
    int year;           // Year (>= 0)
    boolean completed;  // Task Completion Status
    String task;        // Task Description
}
```

---



Prerequisites
- Java Development Kit (JDK 8 or newer) installed and configured on your system's PATH.

1. Compile
Open your terminal in the project directory and run:
```powershell
javac ToDoList.java
```

2. Run
Launch the application with:
```powershell
java ToDoList
```
