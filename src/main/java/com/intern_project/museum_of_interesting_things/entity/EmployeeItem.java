package com.intern_project.museum_of_interesting_things.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "EmployeeItem")
@Table(name = "employee_item")
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"item", "employee"})
@Data
public class EmployeeItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private int id;

    @ManyToOne
    @JoinColumn(name = "employee_id",
            foreignKey = @ForeignKey(name = "employee_item_ibfk_2"))
    @ToString.Exclude
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "item_id",
            foreignKey = @ForeignKey(name = "employee_item_ibfk_1"))
    @ToString.Exclude
    private Item item;

    @Column(name = "worth_value")
    private double worthValue;

    public EmployeeItem(Employee employee, Item item, double worthValue) {
        this.employee = employee;
        this.item = item;
        this.worthValue = worthValue;
    }
}
