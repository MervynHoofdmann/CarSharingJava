package carsharing.db;

public class Car {
    private int id;
    private String name;
    private int companyId;

    public Car(int i, String n, int c) {
        id = i;
        name = n;
        companyId = c;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompanyId() {
        return companyId;
    }
}
