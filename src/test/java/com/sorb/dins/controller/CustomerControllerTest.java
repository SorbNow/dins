package com.sorb.dins.controller;

import com.sorb.dins.model.Customer;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application.properties")
@Sql(value = {"/create_customer_before.sql", "/create_contacts_before.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/delete_contacts_after.sql", "/delete_customer_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

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
    void findCustomersByName() throws Exception {

        mockMvc.perform(get("/customers")
                .param("name", "nancy"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName", CoreMatchers.is("Nancy")))
                .andExpect(jsonPath("$[0].lastName", CoreMatchers.is("Smith")));
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
    void getCustomerById() throws Exception {

        mockMvc.perform(get("/customers/{id}",2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("John")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Deere")));
    }

    @Test
    void changeCustomer() throws Exception {
        mockMvc.perform(put("/customers/{id}",2)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"firstName\" : \"Alice\", \"lastName\" : \"Madness\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is("Alice")))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is("Madness")));

    }

    @Test
    void deleteCustomer() throws Exception {

        mockMvc.perform(delete("/customers/{id}",3))
                .andExpect(status().isOk());
        assertFalse(customerRepository.existsById(3L));
    }
}