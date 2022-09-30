import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Lab4A {
    public static final String FILE_NAME = "phones.txt";
    public static final String PHONE_NUMBER = "^\\+?\\d{10,12}$";
    public static final String NAME = "[a-zA-Z]+";

    public static final AtomicBoolean readLock = new AtomicBoolean(false);
    public static final AtomicBoolean writeLock = new AtomicBoolean(false);

    private void run() {
        boolean isReader = getRole();
        while(true) {
            int choice = getChoice(isReader);
            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the name");
                    String name = new Scanner(System.in).nextLine();
                    new Thread(() -> findByName(
                            name
                                    .replaceAll("\\s+", "")
                                    .toLowerCase()
                    )).start();
                }
                case 2 -> {
                    System.out.println("Enter the phone number");
                    String phoneNumber = new Scanner(System.in).next();
                    new Thread(() -> findByPhoneNumber(phoneNumber)).start();
                }
                case 3 -> {
                    System.out.println("Enter the name");
                    String name = new Scanner(System.in).nextLine();
                    String nameToCheck = name.replaceAll("\\s+", "");
                    if(!nameToCheck.matches(NAME)) {
                        System.out.println("Invalid input");
                        break;
                    }
                    System.out.println("Enter the phone number");
                    String phoneNumber = new Scanner(System.in).next();
                    if(!phoneNumber.matches(PHONE_NUMBER)) {
                        System.out.println("Invalid input");
                        break;
                    }
                    String record = name + " - " + phoneNumber;
                    new Thread(() -> addNewRecord(record)).start();
                }
                case 4 -> {
                    System.out.println("Enter the name");
                    String nameToDelete = new Scanner(System.in).nextLine();
                    new Thread(() -> deleteByName(
                            nameToDelete
                                    .replaceAll("\\s+", "")
                                    .toLowerCase())).start();
                }
                case 0 -> System.exit(0);
            }
        }
    }
    private void findByName(String name) {
        readLock();
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                String nameFromLine = getNameFromRecord(line);
                if (nameFromLine.equals(name)) {
                    int index = line.indexOf("-");
                    sb.append(line.substring(index + 2));
                    sb.append(System.lineSeparator());
                }
                line = br.readLine();
            }
            if(sb.isEmpty()) {
                System.out.println("No records found");
                return;
            }
            System.out.println(sb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            readUnlock();
        }
    }
    private void findByPhoneNumber(String phoneNumber) {
        readLock();
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();

            while (line != null) {
                String phoneNumberFromRecord = getPhoneNumberFromRecord(line);
                if (phoneNumberFromRecord.equals(phoneNumber)) {
                    int index = line.indexOf("-");
                    sb.append(line, 0, index);
                    sb.append(System.lineSeparator());
                }
                line = br.readLine();
            }
            if(sb.isEmpty()) {
                System.out.println("No records found");
                return;
            }
            System.out.println(sb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            readUnlock();
        }
    }
    private void addNewRecord(String record) {
        writeLock();
        try(Writer output = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            output.append(record);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeUnlock();
        }
    }
    private void deleteByName(String name) {
        writeLock();
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line = br.readLine();

            while (line != null) {
                String nameFromLine = getNameFromRecord(line);
                if(!nameFromLine.equals(name)) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                }
                line = br.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try(Writer output = new BufferedWriter(new FileWriter(FILE_NAME))) {
            output.append(sb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            writeUnlock();
        }

    }
    private String getNameFromRecord(String record) {
        String name = record.substring(0, record.indexOf("-"));
        return name.replaceAll("\\s+", "").toLowerCase();
    }
    private String getPhoneNumberFromRecord(String record) {
        return record.substring(record.indexOf("-") + 2);
    }
    private int getChoice(boolean isReader) {
        while (true) {
            System.out.println("Choice what to do\n1 - Find phone number by name\n2 - Find name by phone number");
            if (!isReader) {
                System.out.println("3 - Add new record\n4 - Delete existed record by name");
            }
            System.out.println("0 - Exit");
            try {
                int choice = new Scanner(System.in).nextInt();
                if (isReader && (choice == 1 || choice == 2) ||
                        !isReader && choice >= 1 && choice <= 4 ||
                        choice == 0) {
                    return choice;
                } else {
                    System.out.println("Invalid input");
                }
            } catch (InputMismatchException ex) {
                System.out.println("Invalid input");
            }
        }
    }
    private boolean getRole() {
        while(true) {
            System.out.println("Are you a reader or a writer? Type R/W:");
            String roleInput = new Scanner(System.in).next();
            switch (roleInput) {
                case "R" -> {
                    return true;
                }
                case "W" -> {
                    return false;
                }
                default -> System.out.println("Wrong input");
            }
        }
    }
    private void readLock(){
        while(writeLock.get()){}
        readLock.set(true);
    }
    private void readUnlock(){
        readLock.set(false);
    }
    private void writeLock(){
        while(readLock.get()){}
        writeLock.set(true);
    }
    private void writeUnlock(){
        writeLock.set(false);
    }

    public static void main(String[] args) {
        new Lab4A().run();
    }
}
