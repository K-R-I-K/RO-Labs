public class People extends Thread{
    private int from;
    private int to;
    private Airport airport;

    public People(int from, int to, Airport airport) {
        this.from = from;
        this.to = to;
        this.airport = airport;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public void run() {
        airport.stayInQueue(this);
    }
}
