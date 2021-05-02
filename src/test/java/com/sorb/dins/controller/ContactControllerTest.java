package com.sorb.dins.controller;

import com.sorb.dins.model.Contact;
import com.sorb.dins.repository.ContactRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
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
    void getContactsByCustomerIdWithNegativeId() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "-3"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("must be greater than 0")));

    }

    @Test
    void getContactsByCustomerIdWithWrongTypeId() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "a"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("should be of type long")));

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
    void getContactsByNullCustomerIdAndPhone() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("phoneNumber", "787446"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("customerId parameter is missing")));

    }

    @Test
    void getContactsByCustomerIdWithWrongPhoneNumber() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "1")
                .param("phoneNumber", "7876"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Contacts with phone number:")))
                .andExpect(content().string(containsString("not found in database")));

    }


    @Test
    void getContactsWithWrongCustomerId() throws Exception {
        mockMvc.perform(get("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .param("customerId", "4")
                .param("phoneNumber", "787446"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Contacts with phone number:")))
                .andExpect(content().string(containsString("not found in database")));

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
    void saveContactValidationTest() throws Exception {
        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"phoneNumber\":13772, \"customer\" : {\"id\":1 }}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Last name parameter must have at least 1 character")));

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"lastName\" : \"Madness\", \"phoneNumber\":13772, \"customer\" : {\"id\":1 }}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("First name parameter must have at least 1 character")));

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"customer\" : {\"id\":1 }}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":\"asd\", \"customer\" : {\"id\":1 }}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("parse error")));

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":123 }"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Customer can't be null")));

        mockMvc.perform(post("/contacts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":123, \"customer\" : {\"id\":32 }}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("not found in database")));
    }

    @Test
    void getContactById() throws Exception {
        mockMvc.perform(get("/contacts/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Vasily")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Dad")))
                .andExpect(jsonPath("$.phoneNumber", CoreMatchers.is(779345)));
    }

    @Test
    void getContactByWrongId() throws Exception {
        mockMvc.perform(get("/contacts/{id}", 222))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("not found in database")));
    }

    @Test
    void getContactByNegativeId() throws Exception {
        mockMvc.perform(get("/contacts/{id}", -222))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("must be greater than 0")));
    }

    @Test
    void getContactByWrongIdType() throws Exception {
        mockMvc.perform(get("/contacts/{id}", "a"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("should be of type long")));
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
    void changeContactWithWrongId() throws Exception {

        mockMvc.perform(put("/contacts/{id}", 2222)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":123}"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found in database")));
    }

    @Test
    void changeContactWithNegativeId() throws Exception {

        mockMvc.perform(put("/contacts/{id}", -2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":123}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be greater than 0")));
    }

    @Test
    void changeContactWithWrongPhoneNumber() throws Exception {

        mockMvc.perform(put("/contacts/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":\"asd\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("parse error")));
    }

    @Test
    void changeContactWithWrongPhoneNumberMediaType() throws Exception {

        mockMvc.perform(put("/contacts/{id}", 2)
                .contentType(MediaType.APPLICATION_CBOR)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\", \"phoneNumber\":123}"))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().string(containsString("Please, use JSON media type")));
    }

    @Test
    void changeContactWithNullBody() throws Exception {

        mockMvc.perform(put("/contacts/{id}", 2))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Malformed JSON request. Please, check your request body")));
    }

    @Test
    void deleteContact() throws Exception {
        mockMvc.perform(delete("/contacts/{id}", 2))
                .andExpect(status().isOk());
        assertFalse(contactRepository.existsById(2L));
    }

    @Test
    void deleteContactWithNegativeId() throws Exception {
        mockMvc.perform(delete("/contacts/{id}", -2))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be greater than 0")));
    }

    @Test
    void deleteContactWithWrongId() throws Exception {
        mockMvc.perform(delete("/contacts/{id}", 222))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("not found in database")));
    }

    @Test
    void deleteContactWithWrongTypeId() throws Exception {
        mockMvc.perform(delete("/contacts/{id}", "a"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("should be of type long")));
    }

    @Test
    void gethWrongMethodType() throws Exception {

        mockMvc.perform(put("/contacts")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Supported methods")))
                .andExpect(content().string(containsString("POST")));

        mockMvc.perform(post("/contacts/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Supported methods")))
                .andExpect(content().string(containsString("PUT")));
    }
}