import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Lab5A {
    private static final Lab5A main = new Lab5A();
    public static final CyclicBarrier BARRIER = new CyclicBarrier(3, main.barrierRunnable());
    private static Line line1;
    private static Line line2;
    private static Line line3;
    public Runnable barrierRunnable() {
        return () -> {
            changeBorders(line1, line2);
            changeBorders(line2, line3);
            printLines();
            if (Direction.numberOfDifDirections(line1.getDirections())
                    + Direction.numberOfDifDirections(line2.getDirections())
                    + Direction.numberOfDifDirections(line3.getDirections()) <= 1) {
                line1.interrupt();
                line2.interrupt();
                line3.interrupt();
            }
        };
    }
    private void printLines() {
        System.out.println("Line1" + line1.toString());
        System.out.println("Line2" + line2.toString());
        System.out.println("Line3" + line3.toString());
        System.out.println("-----------------------------------------------");
    }
    private void changeBorders(Line line1, Line line2) {
        if(!line1.isRightChanged() && !line2.isLeftChanged()
                && line1.getDirections().get(49) == Direction.RIGHT && line2.getDirections().get(0) == Direction.LEFT) {
            line1.getDirections().set(49, Direction.LEFT);
            line2.getDirections().set(0, Direction.RIGHT);
        }
    }
    private static class Line extends Thread {
        private final List<Direction> directions;
        public boolean isLeftChanged;
        public boolean isRightChanged;
        public Line() {
            directions = Direction.generateLine();
        }
        public List<Direction> getDirections() {
            return directions;
        }
        public boolean isLeftChanged() {
            return isLeftChanged;
        }
        public boolean isRightChanged() {
            return isRightChanged;
        }
        @Override
        public void run() {
            while(true) {
                try {
                    BARRIER.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    break;
                }
                changeDirections();
            }
        }
        public void changeDirections() {
            isLeftChanged = false;
            isRightChanged = false;
            for(int i = 0; i < directions.size() - 1; ++i) {
                if(directions.get(i) == Direction.RIGHT && directions.get(i + 1) == Direction.LEFT) {
                    directions.set(i, Direction.LEFT);
                    directions.set(i + 1, Direction.RIGHT);
                    if(i == 0) {
                        isLeftChanged = true;
                    }
                    if(i == 48) {
                        isRightChanged = true;
                    }
                    ++i;
                }
            }
        }
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Direction x : directions) {
                sb.append(x == Direction.LEFT ? "<" : ">");
            }
            return sb.toString();
        }
    }
    private enum Direction {
        LEFT,
        RIGHT;
        public static List<Direction> generateLine() {
            List<Direction> recruits = new ArrayList<>();
            for(int i = 0; i < 50; ++i) {
                recruits.add(Math.random() < 0.5 ? LEFT : RIGHT);
            }
            return recruits;
        }
        public static int numberOfDifDirections(List<Direction> recruits) {
            int counter = 0;
            for(int i = 0; i < recruits.size() - 1; ++i) {
                if(recruits.get(i) != recruits.get(i + 1)) {
                    ++counter;
                }
            }
            return counter;
        }

    }
    private void makeLines(){
        line1 = new Line();
        line2 = new Line();
        line3 = new Line();
        line1.start();
        line2.start();
        line3.start();
    }
    public static void main(String[] args) {
        main.makeLines();
    }
}