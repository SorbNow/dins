package com.sorb.dins.service;

import com.sorb.dins.model.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Contact service interface
 *
 * @author sorb
 */
@Service
public interface ContactService {

    /**
     * method for saving {@link Contact} object
     *
     * @param contact - {@link Contact} object which needs to be saved
     * @return saved object from database
     */
    Contact save(Contact contact);

    /**
     * search method for {@link Contact} object
     *
     * @param id - {@link Contact#id} which needs to be found
     * @return found contact from database
     */
    Contact getById(long id);

    /**
     * method for deleting {@link Contact} object
     *
     * @param id - {@link Contact#id} which needs to be removed
     */
    void delete(long id);

    /**
     * search method for {@link Contact} object
     *
     * @param customerId - {@link com.sorb.dins.model.Customer#id} by which need to find contacts
     * @return list with found contacts
     */
    List<Contact> getByCustomerId(long customerId);

    /**
     * method for updating {@link Contact}
     *
     * @param oldContact - contact by which need to be updated
     * @param newContact - contact from which new fields will be taken
     * @return updated contact from database
     */
    Contact updateContact(Contact oldContact, Contact newContact);

    /**
     * search method for {@link Contact} by {@link Contact#phoneNumber}
     * and {@link com.sorb.dins.model.Customer#id}
     *
     * @param phoneNumber - phone number by which need to find contacts
     * @param customerId  - customer id whose contacts are being searched
     * @return contact from database
     */
    List<Contact> findContactByPhoneNumber(long phoneNumber, long customerId);

}
