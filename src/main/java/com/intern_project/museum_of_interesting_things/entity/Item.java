package com.intern_project.museum_of_interesting_things.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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


    public Item(String name, String description, Date dateAcquired, int isLost, int isMuseumItem) {
        this.name = name;
        this.description = description;
        this.dateAcquired = dateAcquired;
        this.isLost = isLost;
        this.isMuseumItem = isMuseumItem;
    }


}
