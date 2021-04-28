package com.sorb.dins.service;

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
        return contactRepository.save(contact);
    }

    @Override
    public Contact getById(long id) {
        return contactRepository.findById(id).isPresent() ? contactRepository.findById(id).get() : null;
    }

    @Override
    public void delete(long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public List<Contact> getByCustomerId(long customerId) {
        return contactRepository.findContactsByCustomer_Id(customerId);
    }

    @Override
    public Contact updateContact(Contact oldContact, Contact newContact) {
        if (isRequiresUpdate(oldContact.getFirstName(), newContact.getFirstName()))
            oldContact.setFirstName(newContact.getFirstName());

        if (isRequiresUpdate(oldContact.getLastName(), newContact.getLastName()))
            oldContact.setLastName(newContact.getLastName());

        if (oldContact.getPhoneNumber() != newContact.getPhoneNumber() && newContact.getPhoneNumber() != 0)
            oldContact.setPhoneNumber(newContact.getPhoneNumber());

        return save(oldContact);
    }

    @Override
    public List<Contact> findContactByPhoneNumber(long phoneNumber, long customerId) {
        return contactRepository.findContactsByPhoneNumberAndCustomer_Id(phoneNumber, customerId);
    }

    private boolean isRequiresUpdate(String oldString, String newString) {
        return !oldString.equals(newString) && !newString.trim().isEmpty();
    }

    public void deleteRelatedContacts(long customerId) {
        for (Contact c : contactRepository.findContactsByCustomer_Id(customerId))
            contactRepository.deleteById(c.getId());
    }
}
