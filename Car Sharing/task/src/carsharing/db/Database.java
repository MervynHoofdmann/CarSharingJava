package carsharing.db;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public Database() throws ClassNotFoundException, SQLException {

        try {
            String databaseName = "carsharing";
            DBConnection.connection = DriverManager.getConnection("jdbc:h2:./src/carsharing/db/" + databaseName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        DBConnection.connection.setAutoCommit(true);

        Class.forName("org.h2.Driver");

        Statement stmt = DBConnection.connection.createStatement();

        String dropOldCarTable = "DROP TABLE IF EXISTS car";
        String dropOldCompanyTable = "DROP TABLE IF EXISTS company";
        String createNewCompanyTable = "CREATE TABLE company (id INT AUTO_INCREMENT, name VARCHAR NOT NULL UNIQUE, PRIMARY KEY(id))";
        String createNewCarTable = "CREATE TABLE car (id INT AUTO_INCREMENT, name VARCHAR NOT NULL UNIQUE, company_id INT NOT NULL, CONSTRAINT fk_id FOREIGN KEY (company_id) REFERENCES company(id), PRIMARY KEY(id))";

        stmt.executeUpdate(dropOldCarTable);
        stmt.executeUpdate(dropOldCompanyTable);
        stmt.executeUpdate(createNewCompanyTable);
        stmt.executeUpdate(createNewCarTable);
    }

}
