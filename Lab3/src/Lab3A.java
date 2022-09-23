import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Lab3A {
    private class Pot {
        private static final int CAPACITY = 10;
        public AtomicInteger numberOfSwallows;
        public AtomicBoolean isNeedToFill;

        public Pot() {
            numberOfSwallows = new AtomicInteger(0);
            isNeedToFill = new AtomicBoolean(true);
        }

        public synchronized void bringHoney() {
            if (isNeedToFill.get()) {
                if (numberOfSwallows.get() >= CAPACITY) {
                    try {
                        notify();
                        isNeedToFill.set(false);
                        wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(Thread.currentThread().getName() + " bring honey. " +
                        "In pot " + numberOfSwallows.get() + " swallows");
                //System.out.println(Thread.currentThread().getName());
                numberOfSwallows.incrementAndGet();
            }
        }

        public synchronized void eatHoney() {
            if (numberOfSwallows.get() < CAPACITY) {
                try {
                    isNeedToFill.set(true);
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            while (numberOfSwallows.get() != 0) {
                System.out.println(Thread.currentThread().getName() + " eat honey. " +
                    "In pot " + numberOfSwallows.get() + " swallows");
                numberOfSwallows.decrementAndGet();
            }
            notify();
        }
    }
    public class Bear extends Thread {
        public Pot pot;
        public Bear(Pot pot) {
            this.pot = pot;
        }
        
        @Override
        public void run() {
            while (true) {
                pot.eatHoney();
            }
        }
    }
    public class Bee extends Thread{
        public Pot pot;
        public Bee(Pot pot) {
            this.pot = pot;
        }

        @Override
        public void run() {
            while (true) {
                pot.bringHoney();
            }
        }
    }


    private void makeThreads(int numberOfBees) {
        Pot pot = new Pot();
        Bear bear = new Bear(pot);
        bear.setName("Bear");
        bear.start();
        for(int i = 0; i < numberOfBees; ++i){
            Bee bee = new Bee(pot);
            bee.setName("Bee" + i);
            bee.start();
        }
    }

    public static void main(String[] args) {
        new Lab3A().makeThreads(3);
    }
}
