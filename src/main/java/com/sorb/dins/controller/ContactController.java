package com.sorb.dins.controller;

import com.sorb.dins.model.Contact;
import com.sorb.dins.service.ContactService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getContactsByCustomerId(@RequestParam long customerId,
                                                 @RequestParam(required = false, defaultValue = "0") long phoneNumber) {
        return phoneNumber==0? contactService.getByCustomerId(customerId):contactService.findContactByPhoneNumber(phoneNumber,customerId);
    }

//    @GetMapping
//    public List<Contact> getContactsByPhoneNumber(@RequestParam(required = false) long phoneNumber, @RequestParam(defaultValue = "false", required = false) boolean isRequiresLike) {
//        return contactService.findContactByPhoneNumber(phoneNumber, isRequiresLike);
//    }

    /*
    JSON:
    {
    "firstName" : "nanrny",
    "lastName" : "dady",
    "phoneNumber":13772,
    "customer" : {"id":1}


}*/

    @PostMapping
    public Contact saveContact(@RequestBody Contact newContact) {
        return contactService.save(newContact);
    }

    @PutMapping("/{id}")
    public Contact changeContact(@PathVariable long id, @RequestBody Contact changedContact) {
        return contactService.updateContact(contactService.getById(id), changedContact);
    }

    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable long id) {
        contactService.delete(id);
    }
}
