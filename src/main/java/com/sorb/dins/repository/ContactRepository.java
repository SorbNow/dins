package com.sorb.dins.repository;

import com.sorb.dins.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findContactsByCustomer_Id(Long id);

    List<Contact> findContactsByPhoneNumberAndCustomer_Id(long phoneNumber,long id);

}
