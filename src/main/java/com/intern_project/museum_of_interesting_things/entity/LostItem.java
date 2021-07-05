package com.intern_project.museum_of_interesting_things.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "lost_items")
@Entity(name = "LostItem")
@NoArgsConstructor
@Data
public class LostItem {

    @Id
    @Column(name = "item_id")
    private int id;

    @Column(name = "description")
    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_lost")
    private Date dateLost;

    @OneToOne
    @MapsId
    @ToString.Exclude
    @JoinColumn(name = "item_id")
    private Item item;

    public LostItem(int id, String description, Date dateLost, Item item) {
        this.id = id;
        this.description = description;
        this.dateLost = dateLost;
        this.item = item;
    }

    public LostItem(String locDescription, Date dateLost, Item newItem) {
        this.description = locDescription;
        this.dateLost = dateLost;
        this.item = newItem;
    }
}
