package carsharing.db;

public class Car {
    private int id;
    private String name;
    private int company_id;

    public Car(int id, String name, int company_id) {
        this.id = id;
        this.name = name;
        this.company_id = company_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompany_id(int company_id) {
        this.company_id = company_id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCompany_id() {
        return company_id;
    }
}
