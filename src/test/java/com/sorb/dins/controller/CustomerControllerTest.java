package com.sorb.dins.controller;

import com.sorb.dins.repository.ContactRepository;
import com.sorb.dins.repository.CustomerRepository;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
@Sql(value = {"/create_customer_before.sql", "/create_contacts_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_contacts_after.sql", "/delete_customer_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ContactRepository contactRepository;

    @Test
    void getAllCustomers() throws Exception {

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Nancy")))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Smith")))
                .andExpect(jsonPath("$[2].firstName", CoreMatchers.is("Michael")))
                .andExpect(jsonPath("$[2].lastName", CoreMatchers.is("Sallivan")));
    }

    @Test
    void getEmptyTableCustomers() throws Exception {

        //clear DB
        contactRepository.deleteAll();
        customerRepository.deleteAll();

        mockMvc.perform(get("/customers"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Customers table is empty")));
    }

    @Test
    void findCustomersByName() throws Exception {

        mockMvc.perform(get("/customers")
                .param("name", "nancy"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Nancy")))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Smith")));
    }

    @Test
    void findCustomersByWrongName() throws Exception {

        mockMvc.perform(get("/customers")
                .param("name", "Pier"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Customers with name Pier not found in database")));
    }

    @Test
    void findCustomersByPartOfName() throws Exception {

        mockMvc.perform(get("/customers")
                .param("name", "an")
                .param("isRequiresLike", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Nancy")))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Smith")))
                .andExpect(jsonPath("$[1].firstName", CoreMatchers.is("Michael")))
                .andExpect(jsonPath("$[1].lastName", CoreMatchers.is("Sallivan")));
    }

    @Test
    void findCustomersByWrongPartOfName() throws Exception {

        mockMvc.perform(get("/customers")
                .param("name", "ie")
                .param("isRequiresLike", "true"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Customers with name ie not found in database")));
    }

    @Test
    void saveCustomer() throws Exception {

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Alice")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Madness")));
    }

    @Test
    void saveWrongCustomer() throws Exception {

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Last name parameter must have at least 1 character")));

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("First name parameter must have at least 1 character")));

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("First name parameter must have at least 1 character")))
                .andExpect(content().string(containsString("Last name parameter must have at least 1 character")));
    }

    @Test
    void getCustomerById() throws Exception {

        mockMvc.perform(get("/customers/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("John")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Deere")));
    }

    @Test
    void getCustomerByWrongId() throws Exception {

        mockMvc.perform(get("/customers/{id}", 14))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Customer with id: 14 not found in database")));
    }

    @Test
    void getCustomerByNegativeId() throws Exception {

        mockMvc.perform(get("/customers/{id}", -14))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be greater than 0")));
    }

    @Test
    void getCustomerWithWrongTypeOfId() throws Exception {

        mockMvc.perform(get("/customers/{id}", "asd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("should be of type long")));
    }

    @Test
    void changeCustomer() throws Exception {
        mockMvc.perform(put("/customers/{id}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Alice")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Madness")));

    }


    @Test
    void changeWithCustomerNegativeId() throws Exception {
        mockMvc.perform(put("/customers/{id}", -22)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("must be greater than 0")));

    }

    @Test
    void deleteCustomer() throws Exception {

        mockMvc.perform(delete("/customers/{id}", 3))
                .andExpect(status().isOk());
        assertFalse(customerRepository.existsById(3L));
    }

    @Test
    void deleteCustomerWithWrongId() throws Exception {

        mockMvc.perform(delete("/customers/{id}", 1233))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Customer with id: 1233 not found in database")));
        ;
    }

    @Test
    void saveCustomerWithoutRequestBody() throws Exception {

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Malformed JSON request. Please, check your request body")));
    }

    @Test
    void saveCustomerWithWrongTypeRequestBody() throws Exception {

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_PDF)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(" Please, use JSON media type")));
    }

    @Test
    void saveCustomerWithWrongMethodType() throws Exception {

        mockMvc.perform(put("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Supported methods")))
                .andExpect(content().string(containsString("POST")));
    }

    @Test
    void saveCustomerWithAnotherWrongMethodType() throws Exception {

        mockMvc.perform(post("/customers/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Supported methods")))
                .andExpect(content().string(containsString("PUT")));
    }


}