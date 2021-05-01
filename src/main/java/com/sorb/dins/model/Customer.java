package com.sorb.dins.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Customer entity class
 * getters, setters, toString methods generates by lombok
 *
 * @author sorb
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class Customer {

    /**
     * ID column in customer
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * First name column in customer. Casts to first_name column in DB
     */
    @Column
    @NotBlank(message = "First name parameter must have at least 1 character")
    private String firstName;

    /**
     * Last name column in customer. Casts to last_name column in DB
     */
    @Column
    @NotBlank(message = "Last name parameter must have at least 1 character")
    private String lastName;

    /**
     * Contact column in customer.
     * Associated with {@link Contact}
     */
    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    private Set<Contact> contacts;

}
