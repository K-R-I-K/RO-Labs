public class Plane extends Thread{
    private int size;
    private int flightRange;
    private int from;
    private int to;
    private int currentNumberOFPeople;
    private boolean isStartFlight;

    public Plane(int size, int flightRange) {
        this.size = size;
        this.flightRange = flightRange;
        from = -1;
        to = -1;
        currentNumberOFPeople = 0;
        isStartFlight = false;
    }

    public int getSize() {
        return size;
    }

    public int getFlightRange() {
        return flightRange;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getCurrentNumberOFPeople() {
        return currentNumberOFPeople;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public void incrementCurrentPeoples(){
        ++currentNumberOFPeople;
    }

    public void startFlight(){
        isStartFlight = true;
    }

    @Override
    public void run() {
        while(currentNumberOFPeople < size) {
            if(isStartFlight) break;
        }
        if(currentNumberOFPeople == size) {
            System.out.println(Thread.currentThread().getName() + " is full and start flight");
        } else {
            System.out.println(Thread.currentThread().getName() + " isn`t full but start flight (run out of people)");
        }
    }
}
