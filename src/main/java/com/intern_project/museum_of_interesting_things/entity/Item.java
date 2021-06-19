package com.intern_project.museum_of_interesting_things.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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

    @Column(name = "date_acquired")
    private Date dateAcquired;

    @Column(name = "is_lost")
    private int isLost;

    @Column(name = "is_museum_item")
    private int isMuseumItem;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LostItem lostItem;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinTable(
            name = "item_location",
            joinColumns = @JoinColumn(name = "item_id"), //write how bridge table get connected with this source table/entity
            inverseJoinColumns = @JoinColumn(name = "location_id") //write how bridge table get connected with other target table/entity
    )
    private Set<Location> locations;

    public void addLocationToItem(Location location) {
        if (locations == null) {
            locations = new HashSet<>();
        }
        locations.add(location);
    }

    public Item(String name, String description, Date dateAcquired, int isLost, int isMuseumItem) {
        this.name = name;
        this.description = description;
        this.dateAcquired = dateAcquired;
        this.isLost = isLost;
        this.isMuseumItem = isMuseumItem;
    }


}
