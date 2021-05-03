package com.sorb.dins.controller;

import com.sorb.dins.model.Customer;
import com.sorb.dins.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * Customer controller class
 *
 * @author sorb
 */
@RestController
@RequestMapping("/customers")
@Validated
public class CustomerController {

    /**
     * Autowired {@link CustomerService}
     */
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "get customer list by full/part of first and last name. If parameters empty-return all customers")
    @GetMapping
    public List<Customer> getCustomers(@Parameter(description = "first or last name of sought customer")
                                       @RequestParam(required = false) String name,
                                       @Parameter(description = "if true - search by part of fist and last name")
                                       @RequestParam(defaultValue = "false", required = false) boolean isRequiresLike) {
        return name == null ? customerService.getAll() : customerService.findCustomerByName(name, isRequiresLike);
    }

    @Operation(summary = "save customer to database")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "add customer JSON",
                    value =
                            "{ \"firstName\": \"string\", \"lastName\": \"string\" }")
    }))
    @PostMapping
    public Customer saveCustomer(@Valid @RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @Operation(summary = "get customer from database by id")
    @GetMapping("/{id}")
    public Customer getCustomerById(@Parameter(description = "id of customer which should be searched") @Positive @PathVariable long id) {
        return customerService.getById(id);
    }

    @Operation(summary = "edit customer in database")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "edit customer JSON",
                    value =
                            "{ \"firstName\": \"string\", \"lastName\": \"string\" }")
    }))
    @PutMapping("/{id}")
    public Customer changeCustomer(@Parameter(description = "id of customer which should be edited")
                                   @Positive @PathVariable long id, @RequestBody Customer changedCustomer) {
        return customerService.updateCustomer(customerService.getById(id), changedCustomer);
    }

    @Operation(summary = "delete customer from database")
    @DeleteMapping("/{id}")
    public void deleteCustomer(@Parameter(description = "id of customer which should be deleted")
                               @Positive @PathVariable long id) {
        customerService.delete(id);
    }

}
