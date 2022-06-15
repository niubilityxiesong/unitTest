package com.cleancode.unitTest.repository;

import com.cleancode.unitTest.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<List<Item>> findByCustomerId(Integer id);
}
