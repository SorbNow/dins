package com.sorb.dins.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

/**
 * Contact entity class
 * getters, setters, toString methods generates by lombok
 *
 * @author sorb
 */
@Entity
@Table
@Getter
@Setter
@ToString
public class Contact {

    /**
     * ID column in contact
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    /**
     * First name column in contact. Casts to first_name column in DB
     */
    @Column
    private String firstName;

    /**
     * Last name column in contact. Casts to last_name column in DB
     */
    @Column
    private String lastName;
    /**
     * Phone number column in contact. Casts to phone_number column in DB
     */
    @Column
    private long phoneNumber;

    /**
     * Customer column in contact. Casts to customer_id column in DB
     * Associated with {@link Customer}
     * JoinColumn references to {@link Customer#id} field
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

}
