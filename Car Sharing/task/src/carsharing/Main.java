package carsharing;

import carsharing.db.*;

import java.sql.SQLException;

public class Main {
    public static InDatabaseCompanyDao companyDao = new InDatabaseCompanyDao();

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Database database = new Database();

        Menu menu = new Menu();
        menu.menu();
        DBConnection.connection.close();
        System.exit(0);
    }


}