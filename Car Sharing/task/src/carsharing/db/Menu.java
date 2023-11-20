package carsharing.db;

import carsharing.Main;

import java.util.HashMap;
import java.util.Scanner;

public class Menu {
    private final static Scanner scanner = new Scanner(System.in);
    private static Company activeCompany;
    private static Customer activeCustomer;

    public void menu() {
        MenuState menu = MenuState.START;
        while (menu != MenuState.EXIT) menu = switch (menu) {
            case START -> startMenuTransition();
            case MANAGER -> managerMenuTransition();
            case COMPANY_LIST -> companyListMenuTransition();
            case CREATE_COMPANY -> createCompanyMenuTransition();
            case COMPANY_MENU -> companyMenuMenuTransition();
            case CREATE_CAR -> createCarMenuTransition();
            case CAR_LIST -> carListMenuTransition();
            case CUSTOMER_LIST -> customerListMenuTransition();
            case CREATE_CUSTOMER -> createCustomerMenuTransition();
            case CUSTOMER_MENU -> customerMenuMenuTransition();
            case RENT_CAR -> rentCarMenuTransition();
            case RETURN_CAR -> returnCarMenuTransition();
            case SHOW_RENTED_CAR -> showRentedCarMenuTransition();
            default -> menu;
        };
    }

    private MenuState showRentedCarMenuTransition() {
        if (activeCustomer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return MenuState.CUSTOMER_MENU;
        }
        for (Car car : Main.carDao.findAll()) {
            if (car.getId() == activeCustomer.getRentedCarId()) {
                System.out.println(car.getName());
            }
        }
        System.out.println("Company:\n" + activeCompany.getName());
        return MenuState.CUSTOMER_MENU;
    }

    private MenuState returnCarMenuTransition() {
        if (activeCustomer.getRentedCarId() == null) {
            System.out.println("You didn't rent a car!");
            return MenuState.CUSTOMER_MENU;
        }
        Main.customerDao.returnRentedCar(activeCustomer.getId());
        activeCustomer.setRentedCarId(null);
        return MenuState.CUSTOMER_MENU;
    }

    private MenuState rentCarMenuTransition() {
        HashMap<Integer, Company> inMenuCompanies = new HashMap<>();
        HashMap<Integer, Car> inMenuCars = new HashMap<>();
        if (Main.companyDao.findAll().isEmpty()) {
            System.out.println("The company list is empty!");
            return MenuState.CUSTOMER_MENU;
        }
        if (activeCustomer.getRentedCarId() != null) {
            System.out.println("You've already rented a car!");
            return MenuState.CUSTOMER_MENU;
        }
        System.out.println("Choose a company:");
        int menuId = 1;
        for (Company company : Main.companyDao.findAll()) {
            inMenuCompanies.put(menuId, company);
            System.out.println(menuId++ + ". " + company.getName());
        }
        System.out.println("0. Back");
        try {
            int menuInput = Integer.parseInt(scanner.nextLine());
            if (menuInput == 0) {
                return MenuState.CUSTOMER_MENU;
            } else if (inMenuCompanies.containsKey(menuInput)) {
                activeCompany = inMenuCompanies.get(menuInput);
            } else {
                System.out.println("Error! Incompatible input");
                return MenuState.CUSTOMER_MENU;
            }
        } catch (NumberFormatException e) {
            System.out.println("Error! Incompatible input");
            return MenuState.CUSTOMER_MENU;
        }
        if (Main.carDao.findAvailableCarsByCompany(activeCompany.getId()).isEmpty()) {
            System.out.println("No available cars in the '" + activeCompany.getName() + "' company");
            return MenuState.CUSTOMER_MENU;
        }
        System.out.println("Choose a car:");
        menuId = 1;
        for (Car car : Main.carDao.findAvailableCarsByCompany(activeCompany.getId())) {
            inMenuCars.put(menuId, car);
            System.out.println(menuId++ + ". " + car.getName());
        }
        System.out.println("0. Back");
        int menuInput;
        try {
            menuInput = Integer.parseInt(scanner.nextLine());
            if (menuInput == 0) {
                return MenuState.CUSTOMER_MENU;
            }
            if (inMenuCars.containsKey(menuInput)) {
                Main.customerDao.setRentedCarIdById(inMenuCars.get(menuInput).getId(), activeCustomer.getId());
                activeCustomer = Main.customerDao.getCustomerById(activeCustomer.getId());
                return MenuState.CUSTOMER_MENU;
            }
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Error! Incompatible input");
        return MenuState.CUSTOMER_MENU;

    }

    private MenuState customerMenuMenuTransition() {
        System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
        int menuInput;
        try {
            menuInput = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error! Incompatible input");
            return MenuState.CUSTOMER_MENU;
        }
        switch (menuInput) {
            case 1 -> {
                return MenuState.RENT_CAR;
            }
            case 2 -> {
                return MenuState.RETURN_CAR;
            }
            case 3 -> {
                return MenuState.SHOW_RENTED_CAR;
            }
            case 0 -> {
                return MenuState.START;
            }
            default -> System.out.println("Error! Incompatible input");
        }
        return MenuState.START;
    }

    private MenuState createCustomerMenuTransition() {
        System.out.println("Enter the customer name:");
        Main.customerDao.addByName(scanner.nextLine());
        return MenuState.START;
    }

    private MenuState customerListMenuTransition() {
        HashMap<Integer, Customer> inMenuCustomers = new HashMap<>();
        if (Main.customerDao.findAll().isEmpty()) {
            System.out.println("The customer list is empty!");
            return MenuState.START;
        }
        System.out.println("Customer list:");
        int menuId = 1;
        for (Customer customer : Main.customerDao.findAll()) {
            inMenuCustomers.put(menuId, customer);
            System.out.println(menuId++ + ". " + customer.getName());
        }
        System.out.println("0. Back");
        try {
            int menuInput = Integer.parseInt(scanner.nextLine());
            if (menuInput == 0) {
                return MenuState.START;
            }
            if (inMenuCustomers.containsKey(menuInput)) {
                activeCustomer = inMenuCustomers.get(menuInput);
                return MenuState.CUSTOMER_MENU;
            }
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Error! Incompatible input");
        return MenuState.START;
    }

    private MenuState carListMenuTransition() {
        int inCompanyCars = 0;
        for (Car car : Main.carDao.findAll()) {
            if (car.getCompanyId() == activeCompany.getId()) {
                inCompanyCars += 1;
            }
        }
        if (inCompanyCars == 0) {
            System.out.println("The car list is empty!\n");
            return MenuState.COMPANY_MENU;
        }
        int menuId = 1;
        for (Car car : Main.carDao.findAll()) {
            if (car.getCompanyId() == activeCompany.getId()) {
                System.out.println(menuId++ + ". " + car.getName());
            }
        }
        System.out.println();
        return MenuState.COMPANY_MENU;
    }

    private MenuState companyMenuMenuTransition() {
        System.out.println("1. Car list\n2. Create a car\n0. Back");
        int menuInput;
        try {
            menuInput = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error! Incompatible input");
            return MenuState.MANAGER;
        }
        switch (menuInput) {
            case 1 -> {
                return MenuState.CAR_LIST;
            }
            case 2 -> {
                return MenuState.CREATE_CAR;
            }
            case 0 -> {
                return MenuState.MANAGER;
            }
            default -> System.out.println("Error! Incompatible input");
        }
        return MenuState.MANAGER;
    }

    private static MenuState createCarMenuTransition() {
        System.out.println("Enter the car name:");
        Main.carDao.addByNameAndCompanyId(scanner.nextLine(), activeCompany.getId());
        return MenuState.COMPANY_MENU;
    }

    private static MenuState createCompanyMenuTransition() {
        System.out.println("Enter the company name:");
        Main.companyDao.addByName(scanner.nextLine());
        return MenuState.MANAGER;
    }

    private static MenuState companyListMenuTransition() {
        HashMap<Integer, Company> inMenuCompanies = new HashMap<>();
        if (Main.companyDao.findAll().isEmpty()) {
            System.out.println("The company list is empty!\n");
            return MenuState.MANAGER;
        }
        System.out.println("Choose a company:");
        int menuId = 1;
        for (Company company : Main.companyDao.findAll()) {
            inMenuCompanies.put(menuId, company);
            System.out.println(menuId++ + ". " + company.getName());
        }
        System.out.println("0. Back");
        try {
            int menuInput = Integer.parseInt(scanner.nextLine());
            if (menuInput == 0) {
                return MenuState.MANAGER;
            }
            if (inMenuCompanies.containsKey(menuInput)) {
                activeCompany = inMenuCompanies.get(menuInput);
                System.out.println("'" + activeCompany.getName() + "' company");
                return MenuState.COMPANY_MENU;
            }
        } catch (NumberFormatException ignored) {
        }
        System.out.println("Error! Incompatible input");
        return MenuState.MANAGER;
    }

    private static MenuState startMenuTransition() {
        System.out.print("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit\n");
        int menuInput;
        try {
            menuInput = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error! Incompatible input");
            return MenuState.START;
        }
        switch (menuInput) {
            case 1 -> {
                return MenuState.MANAGER;
            }
            case 2 -> {
                return MenuState.CUSTOMER_LIST;
            }
            case 3 -> {
                return MenuState.CREATE_CUSTOMER;
            }
            case 0 -> {
                return MenuState.EXIT;
            }
            default -> System.out.println("Error! Incompatible input");
        }
        return MenuState.START;
    }

    private static MenuState managerMenuTransition() {
        System.out.print("1. Company list\n2. Create a company\n0. Back\n");
        int menuInput;
        try {
            menuInput = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Error! Incompatible input");
            return MenuState.MANAGER;
        }
        switch (menuInput) {
            case 1 -> {
                return MenuState.COMPANY_LIST;
            }
            case 2 -> {
                return MenuState.CREATE_COMPANY;
            }
            case 0 -> {
                return MenuState.START;
            }
            default -> System.out.println("Error! Incompatible input");
        }
        return MenuState.MANAGER;
    }
}
