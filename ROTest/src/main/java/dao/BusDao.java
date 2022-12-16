package dao;

import java.util.ArrayList;
import java.util.List;

public class BusDao {
    private static List<Bus> buses;

    public BusDao() {
        buses = new ArrayList<>();
        buses.add(new Bus("Driver1", 1, 1, "Volvo", 1997, 999999));
        buses.add(new Bus("Driver-1", 2, 1, "KuzKuz", 9999, 999999999));
        buses.add(new Bus("Driver2", 2, 2, "Audi", 2020, 99));
        buses.add(new Bus("Driver3", 3, 3, "Korobko", 2015, 9999));
        buses.add(new Bus("Driver4", 4, 4, "Java", 2000, 99999));
    }

    public List<Bus> busesByRootNumber(int rootNumber){
        List<Bus> ans = new ArrayList<>();
        buses.forEach(b -> {
            if(b.getRootNumber() == rootNumber) {
                ans.add(b);
            }
        });
        return ans;
    }

    public List<Bus> busesThatUsedMoreThanGivenTerm(int year) {
        List<Bus> ans = new ArrayList<>();
        buses.forEach(b -> {
            if(b.getYearOfStartOfOperation() < year) {
                ans.add(b);
            }
        });
        return ans;
    }

    public List<Bus> busesThatDroveMoreThanGivenDistance(int distance) {
        List<Bus> ans = new ArrayList<>();
        buses.forEach(b -> {
            if(b.getMileage() > distance) {
                ans.add(b);
            }
        });
        return ans;
    }
}
