import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Lab4B {
    private static final String FILE_NAME = "garden.txt";
    private final boolean[][] garden;
    ReadWriteLock rwLock = new ReentrantReadWriteLock();

    Lock readLock = rwLock.readLock();
    Lock writeLock = rwLock.writeLock();
    {
        garden = new boolean[10][10];
    }
    private void start (){
        new Thread(this::lookAfterGarden).start();
        new Thread(this::changeNature).start();
        new Thread(this::writeToFile).start();
        new Thread(this::printFromFile).start();
    }
    private void printFromFile() {
        while (true) {
            StringBuffer sb = new StringBuffer();
            readLock.lock();
            try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append(System.lineSeparator());
                    if(line.isBlank()){
                        sb = new StringBuffer();
                    }
                    line = br.readLine();
                }
                System.out.println(sb);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            readLock.unlock();
            timeForProcess(1000);
        }
    }
    private void writeToFile() {
        writeLock.lock();
        try (Writer output = new BufferedWriter(new FileWriter(FILE_NAME))) {
            output.append("");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeLock.unlock();
        while (true) {
            writeLock.lock();
            try (Writer output = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
                StringBuffer sb = new StringBuffer();
                sb.append(System.lineSeparator());
                for (boolean[] gardenRow : garden) {
                    for (boolean gardenBed : gardenRow) {
                        sb.append(gardenBed ? "1" : "0");
                    }
                    sb.append(System.lineSeparator());
                }
                System.out.println("Thread write garden to file");
                output.append(sb);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeLock.unlock();
            timeForProcess(1000);
        }
    }
    private void changeNature() {
        while(true) {
            for (int i = 0; i < garden.length; ++i) {
                for (int j = 0; j < garden[0].length; ++j) {
                    if (Math.random() * garden.length <= garden.length / 20.0) {
                        System.out.println("Nature change [" + i + " " + j + "] garden bed");
                        garden[i][j] = !garden[i][j];
                    }
                    timeForProcess(30);
                }
            }
        }
    }
    private void timeForProcess(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    private void lookAfterGarden(){
        while(true) {
            for (int i = 0; i < garden.length; ++i) {
                for (int j = 0; j < garden[0].length; ++j) {
                    if (!garden[i][j]) {
                        System.out.println("Gardener look after [" + i + " " + j + "] garden bed");
                        garden[i][j] = true;
                        timeForProcess(1000);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        new Lab4B().start();
    }
}
