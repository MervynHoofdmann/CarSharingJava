package carsharing.db;

import carsharing.Main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Statement stmt;

    static {
        try {
            stmt = DBConnection.connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String companyId = "";

    public void menu() throws SQLException {
        MenuState menu = MenuState.START;

        while (menu != MenuState.EXIT) menu = switch (menu) {
            case START -> startMenuTransition(menu);
            case MANAGER -> managerMenuTransition(menu);
            case COMPANY_LIST -> companyListMenuTransition();
            case CREATE_COMPANY -> createCompanyMenuTransition();
            case CAR_MANAGER -> carManagerMenuTransition(menu);
            case CAR_LIST -> carListMenuTransition();
            case CREATE_CAR -> createCarMenuTransition();
            default -> menu;
        };
    }

    private MenuState createCarMenuTransition() throws SQLException {
        MenuState menu;
        System.out.println("Enter the car name:");
        String input = scanner.nextLine();
        String addCar = "INSERT INTO car (name, company_id) VALUES ('" + input + "', " + companyId + ")";
        String getCar = "SELECT * FROM car WHERE name = '" + input + "'";
        try {
            stmt.executeUpdate(addCar);
        } catch (SQLException e) {
            System.out.println("Error! Car already exists\n");
            menu = MenuState.CAR_MANAGER;
            return menu;
        }
        System.out.println("The car was added!\n");
        ResultSet res = stmt.executeQuery(getCar);
        res.next();
        Car car = new Car(res.getInt(1), res.getString(2), Integer.parseInt(companyId));
        Main.carDao.add(car);
        menu = MenuState.CAR_MANAGER;
        return menu;
    }

    private MenuState carListMenuTransition() throws SQLException {
        MenuState menu;
        String showCarList = "SELECT id, name FROM car WHERE company_id = " + companyId;
        ResultSet res = stmt.executeQuery(showCarList);
        if (!res.next()) {
            System.out.println("The car list is empty!\n");
            menu = MenuState.CAR_MANAGER;
            return menu;
        }
        // int i needs to be adjusted, does not always equal the id from the database entry but does get the job done for stage 3/4
        int i = 1;
        for (Car car : Main.carDao.cars) {
            if (car.getCompany_id() == Integer.parseInt(companyId)) {
                System.out.println(i + ". " + car.getName());
                i++;
            }
        }
        return MenuState.CAR_MANAGER;
    }

    private MenuState carManagerMenuTransition(MenuState menu) {
        System.out.println("1. Car list\n2. Create a car\n0. Back");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> menu = MenuState.CAR_LIST;
            case "2" -> menu = MenuState.CREATE_CAR;
            case "0" -> menu = MenuState.MANAGER;
            default -> System.out.println("Error! Incompatible input");
        }
        return menu;
    }

    private static MenuState createCompanyMenuTransition() throws SQLException {
        MenuState menu;
        System.out.println("Enter the company name:");
        String input = scanner.nextLine();
        String addCompany = "INSERT INTO company (name) VALUES ('" + input + "')";
        String getCompany = "SELECT * FROM company WHERE name = '" + input + "'";
        try {
            stmt.executeUpdate(addCompany);
        } catch (SQLException e) {
            System.out.println("Error! Company already exists\n");
            menu = MenuState.MANAGER;
            return menu;
        }
        System.out.println("The company was created!\n");
        ResultSet res = stmt.executeQuery(getCompany);
        res.next();
        Company company = new Company(res.getInt(1), res.getString(2));
        Main.companyDao.add(company);
        menu = MenuState.MANAGER;
        return menu;
    }

    private static MenuState companyListMenuTransition() throws SQLException {
        MenuState menu;
        String showCompanyList = "SELECT * FROM company";
        String showCompany;
        ResultSet res = stmt.executeQuery(showCompanyList);
        if (!res.next()) {
            System.out.println("The company list is empty!\n");
            menu = MenuState.MANAGER;
            return menu;
        }
        System.out.println("Choose a company:");
        // int i needs to be adjusted, does not always equal the id from the database entry but does get the job done for stage 3/4
        int i = 1;
        for (Company company : Main.companyDao.companies) {
            System.out.println(i + ". " + company.getName());
            i++;
        }
        System.out.println("0. Back");
        companyId = scanner.nextLine();
        if (companyId.equals("0")) {
            menu = MenuState.MANAGER;
            return menu;
        }
        showCompany = "SELECT * FROM company WHERE id = '" + companyId + "'";
        ResultSet resCompany = stmt.executeQuery(showCompany);
        if (!resCompany.next()) {
            System.out.println("No company found for that id!\n");
            menu = MenuState.MANAGER;
            return menu;
        }
        String getCompanyName = "SELECT name FROM company WHERE id = " + companyId;
        ResultSet resCompanyName = stmt.executeQuery(getCompanyName);
        resCompanyName.next();
        System.out.println("'" + resCompanyName.getString(1) + "' company");
        menu = MenuState.CAR_MANAGER;
        return menu;
    }

    private static MenuState startMenuTransition(MenuState menu) {
        System.out.print("1. Log in as a manager\n0. Exit\n");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> menu = MenuState.MANAGER;
            case "0" -> menu = MenuState.EXIT;
            default -> System.out.println("Error! Incompatible input");
        }
        return menu;
    }

    private static MenuState managerMenuTransition(MenuState menu) {
        System.out.print("1. Company list\n2. Create a company\n0. Back\n");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> menu = MenuState.COMPANY_LIST;
            case "2" -> menu = MenuState.CREATE_COMPANY;
            case "0" -> menu = MenuState.START;
            default -> System.out.println("Error! Incompatible input");
        }
        return menu;
    }
}
