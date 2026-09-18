import java.io.*;
import java.util.*;

public class ToDoList {


    static class TaskRecord implements Serializable {
        private static final long serialVersionUID = 1L;
        int sno;
        int time;
        int day;
        int month;
        int year;
        boolean completed;
        String task;

        public TaskRecord(int sno, int time, int day, int month, int year, boolean completed, String task) {
            this.sno = sno;
            this.time = time;
            this.day = day;
            this.month = month;
            this.year = year;
            this.completed = completed;
            this.task = task;
        }
    }

    private static final String FILE_NAME = "todo.dat";
    private static final Scanner scanner = new Scanner(System.in);


    private static String input(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    private static void sleep(double seconds) {
        try {
            Thread.sleep((long) (seconds * 1000));
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    //  to read all records from todo.dat
    @SuppressWarnings("unchecked")
    private static List<TaskRecord> readRecords() {
        List<TaskRecord> records = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return records;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    TaskRecord s = (TaskRecord) in.readObject();
                    records.add(s);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            // file not found, return empty records
        } catch (Exception e) {
            // handle corrupted/unreadable file
        }
        return records;
    }


    private static void writeRecords(List<TaskRecord> records) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            for (TaskRecord s : records) {
                out.writeObject(s);
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }


    private static void printFancyTable(List<TaskRecord> records, String[] headers) {
        int[] colWidths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            colWidths[i] = headers[i].length();
        }

        for (TaskRecord r : records) {
            colWidths[0] = Math.max(colWidths[0], String.valueOf(r.sno).length());
            colWidths[1] = Math.max(colWidths[1], String.valueOf(r.time).length());
            colWidths[2] = Math.max(colWidths[2], String.valueOf(r.day).length());
            colWidths[3] = Math.max(colWidths[3], String.valueOf(r.month).length());
            colWidths[4] = Math.max(colWidths[4], String.valueOf(r.year).length());
            colWidths[5] = Math.max(colWidths[5], String.valueOf(r.completed).length());
            colWidths[6] = Math.max(colWidths[6], r.task != null ? r.task.length() : 0);
        }


        for (int i = 0; i < colWidths.length; i++) {
            colWidths[i] += 2;
        }


        System.out.print("╒");
        for (int i = 0; i < colWidths.length; i++) {
            System.out.print("═".repeat(colWidths[i]));
            if (i < colWidths.length - 1) System.out.print("╤");
        }
        System.out.println("╕");

        // Headers: │ S.No │ Hour │ ...
        System.out.print("│");
        for (int i = 0; i < headers.length; i++) {
            System.out.printf(" %-" + (colWidths[i] - 1) + "s│", headers[i]);
        }
        System.out.println();


        System.out.print("╞");
        for (int i = 0; i < colWidths.length; i++) {
            System.out.print("═".repeat(colWidths[i]));
            if (i < colWidths.length - 1) System.out.print("╪");
        }
        System.out.println("╡");

        // Rows
        for (TaskRecord r : records) {
            System.out.print("│");
            System.out.printf(" %-" + (colWidths[0] - 1) + "s│", r.sno);
            System.out.printf(" %-" + (colWidths[1] - 1) + "s│", r.time);
            System.out.printf(" %-" + (colWidths[2] - 1) + "s│", r.day);
            System.out.printf(" %-" + (colWidths[3] - 1) + "s│", r.month);
            System.out.printf(" %-" + (colWidths[4] - 1) + "s│", r.year);
            System.out.printf(" %-" + (colWidths[5] - 1) + "s│", r.completed);
            System.out.printf(" %-" + (colWidths[6] - 1) + "s│", r.task != null ? r.task : "");
            System.out.println();
        }

        // Bottom border: ╘═══╧═══╛
        System.out.print("╘");
        for (int i = 0; i < colWidths.length; i++) {
            System.out.print("═".repeat(colWidths[i]));
            if (i < colWidths.length - 1) System.out.print("╧");
        }
        System.out.println("╛");
    }

    // Function to create a new data file or deleting all data if already exists
    public static void createfile() {
        int sno = 1;
        List<TaskRecord> records = new ArrayList<>();

        try {
            int number = Integer.parseInt(input("enter number of records you wish to enter: "));
            for (int i = 0; i < number; i++) {
                int time = Integer.parseInt(input("enter the hour you want to do the task in(0-24): "));
                int day = Integer.parseInt(input("enter the date of the task: "));
                int month = Integer.parseInt(input("enter the month of task completing: "));
                int year = Integer.parseInt(input("enter year: "));
                boolean completed = false;
                String task = input("enter task you want to achieve: ");

                if (time < 1 || time > 24 || day < 1 || day > 31 || month < 1 || month > 12 || year < 0 || task.trim().isEmpty() || task.length() > 10000) {
                    if (time > 24 || time < 1) {
                        System.out.println("TIME OUT OF BOUND !");
                    }
                    if (day > 31 || day < 1) {
                        System.out.println("DATE IS OUT OF BOUNDS, PLEASE ENTER WITH 1-31");
                    }
                    if (month > 12 || month < 1) {
                        System.out.println("PLEASE ENTER MONTHS 1-12");
                    }
                    if (task.length() > 10000) {
                        System.out.println("TASK LENGTH TOO LONG PLEASE ENTER TASK WITHIN THOUSAND CHARACTERS");
                    }
                    if (task.trim().isEmpty()) {
                        System.out.println("Input cannot be empty or just spaces!");
                    }
                } else {
                    TaskRecord rec = new TaskRecord(sno, time, day, month, year, completed, task);
                    sno++;
                    records.add(rec);
                }
            }
            writeRecords(records);
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Function to display all the user data
    public static void displaying() {
        List<TaskRecord> records = readRecords();
        String[] headers = {"S.No", "Hour", "Day", "Month", "Year", "completed", "Task"};

        if (!records.isEmpty()) {
            printFancyTable(records, headers);
        } else {
            System.out.println("No records found.");
        }
        System.out.println();
        System.out.println("ALL THE TASKS HAVE BEEN DISPLAYED");
        System.out.println();
    }

    // Function to search for a particular record
    public static void searching() {
        String[] headers = {"S.No", "Hour", "Day", "Month", "Year", "completed", "Task"};
        List<TaskRecord> allRecords = readRecords();
        List<TaskRecord> records = new ArrayList<>();

        String searchwith = input("enter what you want to search with(sno) for serieal number,(date) for searching with date: ");

        if (searchwith.equalsIgnoreCase("sno")) {
            try {
                int sno = Integer.parseInt(input("enter serial number you want searched: "));
                boolean found = false;
                for (TaskRecord s : allRecords) {
                    if (s.sno == sno) {
                        records.add(s);
                        printFancyTable(records, headers);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println("no record with serial number " + sno);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid serial number!");
            }
        } else if (searchwith.equalsIgnoreCase("date")) {
            try {
                int day = Integer.parseInt(input("enter date(day 1-31): "));
                int month = Integer.parseInt(input("enter month(1-12): "));
                int year = Integer.parseInt(input("enter year (greater than 0000): "));

                for (TaskRecord s : allRecords) {
                    if (s.day == day && s.month == month && s.year == year) {
                        records.add(s);
                    }
                }
                if (!records.isEmpty()) {
                    printFancyTable(records, headers);
                } else {
                    System.out.println("No records found for the given date.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid numeric input!");
            }
        }
    }

    // Function to update a record
    public static void updating() {
        List<TaskRecord> records = readRecords();
        if (records.isEmpty()) {
            System.out.println("No records available to update.");
            return;
        }

        try {
            int sno = Integer.parseInt(input("enter serial number of record you want to update: "));
            boolean exists = false;
            for (TaskRecord r : records) {
                if (r.sno == sno) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                System.out.println("Record with serial number " + sno + " not found.");
                return;
            }

            String new_hour = input("enter new time(0-24): ");
            String new_day = input("enter new day (1-31): ");
            String new_month = input("enter new month(1-12): ");
            String new_year = input("enter new year(greater than 0000): ");
            String new_task_status = input("yes or no if completed or not: ");
            String new_task = input("enter updated task statement less than 10000 chars: ");

            boolean updatedStatus;
            if (new_task_status.equalsIgnoreCase("yes")) {
                updatedStatus = true;
            } else if (new_task_status.equalsIgnoreCase("no")) {
                updatedStatus = false;
            } else {
                System.out.println("Error");
                updating();
                return;
            }

            for (TaskRecord s : records) {
                if (s.sno == sno) {
                    int old_hour = s.time;
                    int old_day = s.day;
                    int old_month = s.month;
                    int old_year = s.year;
                    boolean old_task_status = s.completed;
                    String old_task = s.task;

                    if (new_hour.trim().isEmpty()) {
                        s.time = old_hour;
                    } else {
                        s.time = Integer.parseInt(new_hour.trim());
                    }

                    if (new_day.trim().isEmpty()) {
                        s.day = old_day;
                    } else {
                        s.day = Integer.parseInt(new_day.trim());
                    }

                    if (new_month.trim().isEmpty()) {
                        s.month = old_month;
                    } else {
                        s.month = Integer.parseInt(new_month.trim());
                    }

                    if (new_year.trim().isEmpty()) {
                        s.year = old_year;
                    } else {
                        s.year = Integer.parseInt(new_year.trim());
                    }

                    s.completed = updatedStatus;

                    if (new_task.trim().isEmpty()) {
                        s.task = old_task;
                    } else {
                        s.task = new_task;
                    }
                    break;
                }
            }

            writeRecords(records);
            System.out.println("Record updated successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input!");
        }
    }

    // Function to delete a record
    public static void deleting() {
        displaying();
        List<TaskRecord> currentRecords = readRecords();
        if (currentRecords.isEmpty()) {
            return;
        }

        try {
            int sno = Integer.parseInt(input("enter serial number of record you want to delelte: "));
            List<TaskRecord> records = new ArrayList<>();
            boolean found = false;

            for (TaskRecord s : currentRecords) {
                if (s.sno != sno) {
                    records.add(s);
                } else {
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No record found with serial number " + sno);
                return;
            }

            System.out.println("deleting record...");
            sleep(0.4);
            System.out.println();

            writeRecords(records);
            System.out.println("record deleted successfully");
        } catch (NumberFormatException e) {
            System.out.println("Invalid serial number!");
        }
    }

    // Function to append a record at the end
    public static void adding() {
        List<TaskRecord> records = readRecords();
        int num = records.size();
        int sno = num + 1;

        try {
            int time = Integer.parseInt(input("Enter the hour you want to do the task in (1-24): "));
            int day = Integer.parseInt(input("Enter the date of the task (1-31): "));
            int month = Integer.parseInt(input("Enter the month of task completing (1-12): "));
            int year = Integer.parseInt(input("Enter year (>= 0): "));
            boolean completed = false;
            String task = input("Enter task you want to achieve: ").trim();

            List<String> errors = new ArrayList<>();
            if (!(1 <= time && time <= 24)) {
                errors.add("Time out of bound! (1-24)");
            }
            if (!(1 <= day && day <= 31)) {
                errors.add("Date is out of bounds! (1-31)");
            }
            if (!(1 <= month && month <= 12)) {
                errors.add("Month out of bounds! (1-12)");
            }
            if (year < 0) {
                errors.add("Year must be >= 0");
            }
            if (task.length() > 10000) {
                errors.add("Task length too long (max 10000 chars)");
            }
            if (task.isEmpty()) {
                errors.add("Input cannot be empty or just spaces!");
            }

            if (!errors.isEmpty()) {
                for (String e : errors) {
                    System.out.println(e.toUpperCase());
                }
                return;
            }

            TaskRecord rec = new TaskRecord(sno, time, day, month, year, completed, task);
            records.add(rec);
            writeRecords(records);
            System.out.println("Record added successfully with serial number " + sno + ".");

        } catch (NumberFormatException e) {
            System.out.println("Invalid numeric input. Please enter valid numbers.");
        }
    }

    // Main interactive menu loop
    public static void menu() {
        while (true) {
            System.out.println();
            sleep(0.5);

            System.out.println("Enter (new) to create a new file");
            System.out.println();

            sleep(0.5);

            System.out.println("Enter (read) to display all the tasks or records");
            System.out.println();

            sleep(0.5);

            System.out.println("Enter (search) to search for a record using serial number or date");
            System.out.println();

            sleep(0.5);

            System.out.println("Enter (update) to update a record");
            System.out.println();

            sleep(0.5);

            System.out.println("enter (delete) to delete a record");
            System.out.println();

            sleep(0.5);

            System.out.println("Enter (add) to append a record at the end");
            System.out.println();

            sleep(0.5);

            System.out.println("Enter (quit) to exit the programme");
            System.out.println();

            System.out.println("_".repeat(50));
            System.out.println();

            String choice = input("PLEASE ENTER YOUR CHOICE OF FUNCTION TO PROCEED: ").trim();

            if (choice.equalsIgnoreCase("new")) {
                createfile();
            } else if (choice.equalsIgnoreCase("read")) {
                displaying();
            } else if (choice.equalsIgnoreCase("search")) {
                searching();
            } else if (choice.equalsIgnoreCase("update")) {
                updating();
            } else if (choice.equalsIgnoreCase("delete")) {
                deleting();
            } else if (choice.equalsIgnoreCase("add")) {
                adding();
            } else if (choice.equalsIgnoreCase("quit")) {
                System.out.println("PROGRAMME ENDED");
                break;
            } else {
                System.out.println("ENTER VALID CHOICE!!");
                menu();
                break;
            }
        }
    }

    public static void main(String[] args) {
        menu();
    }
}
