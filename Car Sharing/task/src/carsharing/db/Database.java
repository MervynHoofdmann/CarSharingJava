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
        String dropOldTable = "DROP TABLE IF EXISTS COMPANY";
        String createNewTable = "CREATE TABLE COMPANY (ID INT AUTO_INCREMENT, NAME VARCHAR NOT NULL UNIQUE, PRIMARY KEY(ID))";
        stmt.executeUpdate(dropOldTable);
        stmt.executeUpdate(createNewTable);
    }

}
