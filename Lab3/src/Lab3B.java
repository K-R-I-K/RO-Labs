import java.util.concurrent.Semaphore;

public class Lab3B {
    private class BarberShop{
        private Semaphore isHaircutting;
        private int visitorsInQueue;
        public BarberShop() {
            this.isHaircutting = new Semaphore(1);
            visitorsInQueue = 0;
        }

        public synchronized void haircut(){
            if(visitorsInQueue == 0 && isHaircutting.availablePermits() == 1){
                try {
                    System.out.println(Thread.currentThread().getName() + " is sleeping now");
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName() + " is haircutting a visitor");
            try {
                Thread.sleep(100);
                if(visitorsInQueue != 0) {
                    System.out.println(Thread.currentThread().getName() + " wake up another visitor");
                    notify();
                    --visitorsInQueue;
                } else {
                    System.out.println(Thread.currentThread().getName() + " is sleeping without visitors");
                    isHaircutting.release();
                    wait();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public synchronized void stayInQueue(){
            if(isHaircutting.tryAcquire()){
                System.out.println(Thread.currentThread().getName() + " wake up hairdresser and get haircut");
                notify();
            } else {
                ++visitorsInQueue;
                try {
                    System.out.println(Thread.currentThread().getName() + " is sleeping in queue");
                    wait();
                    System.out.println(Thread.currentThread().getName() + " get haircut");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    private class Hairdresser extends Thread{
        private BarberShop barberShop;

        public Hairdresser(BarberShop barberShop) {
            this.barberShop = barberShop;
        }

        @Override
        public void run() {
            while(true) {
                barberShop.haircut();
            }
        }
    }
    private class Visitor extends Thread{
        private BarberShop barberShop;

        public Visitor(BarberShop barberShop){
            this.barberShop = barberShop;
        }

        @Override
        public void run() {
            barberShop.stayInQueue();
        }
    }

    private void makeThreads(int numberOfVisitors) {
        BarberShop barberShop = new BarberShop();
        Hairdresser hairdresser = new Hairdresser(barberShop);
        hairdresser.setName("Hairdresser");
        hairdresser.start();
        for(int i = 0; i < numberOfVisitors; ++i){
            Visitor visitor = new Visitor(barberShop);
            visitor.setName("Visitor" + i);
            visitor.start();
        }
    }

    public static void main(String[] args) {
        new Lab3B().makeThreads(3);
    }
}
