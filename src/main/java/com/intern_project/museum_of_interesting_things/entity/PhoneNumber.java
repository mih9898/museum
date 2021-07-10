package com.intern_project.museum_of_interesting_things.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Table(name = "phone_numbers")
@Entity(name = "PhoneNumber")
@NoArgsConstructor
@Data
public class PhoneNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;

    @Column(name = "phone_number")
    private int phoneNumber;


    @ManyToOne
    @JoinColumn(name = "employee_id",
            foreignKey = @ForeignKey(name = "phone_numbers_ibfk_1"))
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="employee_id")
    @ToString.Exclude
    private Employee employee;

    public PhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
