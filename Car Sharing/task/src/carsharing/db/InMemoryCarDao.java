package carsharing.db;

import java.util.ArrayList;
import java.util.List;

public class InMemoryCarDao implements CarDao{
    private final List<Car> cars;

    public InMemoryCarDao() {
        this.cars = new ArrayList<>();
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public Car findById(int id) {
        Car found = findByIdInternal(id);
        if (found == null) {
            System.out.println("Car: Id " + id + ", not found");
            return null;
        }
        System.out.println("Car: Id " + id + ", found");
        return new Car(found.getId(), found.getName(), found.getCompany_id());
    }

    @Override
    public void add(Car car) {
        cars.add(car);
        System.out.println("Car: Id " + car.getId() + ", name: " + car.getName() + " added");
    }

    @Override
    public void update(Car car) {
        Car found = findByIdInternal(car.getId());
        if (found != null) {
            found.setName(car.getName());
            System.out.println("Car: Id " + car.getId() + ", updated");
        } else {
            System.out.println("Car: Id " + car.getId() + ", not found");
        }
    }

    @Override
    public void deleteById(int id) {
        Car found = findByIdInternal(id);
        if (found != null) {
            cars.remove(found);
            System.out.println("Car: Id" + id + ", deleted");
        } else {
            System.out.println("Car: Id" + id + ", not found");
        }
    }

    private Car findByIdInternal(int id) {
        for (Car car : cars) {
            if (id == car.getId()) {
                return car;
            }
        }
        return null;
    }
}
