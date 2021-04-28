package com.sorb.dins.controller;

import com.sorb.dins.model.Contact;
import com.sorb.dins.repository.ContactRepository;
import com.sorb.dins.repository.CustomerRepository;
import com.sorb.dins.service.ContactServiceImpl;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
@Sql(value = {"/create_customer_before.sql", "/create_contacts_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_contacts_after.sql", "/delete_customer_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactRepository contactRepository;

    @Test
    void getContactsByCustomerId() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Nana")));

    }

    @Test
    void getContactsByCustomerIdAndPhone() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "1")
                .param("phoneNumber", "787446"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Nana")))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Mom")));

    }

    @Test
    void getContactsByCustomerIdWithWrongPhoneNumber() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "1")
                .param("phoneNumber", "7876"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }


    @Test
    void getContactsWithWrongCustomerId() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "4")
                .param("phoneNumber", "787446"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

    }

    @Test
    void saveContact() throws Exception {
        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":13772, \"customer\" : {\"id\":1 }}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Alice")));

    }

    @Test
    void changeContact() throws Exception {
        Contact contact = contactRepository.findById(2L).get();
        mockMvc.perform(put("/contacts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Alice")));

        Contact changedContact = contactRepository.findById(2L).get();
        assertEquals(contact.getId(), changedContact.getId());
        assertEquals(contact.getPhoneNumber(), changedContact.getPhoneNumber());
        assertEquals(changedContact.getFirstName(), "Alice");
        assertEquals(changedContact.getLastName(), "Madness");
        assertNotEquals(contact.getFirstName(), changedContact.getFirstName());
    }

    @Test
    void deleteContact() throws Exception {
        mockMvc.perform(delete("/contacts/{id}", 2))
                .andExpect(status().isOk());
        assertFalse(contactRepository.existsById(2L));
    }

    @Test
    void getContactById() throws Exception {
        mockMvc.perform(get("/contacts/{id}",2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Vasily")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Dad")))
                .andExpect(jsonPath("$.phoneNumber", CoreMatchers.is(779345)));
    }
}