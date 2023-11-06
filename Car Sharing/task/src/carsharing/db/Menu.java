package carsharing.db;

import carsharing.Main;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Menu {
    public void menu() throws SQLException {
        MenuState menu = MenuState.START;
        Scanner scanner = new Scanner(System.in);
        String input;
        Statement stmt = DBConnection.connection.createStatement();
        while (menu != MenuState.EXIT)
            switch (menu) {
                case START:
                    menu = startMenuTransition(scanner, menu);
                    break;
                case MANAGER:
                    menu = managerMenuTransition(scanner, menu);
                    break;
                case COMPANY_LIST:
                    String showList = "SELECT * FROM COMPANY";
                    var res = stmt.executeQuery(showList);
                    if (!res.next()) {
                        System.out.println("The company list is empty!\n");
                        menu = MenuState.MANAGER;
                        break;
                    }
                    System.out.println("Company list:");
                    do  {
                        Company company = new Company(res.getInt(1), res.getString(2));
                        Main.companyDao.add(company);
                    } while (res.next());
                    System.out.println();
                    menu = MenuState.MANAGER;
                    break;
                case CREATE_COMPANY:
                    System.out.println("Enter the company name:");
                    input = scanner.nextLine();
                    String addCompany = "INSERT INTO COMPANY (NAME) VALUES ('" + input + "')";
                    stmt.executeUpdate(addCompany);
                    System.out.println("The company was created!\n");
                    menu = MenuState.MANAGER;
                    break;
            }
    }

    private static MenuState startMenuTransition(Scanner scanner, MenuState menu) {
        String input;
        System.out.print("1. Log in as a manager\n0. Exit\n");
        input = scanner.nextLine();
        switch (input) {
            case "1" -> menu = MenuState.MANAGER;
            case "0" -> menu = MenuState.EXIT;
            default -> System.out.println("Error! Incompatible input");
        }
        return menu;
    }

    private static MenuState managerMenuTransition(Scanner scanner, MenuState menu) {
        String input;
        System.out.print("1. Company list\n2. Create a company\n0. Back\n");
        input = scanner.nextLine();
        switch (input) {
            case "1" -> menu = MenuState.COMPANY_LIST;
            case "2" -> menu = MenuState.CREATE_COMPANY;
            case "0" -> menu = MenuState.START;
            default -> System.out.println("Error! Incompatible input");
        }
        return menu;
    }
}
