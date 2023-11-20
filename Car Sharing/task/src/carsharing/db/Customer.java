package carsharing.db;

public class Customer {
    private int id;
    private String name;
    private Integer rentedCarId;
    public Customer(int i, String n, Integer r){
        id = i;
        name = n;
        rentedCarId = r;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getRentedCarId() {
        return rentedCarId;
    }

    public void setRentedCarId(Integer rentedCarId) {
        this.rentedCarId = rentedCarId;
    }
}
