package com.sorb.dins.service;

import com.sorb.dins.model.Contact;
import com.sorb.dins.repository.ContactRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

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
        Mockito.verify(contactRepository,Mockito.times(1)).save(contact);
    }

    @Test
    void getById() {
        Contact returnedContact = new Contact();
        returnedContact.setId(2);
        returnedContact.setFirstName("Vasily");
        returnedContact.setLastName("Red");
        returnedContact.setPhoneNumber(779345);

        Mockito.when(contactRepository.findById(2L))
                .thenReturn(Optional.of(returnedContact));

        Contact contact = contactService.getById(2);
        assertNotNull(contact);
        assertEquals("Vasily",contact.getFirstName());
        assertEquals(779345,contact.getPhoneNumber());

        Mockito.verify(contactRepository,Mockito.times(2)).findById(2L);
    }

    @Test
    void delete() {

    }

    @Test
    void getByCustomerId() {
    }

    @Test
    void updateContact() {
    }

    @Test
    void findContactByPhoneNumber() {
    }

    @Test
    void deleteRelatedContacts() {
    }
}