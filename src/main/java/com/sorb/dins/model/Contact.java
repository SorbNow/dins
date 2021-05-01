package com.sorb.dins.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

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
    @NotBlank(message = "First name parameter must have at least 1 character")
    private String firstName;

    /**
     * Last name column in contact. Casts to last_name column in DB
     */
    @Column
    @NotBlank(message = "Last name parameter must have at least 1 character")
    private String lastName;
    /**
     * Phone number column in contact. Casts to phone_number column in DB
     */
    @Column
    @Positive(message = "Phone number must be positive")
    private long phoneNumber;

    /**
     * Customer column in contact. Casts to customer_id column in DB
     * Associated with {@link Customer}
     * JoinColumn references to {@link Customer#id} field
     */
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @NotNull(message = "Customer can't be null")
    @JsonBackReference
    private Customer customer;

}
