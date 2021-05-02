package com.sorb.dins.service;

import com.sorb.dins.exception.ExistsInDatabaseException;
import com.sorb.dins.exception.NotFoundInDatabaseException;
import com.sorb.dins.model.Customer;
import com.sorb.dins.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final ContactServiceImpl contactService;

    public CustomerServiceImpl(CustomerRepository customerRepository, ContactServiceImpl contactService) {
        this.customerRepository = customerRepository;
        this.contactService = contactService;
    }

    @Override
    public List<Customer> getAll() {
        List<Customer> customerList = customerRepository.findAll();

        if (customerList.isEmpty())
            throw new NotFoundInDatabaseException("Customers table is empty");

        return customerList;
    }

    @Override
    public Customer getById(long id) {
        checkCustomerIsPresentInDatabase(id);
        return customerRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        checkCustomerIsPresentInDatabase(id);
        contactService.deleteRelatedContacts(id);
        customerRepository.deleteById(id);
    }

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() != 0 && customerRepository.findById(customer.getId()).isPresent())
            throw new ExistsInDatabaseException("Customer with id: " + customer.getId() + " already exists in database." +
                    " For update use PUT ");
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer oldCustomer, Customer newCustomer) {
        if (isRequiresUpdate(oldCustomer.getFirstName(), newCustomer.getFirstName()))
            oldCustomer.setFirstName(newCustomer.getFirstName());

        if (isRequiresUpdate(oldCustomer.getLastName(), newCustomer.getLastName()))
            oldCustomer.setLastName(newCustomer.getLastName());

        return customerRepository.save(oldCustomer);
    }

    @Override
    public List<Customer> findCustomerByName(String name, boolean isRequiresLike) {
        List<Customer> customerList = isRequiresLike ? customerRepository.findCustomersByLastNameIgnoreCaseContainsOrFirstNameIgnoreCaseContains(name, name)
                : customerRepository.findCustomersByLastNameIgnoreCaseOrFirstNameIgnoreCase(name, name);

        if (customerList.isEmpty())
            throw new NotFoundInDatabaseException("Customers with name " + name + " not found in database");

        return customerList;
    }

    private boolean isRequiresUpdate(String oldString, String newString) {
        return newString!=null && !oldString.equals(newString) && !newString.trim().isEmpty();
    }

    private void checkCustomerIsPresentInDatabase(long id) {
        if (!customerRepository.findById(id).isPresent())
            throw new NotFoundInDatabaseException("Customer with id: " + id + " not found in database");
    }
}
