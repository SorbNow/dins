package com.sorb.dins.service;

import com.sorb.dins.model.Contact;
import com.sorb.dins.model.Customer;
import com.sorb.dins.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class CustomerServiceImplTest {

    @MockBean
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    @Test
    void getAll() {
        Mockito.doReturn(Collections.singletonList(createSimpleCustomer()))
                .when(customerRepository).findAll();

        Customer customer = customerService.getAll().get(0);
        assertEquals(1, customer.getId());
        assertEquals(2, customer.getContacts().size());

        Mockito.verify(customerRepository, times(1)).findAll();
    }

    @Test
    void getById() {
        Mockito.doReturn(Optional.of(createSimpleCustomer()))
                .when(customerRepository).findById(any());

        Customer customer = customerService.getById(1);
        assertNotNull(customer.getFirstName());
        assertEquals(1, customer.getId());
        assertEquals("Peterson", customer.getLastName());

        Mockito.verify(customerRepository, times(2)).findById(anyLong());
    }

    @Test
    void delete() {
        customerService.delete(1);
        Mockito.verify(customerRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void save() {
        Mockito.doReturn(createSimpleCustomer())
                .when(customerRepository).save(any(Customer.class));


    }

    @Test
    void updateCustomer() {

        Customer customer = createSimpleCustomer();
        Customer newCustomer = new Customer();
        newCustomer.setFirstName("Luke");
        newCustomer.setLastName("Skywalker");
        Mockito.doReturn(customer)
                .when(customerRepository).save(customer);

        Customer updatedCustomer = customerService.updateCustomer(customer, newCustomer);
        Mockito.verify(customerRepository, times(1)).save(any(Customer.class));

        assertEquals("Luke", customer.getFirstName());
        assertEquals("Skywalker", customer.getLastName());
        assertNotEquals(0, customer.getId());
        assertNotNull(customer.getContacts());
    }

    @Test
    void findCustomerByName() {
        Mockito.doReturn(Collections.singletonList(createSimpleCustomer()))
                .when(customerRepository).findCustomersByLastNameIgnoreCaseOrFirstNameIgnoreCase("Carl", "Carl");
        Mockito.doReturn(Collections.singletonList(createSimpleCustomer()))
                .when(customerRepository).findCustomersByLastNameIgnoreCaseContainsOrFirstNameIgnoreCaseContains("arl", "arl");

        List<Customer> customers = customerService.findCustomerByName("Carl", false);

        assertEquals(1, customers.size());

        Customer customer = customers.get(0);

        assertEquals("Carl", customer.getFirstName());
        assertNotNull(customer.getContacts());

        Mockito.verify(customerRepository, times(1))
                .findCustomersByLastNameIgnoreCaseOrFirstNameIgnoreCase(anyString(), anyString());

        customers = customerService.findCustomerByName("arl", true);

        Mockito.verify(customerRepository, times(1))
                .findCustomersByLastNameIgnoreCaseContainsOrFirstNameIgnoreCaseContains(anyString(), anyString());
        assertEquals(1, customers.size());

        customer = customers.get(0);

        assertEquals("Carl", customer.getFirstName());
        assertNotNull(customer.getContacts());
    }

    private Customer createSimpleCustomer() {
        Customer simpleCustomer = new Customer();
        simpleCustomer.setId(1L);
        simpleCustomer.setFirstName("Carl");
        simpleCustomer.setLastName("Peterson");

        Contact contact = new Contact();
        contact.setFirstName("Asoka");
        contact.setLastName("Tano");
        contact.setPhoneNumber(2222);
        contact.setCustomer(simpleCustomer);

        Contact contact2 = new Contact();
        contact2.setFirstName("Obi");
        contact2.setLastName("Kenobi");
        contact2.setPhoneNumber(2222);
        contact2.setCustomer(simpleCustomer);

        Set<Contact> contacts = new HashSet<>();
        contacts.add(contact);
        contacts.add(contact2);

        simpleCustomer.setContacts(contacts);

        return simpleCustomer;

    }

}