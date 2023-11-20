package carsharing.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public Database() throws ClassNotFoundException, SQLException {

        try {
            DBConnection.connection = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/carsharing");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.connection.setAutoCommit(true);

        Class.forName("org.h2.Driver");

        Statement stmt = DBConnection.connection.createStatement();
        String createNewCompanyTable = "CREATE TABLE IF NOT EXISTS company "
                + "(id INT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR UNIQUE NOT NULL)";
        String createNewCarTable = "CREATE TABLE IF NOT EXISTS car "
                + "(id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR UNIQUE NOT NULL, company_id INT NOT NULL, "
                + "CONSTRAINT fk_company FOREIGN KEY (company_id) REFERENCES company(id))";
        String createNewCustomerTable = "CREATE TABLE IF NOT EXISTS customer "
                + "(id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR UNIQUE NOT NULL, rented_car_id INT,"
                + "CONSTRAINT fk_car FOREIGN KEY (rented_car_id) REFERENCES car(id))";

        stmt.executeUpdate(createNewCompanyTable);
        stmt.executeUpdate(createNewCarTable);
        stmt.executeUpdate(createNewCustomerTable);
    }

}
