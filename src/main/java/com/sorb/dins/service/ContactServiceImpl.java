package com.sorb.dins.service;

import com.sorb.dins.exception.ExistsInDatabaseException;
import com.sorb.dins.exception.NotFoundInDatabaseException;
import com.sorb.dins.model.Contact;
import com.sorb.dins.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public Contact save(Contact contact) {
        if (contact.getId() != 0 && contactRepository.findById(contact.getId()).isPresent())
            throw new ExistsInDatabaseException("Contact with id: " + contact.getId() + " already exists in database." +
                    " For update use PUT ");

        return contactRepository.save(contact);
    }

    @Override
    public Contact getById(long id) {
        checkContactIsPresentInDatabase(id);
        return contactRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        checkContactIsPresentInDatabase(id);
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> getByCustomerId(long customerId) {
        List<Contact> contactList = contactRepository.findContactsByCustomer_Id(customerId);

        if (contactList.isEmpty())
            throw new NotFoundInDatabaseException("Contacts with customer id: " + customerId + " not found in database");

        return contactList;
    }

    @Override
    public Contact updateContact(Contact oldContact, Contact newContact) {

        if (isRequiresUpdate(oldContact.getFirstName(), newContact.getFirstName()))
            oldContact.setFirstName(newContact.getFirstName());

        if (isRequiresUpdate(oldContact.getLastName(), newContact.getLastName()))
            oldContact.setLastName(newContact.getLastName());

        if (newContact.getPhoneNumber()>0 && newContact.getPhoneNumber() != 0 && oldContact.getPhoneNumber() != newContact.getPhoneNumber())
            oldContact.setPhoneNumber(newContact.getPhoneNumber());

        return contactRepository.save(oldContact);
    }

    @Override
    public List<Contact> findContactByPhoneNumber(long phoneNumber, long customerId) {

        List<Contact> contactList = contactRepository.findContactsByPhoneNumberAndCustomer_Id(phoneNumber, customerId);

        if (contactList.isEmpty())
            throw new NotFoundInDatabaseException("Contacts with phone number: " + phoneNumber + " and customer id: "
                    + customerId + " not found in database");

        return contactList;
    }

    private boolean isRequiresUpdate(String oldString, String newString) {
        return newString!=null && !oldString.equals(newString) && !newString.trim().isEmpty();
    }

    public void deleteRelatedContacts(long customerId) {
        for (Contact c : contactRepository.findContactsByCustomer_Id(customerId))
            contactRepository.deleteById(c.getId());
    }

    private void checkContactIsPresentInDatabase(long id) {
        if (!contactRepository.findById(id).isPresent())
            throw new NotFoundInDatabaseException("Contact with id: " + id + " not found in database");
    }
}
