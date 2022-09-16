import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class A {

    private final int[][] arr;
    private final int ARR_SIZE;
    private final AtomicInteger counter;
    private final AtomicBoolean isFound;
    {
        ARR_SIZE = 1000;
        arr = new int[ARR_SIZE][ARR_SIZE];
        counter = new AtomicInteger(0);
        isFound = new AtomicBoolean(false);
    }

    private Runnable getRunnable() {
        return () -> {
            while (!Thread.interrupted()) {
                int x = getRowNumber();
                if(x != -1)
                    System.out.println(x + " -> " + Thread.currentThread().getName());
                search(x);
            }
        };
    }

    private void makeThreads(int numberOfBees) {
        for(int i = 0; i < numberOfBees; ++i){
            Thread bee = new Thread(getRunnable());
            bee.setName("Bee" + i);
            bee.start();
        }
    }

    private synchronized int getRowNumber() {
        if(!isFound.get() && counter.get() < ARR_SIZE) {
            return counter.getAndIncrement();
        } else {
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    private void search(int row) {
        if(row == -1) {
            return;
        }
        for(int i = 0; i < ARR_SIZE; ++i) {
            if(arr[row][i] == 1) {
                isFound.set(true);
                System.out.println("Winnie the Pooh coordinates are {X:" + row + "; Y:" + i + "}. Found with " + Thread.currentThread().getName());
                break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Enter the number of bees:");
        A task = new A();
        int x = (int)(Math.random() * task.ARR_SIZE);
        int y = (int)(Math.random() * task.ARR_SIZE);
        task.arr[x][y] = 1;
        Scanner scanner = new Scanner(System.in);
        int numberOfBees = scanner.nextInt();
        task.makeThreads(numberOfBees);
    }
}
