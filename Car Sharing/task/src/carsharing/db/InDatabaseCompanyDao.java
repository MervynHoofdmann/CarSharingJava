package carsharing.db;

import java.util.ArrayList;
import java.util.List;

public class InDatabaseCompanyDao implements CompanyDao {
    public List<Company> companies;

    public InDatabaseCompanyDao() {
        this.companies = new ArrayList<>();
    }

    @Override
    public List<Company> findAll() {
        return new ArrayList<>(companies);
    }

    @Override
    public Company findById(int id) {
        Company found = findByIdInternal(id);
        if (found == null) {
            System.out.println("Company: Id " + id + ", not found");
            return null;
        }
        System.out.println("Company: Id " + id + ", found");
        return new Company(found.getId(), found.getName());
    }

    @Override
    public void add(Company company) {
        companies.add(company);
    }

    @Override
    public void update(Company company) {
        Company found = findByIdInternal(company.getId());
        if (found != null) {
            found.setName(company.getName());
            System.out.println("Company: Id " + company.getId() + ", updated");
        } else {
            System.out.println("Company: Id " + company.getId() + ", not found");
        }
    }

    @Override
    public void deleteById(int id) {
        Company found = findByIdInternal(id);
        if (found != null) {
            companies.remove(found);
            System.out.println("Company: Id" + id + ", deleted");
        } else {
            System.out.println("Company: Id" + id + ", not found");
        }
    }

    private Company findByIdInternal(int id) {
        for (Company company : companies) {
            if (id == company.getId()) {
                return company;
            }
        }
        return null;
    }
}
