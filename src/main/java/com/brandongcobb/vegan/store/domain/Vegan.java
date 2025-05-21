package com.brandongcobb.vegan.store.domain;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "vegans", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class Vegan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private boolean isAdmin;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;
    
    protected Vegan() {
    }

    public Vegan(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isAdmin = false;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
    
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
    
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vegan)) return false;
        Vegan vegan = (Vegan) o;
        return Objects.equals(id, vegan.id) && Objects.equals(email, vegan.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
