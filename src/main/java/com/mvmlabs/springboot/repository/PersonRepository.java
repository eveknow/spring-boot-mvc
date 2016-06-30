package com.mvmlabs.springboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.mvmlabs.springboot.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    
    /**
     * Find persons by name.
     */
    public List<Person> findByName(String name);
    
}
