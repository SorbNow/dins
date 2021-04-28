package com.sorb.dins.service;

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
        return customerRepository.findAll();
    }

    @Override
    public Customer getById(long id) {
        return customerRepository.findById(id).isPresent() ? customerRepository.findById(id).get() : null;
    }

    @Override
    public void delete(long id) {
        contactService.deleteRelatedContacts(id);
        customerRepository.deleteById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer oldCustomer, Customer newCustomer) {
        if (isRequiresUpdate(oldCustomer.getFirstName(), newCustomer.getFirstName()))
            oldCustomer.setFirstName(newCustomer.getFirstName());

        if (isRequiresUpdate(oldCustomer.getLastName(), newCustomer.getLastName()))
            oldCustomer.setLastName(newCustomer.getLastName());

        return save(oldCustomer);
    }

    @Override
    public List<Customer> findCustomerByName(String name, boolean isRequiresLike) {
        return isRequiresLike ? customerRepository.findCustomersByLastNameIgnoreCaseContainsOrFirstNameIgnoreCaseContains(name, name)
                : customerRepository.findCustomersByLastNameIgnoreCaseOrFirstNameIgnoreCase(name, name);
    }

    private boolean isRequiresUpdate(String oldString, String newString) {
        return !oldString.equals(newString) && !newString.trim().isEmpty();
    }
}
