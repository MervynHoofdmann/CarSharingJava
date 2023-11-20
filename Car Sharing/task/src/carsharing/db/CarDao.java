package carsharing.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarDao {
    private PreparedStatement preparedStatement;
    private final String addCarByNameAndCompanyId = "INSERT INTO car (name, company_id) VALUES (?,?)";
    private final String getAllCars = "SELECT * FROM car";
    private final String getAvailableCarsByCompany = "SELECT * FROM car WHERE company_id = ? "
            + "AND NOT EXISTS (SELECT * FROM customer WHERE rented_car_id = car.id)";

    public List<Car> findAvailableCarsByCompany(int i) {
        ArrayList<Car> cars = new ArrayList<>();
        ResultSet res;
        try {
            preparedStatement = DBConnection.connection.prepareStatement(getAvailableCarsByCompany);
            preparedStatement.setInt(1, i);
            res = preparedStatement.executeQuery();
            while (res.next()) {
                Car car = new Car(res.getInt("id"), res.getString("name"), res.getInt("company_id"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.out.println("Someting wong wiv finding available cars from database");
        }
        return cars;
    }

    public List<Car> findAll() {
        ArrayList<Car> cars = new ArrayList<>();
        ResultSet res;
        try {
            preparedStatement = DBConnection.connection.prepareStatement(getAllCars);
            res = preparedStatement.executeQuery();
            while (res.next()) {
                Car car = new Car(res.getInt("id"), res.getString("name"), res.getInt("company_id"));
                cars.add(car);
            }
        } catch (SQLException e) {
            System.out.println("Someting wong wiv finding all cars from database");
        }
        return cars;
    }

    public void addByNameAndCompanyId(String n, int i) {
        for (Car car : findAll()) {
            if (car.getName().equals(n)) {
                System.out.println("Car with that name already exists!");
                return;
            }
        }
        try {
            preparedStatement = DBConnection.connection.prepareStatement(addCarByNameAndCompanyId);
            preparedStatement.setString(1, n);
            preparedStatement.setInt(2, i);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The car was added!\n");
    }
}
