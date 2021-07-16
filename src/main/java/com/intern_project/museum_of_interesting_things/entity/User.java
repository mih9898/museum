package com.intern_project.museum_of_interesting_things.entity;

import com.intern_project.museum_of_interesting_things.entity.Authority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;


/**
 * The type User.
 *
 * @author mturchanov
 */
@Table(name = "users")
@Entity(name = "User")
@NoArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    @Column(name = "username")
    private String username;
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private int enabled;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
    private List<Authority> authorityList;

    @ToString.Exclude
    @OneToOne(mappedBy = "user",
            cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE})
//    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Employee employee;

    /**
     * Add authority to user.
     *
     * @param authority the authority
     */
    public void addAuthorityToUser(Authority authority) {
        if(authorityList == null) {
            authorityList = new ArrayList<>();
        }
        authorityList.add(authority);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}


