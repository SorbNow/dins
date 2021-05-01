package com.sorb.dins.controller;

import com.sorb.dins.exception.NotFoundInDatabaseException;
import com.sorb.dins.model.Contact;
import com.sorb.dins.service.ContactService;
import com.sorb.dins.service.CustomerService;
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

    @GetMapping
    public List<Contact> getContactsByCustomerId(@Positive @RequestParam long customerId,
                                                 @RequestParam(required = false, defaultValue = "0") long phoneNumber) {
        return phoneNumber == 0 ? contactService.getByCustomerId(customerId) : contactService.findContactByPhoneNumber(phoneNumber, customerId);
    }

    @GetMapping("/{id}")
    public Contact getContactById(@Positive @PathVariable long id) {
        return contactService.getById(id);
    }


    @PostMapping
    public Contact saveContact(@Valid @RequestBody Contact newContact) {
        if (customerService.getById(newContact.getCustomer().getId()) == null)
            throw new NotFoundInDatabaseException("customer with id " + newContact.getCustomer().getId() + " not found in database");
        return contactService.save(newContact);
    }

    @PutMapping("/{id}")
    public Contact changeContact(@Positive @PathVariable long id, @RequestBody Contact changedContact) {
        return contactService.updateContact(contactService.getById(id), changedContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@Positive @PathVariable long id) {
        contactService.delete(id);
    }
}
