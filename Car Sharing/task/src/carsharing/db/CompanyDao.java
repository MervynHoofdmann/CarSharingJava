package carsharing.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDao {
    private PreparedStatement preparedStatement;
    private String addCompanyByName = "INSERT INTO company (name) VALUES (?)";
    private String getAllCompanies = "SELECT * FROM company";

    public List<Company> findAll() {
        ArrayList<Company> companies = new ArrayList<>();
        ResultSet res;
        try {
            preparedStatement = DBConnection.connection.prepareStatement(getAllCompanies);
            res = preparedStatement.executeQuery();
            while (res.next()) {
                Company company = new Company(res.getInt("id"), res.getString("name"));
                companies.add(company);
            }
        } catch (SQLException e) {
            System.out.println("Someting wong wiv finding all companies from database");
        }
        return companies;
    }

    public void addByName(String n) {
        for (Company company : findAll()) {
            if (company.getName().equals(n)) {
                System.out.println("Company with that name already exists!");
                return;
            }
        }
        try {
            preparedStatement = DBConnection.connection.prepareStatement(addCompanyByName);
            preparedStatement.setString(1, n);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("The company was created!\n");
    }
}
