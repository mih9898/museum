package com.intern_project.museum_of_interesting_things.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;


@Table(name = "locations")
@Entity(name = "Location")
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "items")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    @Column(name = "id")
    private int id;

    @Column(name = "storage_type")
    private String storageType;

    @Column(name = "description")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_when_put")
    private Date dateWhenPut;

    //TODO: if response time is too long then change from genericDao to specificDao
    //   + Using Join Fetching for specific case(item-loc)
    @ManyToMany(cascade = {CascadeType.MERGE}, fetch=FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(
            name = "item_location",
            joinColumns = @JoinColumn(name = "location_id"), //write how bridge table get connected with this source table/entity
            inverseJoinColumns = @JoinColumn(name = "item_id") //write how bridge table get connected with other target table/entity
    )
    private Set<Location> items;


    public Location(String storageType, String description, Date dateWhenPut) {
        this.storageType = storageType;
        this.description = description;
        this.dateWhenPut = dateWhenPut;
    }
}
