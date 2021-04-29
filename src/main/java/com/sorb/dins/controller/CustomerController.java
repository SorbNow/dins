package com.sorb.dins.controller;

import com.sorb.dins.model.Customer;
import com.sorb.dins.service.CustomerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Customer controller class
 *
 * @author sorb
 */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    /**
     * Autowired {@link CustomerService}
     */
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


    @GetMapping
    public List<Customer> getCustomers(@RequestParam(required = false) String name,
                                       @RequestParam(defaultValue = "false", required = false) boolean isRequiresLike) {
        return name == null ? customerService.getAll() : customerService.findCustomerByName(name, isRequiresLike);
    }

    @PostMapping
    public Customer saveCustomer(@RequestBody Customer customer) {
        return customerService.save(customer);
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable long id) {
        return customerService.getById(id);
    }

    @PutMapping("/{id}")
    public Customer changeCustomer(@PathVariable long id, @RequestBody @Validated Customer changedCustomer) {
        return customerService.updateCustomer(customerService.getById(id), changedCustomer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable long id) {
        customerService.delete(id);
    }

}
