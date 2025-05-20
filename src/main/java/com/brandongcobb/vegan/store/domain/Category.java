package com.brandongcobb.vegan.store.domain;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIgnore;   // <- add this
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    public Category() { }

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    public Category getParent() {
        return parent;
    }
    
    public void setParent(Category parent) {
        this.parent = parent;
    }
    
    @OneToMany(mappedBy = "parent",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Category> children = new ArrayList<>();
    
    public Long getId() {
        return id;
    }

    // No setter for id—JPA manages this

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
    
    @JsonIgnore
    public String getFullPath() {
      return parent == null ?
         name :
         parent.getFullPath() + " / " + name;
    }
}
