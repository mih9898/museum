package com.intern_project.museum_of_interesting_things.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Table(name = "employees")
@Entity(name = "Employee")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "phoneNumbers")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;

    @Column(name = "position")
    private String position;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "salary")
    private double salary;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_address")
    private String zipAddress;

    @Column(name = "with_us")
    private Boolean withUs;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true) //fetch eager was here
    private List<PhoneNumber> phoneNumbers;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<EmployeeItem> employeeItems = new HashSet<>();

    @OneToOne
    @MapsId
    @EqualsAndHashCode.Exclude
    @JoinColumn(name = "username")
    private User user;

    public void addPhoneNumberToEmployee(PhoneNumber phoneNumber) {
        if (phoneNumbers == null) {
            phoneNumbers = new ArrayList<>();
        }
        phoneNumbers.add(phoneNumber);
    }

    public Employee(String position, String firstName, String lastName,
                    double salary, String address, String city, String state, String zipAddress, boolean withUs) {
        this.position = position;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipAddress = zipAddress;
        this.withUs = withUs;
    }
}
