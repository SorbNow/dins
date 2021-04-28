package com.sorb.dins.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@ToString
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Customer {

    @Id
    @GeneratedValue
    private long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private Set<Contact> contacts;

}
