package carsharing.db;

import carsharing.Main;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {
    private PreparedStatement preparedStatement;
    private final String addCustomerByName = "INSERT INTO customer (name) VALUES (?)";
    private final String getAllCustomers = "SELECT * FROM customer";
    private final String updateRentedCarId = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
    private final String returnRentedCar = "UPDATE customer SET rented_car_id = null WHERE id = ?";
    private final String getCustomerById = "SELECT * FROM customer WHERE id = ?";

    public Customer getCustomerById(int id) {
        ResultSet res;
        Customer customer = null;
        try {
            preparedStatement = DBConnection.connection.prepareStatement(getCustomerById);
            preparedStatement.setInt(1, id);
            res = preparedStatement.executeQuery();
            while (res.next()) {
                int rentedCarId = res.getInt("rented_car_id");
                boolean wasNull = res.wasNull();
                customer = new Customer(res.getInt("id"), res.getString("name"), wasNull ? null : rentedCarId);
            }
        } catch (SQLException e) {
            System.out.println("someting wong wiv getting customer by id");
        }
        return customer;
    }

    public void returnRentedCar(int id) {
        try {
            preparedStatement = DBConnection.connection.prepareStatement(returnRentedCar);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("someting wong wiv returning car");
            return;
        }
        System.out.println("You've returned a rented car!");
    }

    public void setRentedCarIdById(int rentedCarId, int id) {
        try {
            preparedStatement = DBConnection.connection.prepareStatement(updateRentedCarId);
            preparedStatement.setInt(1, rentedCarId);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("someting wong wiv updating rented car id");
            return;
        }
        for (Car car : Main.carDao.findAll()) {
            if (car.getId() == rentedCarId) {
                System.out.println("You rented '" + car.getName() + "'");
            }
        }
    }

    public List<Customer> findAll() {
        ArrayList<Customer> customers = new ArrayList<>();
        ResultSet res;
        try {
            preparedStatement = DBConnection.connection.prepareStatement(getAllCustomers);
            res = preparedStatement.executeQuery();
            while (res.next()) {
                int rentedCarId = res.getInt("rented_car_id");
                boolean wasNull = res.wasNull();
                Customer customer = new Customer(res.getInt("id"), res.getString("name"), wasNull ? null : rentedCarId);
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println("Someting wong wiv finding all customers");
        }
        return customers;
    }

    public void addByName(String n) {
        for (Customer customer : findAll()) {
            if (customer.getName().equals(n)) {
                System.out.println("Customer with that name already exists!");
                return;
            }
        }
        try {
            preparedStatement = DBConnection.connection.prepareStatement(addCustomerByName);
            preparedStatement.setString(1, n);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Someting wong wiv adding customer");
            return;
        }
        System.out.println("The customer was added!\n");
    }
}
