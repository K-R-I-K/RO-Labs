package dao;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Bus implements Serializable {
    private String driverName;
    private int busNumber;
    private int rootNumber;
    private String brand;
    private int yearOfStartOfOperation;
    private int mileage;
    @Serial
    private static final long serialVersionUID = 1234567L;

    public Bus(String driverName, int busNumber, int rootNumber, String brand, int yearOfStartOfOperation, int mileage) {
        this.driverName = driverName;
        this.busNumber = busNumber;
        this.rootNumber = rootNumber;
        this.brand = brand;
        this.yearOfStartOfOperation = yearOfStartOfOperation;
        this.mileage = mileage;
    }
}
