package com.sorb.dins.repository;

import com.sorb.dins.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findCustomersByLastNameIgnoreCaseOrFirstNameIgnoreCase(String firstName, String lastName);

    List<Customer> findCustomersByLastNameIgnoreCaseContainsOrFirstNameIgnoreCaseContains(String name, String lastName);
}
