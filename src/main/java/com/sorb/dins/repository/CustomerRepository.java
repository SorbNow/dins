package com.sorb.dins.repository;

import com.sorb.dins.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Customer repository class
 * CRUD and generated methods for DB implements from {@link JpaRepository}
 *
 * @author sorb
 */

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * method for searching all {@link Customer} by full first and last name
     *
     * @param firstName - customer first name from table {@link Customer}
     * @param lastName  - customer last name from table {@link Customer}
     * @return list of customers with given first and last name
     */
    List<Customer> findCustomersByLastNameIgnoreCaseOrFirstNameIgnoreCase(String firstName, String lastName);

    /**
     * method for searching all {@link Customer} by part of first and last name
     *
     * @param name     - customer first name from table {@link Customer}
     * @param lastName - customer last name from table {@link Customer}
     * @return list of customers with given part of first and last name
     */
    List<Customer> findCustomersByLastNameIgnoreCaseContainsOrFirstNameIgnoreCaseContains(String name, String lastName);
}
