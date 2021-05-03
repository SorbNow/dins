package com.sorb.dins.repository;

import com.sorb.dins.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Contact repository class
 * CRUD and generated methods for DB implements from {@link JpaRepository}
 *
 * @author sorb
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * method for finding all {@link Contact} for each {@link com.sorb.dins.model.Customer}
     *
     * @param id - customer id from table {@link com.sorb.dins.model.Customer}
     * @return list of contacts for given customer id
     */
    List<Contact> findContactsByCustomer_Id(Long id);

    /**
     * method for finding all {@link Contact} with {@link Contact#phoneNumber} for each
     * {@link com.sorb.dins.model.Customer}
     *
     * @param id          - customer id from table {@link com.sorb.dins.model.Customer}
     * @param phoneNumber - required phone number
     * @return list of contacts for given customer id
     */
    List<Contact> findContactsByPhoneNumberAndCustomer_Id(long phoneNumber, long id);

}
