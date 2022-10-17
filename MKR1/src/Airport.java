import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Airport {
    private List<Plane> planes = new ArrayList<>();
    private int[][] graphOfDestinations = { {0,  2, 3, 5, 10},
                                            {2,  0, 4, 2,  4},
                                            {3,  4, 0, 1,  0},
                                            {5,  2, 1, 0,  1},
                                            {10, 4, 0, 1,  0} };
    private Semaphore queue = new Semaphore(1);

    public void stayInQueue(People people) {
        try {
            queue.acquire();
            boolean isTake = false;
            int length = graphOfDestinations[people.getFrom()][people.getTo()];
            for(int i = 0; i < planes.size(); ++i){
                Plane plane = planes.get(i);
                if(plane.getFlightRange() >= length && plane.getCurrentNumberOFPeople() < plane.getSize()){
                    if(plane.getFrom() == -1){
                        plane.setFrom(people.getFrom());
                        plane.setTo(people.getTo());
                        plane.incrementCurrentPeoples();
                        isTake = true;
                        System.out.println(Thread.currentThread().getName() + " got on the plane");
                        break;
                    } else if(plane.getFrom() == people.getFrom() && plane.getTo() == people.getTo()) {
                        plane.incrementCurrentPeoples();
                        isTake = true;
                        System.out.println(Thread.currentThread().getName() + " got on the plane");
                        break;
                    }
                }
            }
            if(!isTake) {
                System.out.println("No one plane can take " + Thread.currentThread().getName());
            }
            queue.release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void flightAllNotFullPlanes(){
        planes.forEach(Plane::startFlight);
    }

    private void makeThreads(Airport airport){
        planes.add(new Plane(2, 10));
        planes.add(new Plane(5, 5));
        planes.forEach(Thread::start);
        List<People> peoples = new ArrayList<>();
        peoples.add(new People(0, 2, airport));
        peoples.add(new People(1, 4, airport));
        peoples.add(new People(0, 2, airport));
        peoples.add(new People(1, 4, airport));
        peoples.add(new People(1, 4, airport));
        peoples.add(new People(1, 4, airport));
        peoples.add(new People(3, 4, airport));
        peoples.forEach(Thread::start);
        for (People people : peoples) {
            try {
                people.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        flightAllNotFullPlanes();
    }

    public static void main(String[] args) {
        Airport airport = new Airport();
        airport.makeThreads(airport);
    }
}
