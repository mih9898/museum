package com.intern_project.museum_of_interesting_things.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * The type Authority.
 *
 * @author mturchanov
 */
@Table(name = "authorities")
@Entity(name = "Authority")
@NoArgsConstructor
@Data
public class Authority {
    @Column
    private int id;

    @Column
    @Id
    private String username;

    @Column
    private String authority = "ROLE_USER";

}