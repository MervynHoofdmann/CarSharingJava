package carsharing.db;

import java.util.List;

public interface CarDao {
    List<Car> findAll();
    Car findById(int id);
    void add(Car car);
    void update(Car car);
    void deleteById(int id);
}
