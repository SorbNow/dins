package com.example.dins.service;

import com.example.dins.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    List<Customer> getAll();

    Customer getById(long id);

    void delete(long id);

    Customer save(Customer customer);

    Customer updateCustomer(Customer oldCustomer, Customer newCustomer);

    List<Customer> findCustomerByName(String name, boolean isRequiresLike);
}
