package com.sorb.dins.controller;

import com.sorb.dins.exception.NotFoundInDatabaseException;
import com.sorb.dins.model.Contact;
import com.sorb.dins.service.ContactService;
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
 * Contact controller class
 *
 * @author sorb
 */

@RestController
@RequestMapping("/contacts")
@Validated
public class ContactController {

    /**
     * Autowired {@link ContactService}
     */
    private final ContactService contactService;
    private final CustomerService customerService;

    public ContactController(ContactService contactService, CustomerService customerService) {
        this.contactService = contactService;
        this.customerService = customerService;
    }

    @Operation(summary = "get contacts list by customer id. " +
            "If phone number not empty - find contact with number in customer phone book")
    @GetMapping
    public List<Contact> getContactsByCustomerId(@Positive @RequestParam long customerId,
                                                 @RequestParam(required = false, defaultValue = "0") long phoneNumber) {
        return phoneNumber == 0 ? contactService.getByCustomerId(customerId) : contactService.findContactByPhoneNumber(phoneNumber, customerId);
    }

    @Operation(summary = "save contact to customer phone book")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "add contact JSON",
                    value =
                            "{ \"firstName\" : \"string\", \"lastName\": \"string\", \"phoneNumber\":1, \"customer\": { \"id\":1} }")
    }))
    @PostMapping
    public Contact saveContact(@Valid @RequestBody Contact newContact) {
        if (customerService.getById(newContact.getCustomer().getId()) == null)
            throw new NotFoundInDatabaseException("customer with id " + newContact.getCustomer().getId() + " not found in database");
        return contactService.save(newContact);
    }

    @Operation(summary = "get contact from database by id")
    @GetMapping("/{id}")
    public Contact getContactById(@Parameter(description = "id of contact which should be searched")
                                  @Positive @PathVariable long id) {
        return contactService.getById(id);
    }

    @Operation(summary = "edit contact from database by id")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = {
            @ExampleObject(
                    name = "edit contact JSON",
                    value =
                            "{ \"firstName\" : \"string\", \"lastName\": \"string\", \"phoneNumber\":1 }")
    }))
    @PutMapping("/{id}")
    public Contact changeContact(@Parameter(description = "id of contact which should be edited")
                                 @Positive @PathVariable long id,
                                 @RequestBody Contact changedContact) {
        return contactService.updateContact(contactService.getById(id), changedContact);
    }

    @Operation(summary = "delete contact from database by id")
    @DeleteMapping("/{id}")
    public void deleteContact(@Parameter(description = "id of contact which should be deleted")
                              @Positive @PathVariable long id) {
        contactService.delete(id);
    }
}
