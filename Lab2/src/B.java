import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Munition {
    private int serialNumber;
    private int price;

    public Munition(int serialNumber, int price) {
        this.serialNumber = serialNumber;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Munition{" +
                "serial number=" + serialNumber +
                ", price=" + price +
                '}';
    }
}

public class B {
    private static BlockingQueue<Munition> buffer1;
    private static BlockingQueue<Munition> buffer2;
    private static List<Munition> munitions;

    public static void main(String[] args) {

        buffer1 = new LinkedBlockingQueue<>();
        buffer2 = new LinkedBlockingQueue<>();

        munitions = IntStream.range(0, 100).boxed()
                .map(i -> new Munition(i, (int)(Math.random() * 100)))
                .collect(Collectors.toList());
        munitions.forEach(System.out::println);
        int initialPrice = munitions.stream()
                .mapToInt(Munition::getPrice)
                .sum();

        System.out.println("Initial price: " + initialPrice);

        AtomicInteger finalPrice = new AtomicInteger(0);

        Thread ivanov = new Thread(() -> {
            while (munitions.size() > 0) {
                int randomNumber = (int) (Math.random() * munitions.size());
                try {
                    buffer1.put(munitions.remove(randomNumber));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        ivanov.setName("Ivanov");

        Thread petrov = new Thread(() -> {
            while (ivanov.isAlive() || !buffer1.isEmpty()) {
                try {
                    buffer2.put(buffer1.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        petrov.setName("Petrov");

        Thread nechiporchuk = new Thread(() -> {
            while (petrov.isAlive() || !buffer2.isEmpty()) {
                try {
                    finalPrice.addAndGet(buffer2.take().getPrice());
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Final price: " + finalPrice.get());
        });
        nechiporchuk.setName("Nechiporchuk");

        ivanov.start();
        petrov.start();
        nechiporchuk.start();
    }
}
