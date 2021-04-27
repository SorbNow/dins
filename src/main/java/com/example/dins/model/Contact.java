package com.example.dins.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@ToString
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contact {

    @GeneratedValue
    @Id
    private long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private long phoneNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;

}
