package com.sorb.dins.service;

import com.sorb.dins.model.Contact;
import com.sorb.dins.model.Customer;
import com.sorb.dins.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@SpringBootTest
class ContactServiceImplTest {

    @MockBean
    private ContactRepository contactRepository;

    @Autowired
    private ContactServiceImpl contactService;

    @Test
    void save() {
        Contact contact = new Contact();
        contact.setFirstName("Katy");
        Mockito.doReturn(contact)
                .when(contactRepository)
                .save(contact);

        contact = contactService.save(contact);

        assertNotNull(contact);
        Mockito.verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void getById() {
        Contact returnedContact = getSimpleContact();

        Mockito.when(contactRepository.findById(2L))
                .thenReturn(Optional.of(returnedContact));

        Contact contact = contactService.getById(2);
        assertNotNull(contact);
        assertEquals("Vasily", contact.getFirstName());
        assertEquals(779345, contact.getPhoneNumber());

        Mockito.verify(contactRepository, times(2)).findById(2L);
    }

    @Test
    void delete() {
        Mockito.doReturn(Optional.of(getSimpleContact()))
                .when(contactRepository).findById(any());
        contactService.delete(contactRepository.findById(1L).get().getId());

        Mockito.verify(contactRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void getByCustomerId() {

        Contact contact = getSimpleContact();
        Mockito.when(contactRepository.findContactsByCustomer_Id(1L)).thenReturn(Collections.singletonList(contact));
        List<Contact> contactList = contactService.getByCustomerId(1L);
        Mockito.verify(contactRepository, times(1)).findContactsByCustomer_Id(1L);
        assertEquals(1, contactList.size());
        assertEquals(contact.getLastName(), contactList.get(0).getLastName());
    }

    @Test
    void updateContact() {
        Contact contact = getSimpleContact();
        Contact newContact = new Contact();
        newContact.setFirstName("Amy");
        newContact.setLastName("Fauler");
        newContact.setPhoneNumber(4478);
        Mockito.doReturn(contact)
                .when(contactRepository)
                .save(contact);
        Contact updatedContact = contactService.updateContact(contact, newContact);
        assertEquals("Amy", updatedContact.getFirstName());
        assertNotEquals(0, updatedContact.getId());
        Mockito.verify(contactRepository, times(1)).save(contact);
    }

    @Test
    void findContactByPhoneNumber() {
        Mockito.doReturn(Collections.singletonList(getSimpleContact()))
                .when(contactRepository)
                .findContactsByPhoneNumberAndCustomer_Id(779345, 1L);

        List<Contact> contacts = contactService.findContactByPhoneNumber(779345, 1L);
        assertEquals(1, contacts.size());
        assertNotNull(contacts.get(0));

        Mockito.verify(contactRepository, times(1))
                .findContactsByPhoneNumberAndCustomer_Id(779345, 1L);
    }

    @Test
    void deleteRelatedContacts() {
        Contact contact = getSimpleContact();
        Contact secondContact = new Contact();
        secondContact.setId(1L);
        secondContact.setFirstName("Martin");
        secondContact.setLastName("Brundle");
        secondContact.setPhoneNumber(11111);
        secondContact.setCustomer(contact.getCustomer());
        List<Contact> contactList = Arrays.asList(contact, secondContact);

        assertEquals(2, contactList.size());

        Mockito.doReturn(contactList).when(contactRepository).findContactsByCustomer_Id(any());

        contactService.deleteRelatedContacts(1L);

        Mockito.verify(contactRepository, times(1)).findContactsByCustomer_Id(1L);
        Mockito.verify(contactRepository, times(2)).deleteById(any());
    }

    private Contact getSimpleContact() {
        Customer customer = new Customer();
        customer.setFirstName("Vanda");
        customer.setLastName("Smith");
        customer.setId(1L);

        Contact returnedContact = new Contact();
        returnedContact.setId(2);
        returnedContact.setFirstName("Vasily");
        returnedContact.setLastName("Red");
        returnedContact.setPhoneNumber(779345);
        returnedContact.setCustomer(customer);
        return returnedContact;
    }
}