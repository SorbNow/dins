package com.example.dins.service;

import com.example.dins.model.Contact;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContactService {

    Contact save(Contact contact);

    Contact getById(long id);

    void delete(long id);

    List<Contact> getByCustomerId(long customerId);

    Contact updateContact(Contact oldContact, Contact newContact);

    List<Contact> findContactByPhoneNumber(long phoneNumber, long customerId);

}
