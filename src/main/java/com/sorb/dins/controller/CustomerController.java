package com.sorb.dins.controller;

import com.sorb.dins.model.Customer;
import com.sorb.dins.service.CustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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


    @GetMapping
    public List<Customer> getCustomers(@NotBlank @RequestParam(required = false) String name,
                                       @RequestParam(defaultValue = "false", required = false) boolean isRequiresLike) {
        return name == null ? customerService.getAll() : customerService.findCustomerByName(name, isRequiresLike);
    }

    @PostMapping
    public Customer saveCustomer(@Valid @RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@Positive @PathVariable long id) {
        return customerService.getById(id);
    }

    @PutMapping("/{id}")
    public Customer changeCustomer(@Positive @PathVariable long id, @RequestBody Customer changedCustomer) {
        return customerService.updateCustomer(customerService.getById(id), changedCustomer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@Positive @PathVariable long id) {
        customerService.delete(id);
    }

}
