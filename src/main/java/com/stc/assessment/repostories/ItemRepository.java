package com.stc.assessment.repostories;

import com.stc.assessment.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    boolean existsByName(String name);

    Optional<Item> findByName(String name);

    boolean existsByNameAndParentId(String name, Long parentId);
}