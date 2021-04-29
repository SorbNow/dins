package com.sorb.dins.service;

import com.sorb.dins.model.Contact;
import com.sorb.dins.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contact service interface
 *
 * @author sorb
 */
@Service
public interface CustomerService {

    /**
     * search method for {@link Customer} object
     * @return list of all customers and their contacts from database
     */
    List<Customer> getAll();

    /**
     * search method for {@link Customer} object
     * @param id - {@link Customer#id} which needs to be found
     * @return found customer from database
     */
    Customer getById(long id);

    /**
     * method for deleting {@link Customer} object
     * @param id - {@link Customer#id} which needs to be removed
     */
    void delete(long id);

    /**
     * method for saving {@link Customer} object
     * @param customer - {@link Customer} object which needs to be saved
     * @return saved object from database
     */
    Customer save(Customer customer);

    /**
     * method for updating {@link Customer}
     * @param oldCustomer - customer by which need to be updated
     * @param newCustomer - customer from which new fields will be taken
     * @return updated customer from database
     */
    Customer updateCustomer(Customer oldCustomer, Customer newCustomer);

    /**
     * search method for {@link Customer} by {@link Customer#firstName} or {@link Customer#lastName}
     * @param name - first or last name by which need to find customers
     * @param isRequiresLike - boolean value. Set "true" if want to search for a part of name
     * @return  list of customers from database
     */
    List<Customer> findCustomerByName(String name, boolean isRequiresLike);
}
