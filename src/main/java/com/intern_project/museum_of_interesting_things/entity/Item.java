package com.intern_project.museum_of_interesting_things.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.*;

@Table(name = "items")
@Entity(name = "Item")
@NoArgsConstructor
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_acquired")
    private Date dateAcquired;

//    TODO: get rid of this property; seems redundant. if leave then refactor to bool
    @Column(name = "is_lost")
    private int isLost;

    @Column(name = "is_museum_item")
    private Boolean isMuseumItem;

    @Column(name = "image")
    private String image = "noItemImage.png";

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LostItem lostItem;


    @ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "item_location",
            joinColumns = @JoinColumn(name = "item_id"), //write how bridge table get connected with this source table/entity
            inverseJoinColumns = @JoinColumn(name = "location_id") //write how bridge table get connected with other target table/entity
    )
    private List<Location> locations;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true) //fetch eafer was here
    private List<EmployeeItem> employeeItems;




    public void addLocationToItem(Location location) {
        if (locations == null) {
//            locations = new HashSet<>();
            locations = new ArrayList<>();
        }
        locations.add(location);
    }

    public void addEmployeeAprToItem(EmployeeItem employeeItem) {
        if (employeeItems == null) {
            employeeItems = new ArrayList<>();
        }
        employeeItems.add(employeeItem);
    }

    public Item(String name, String description, Date dateAcquired, boolean isMuseumItem) {
        this.name = name;
        this.description = description;
        this.dateAcquired = dateAcquired;
        this.isMuseumItem = isMuseumItem;
    }



    public Item(int id, String name, String description, Date dateAcquired, int isLost, boolean isMuseumItem) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateAcquired = dateAcquired;
        this.isLost = isLost;
        this.isMuseumItem = isMuseumItem;
    }

    public Item(String name, String desc, Date date, int isLost, boolean isMuseum) {
        this.name = name;
        this.description = desc;
        this.dateAcquired = date;
        this.isLost = isLost;
        this.isMuseumItem = isMuseum;
    }


}
