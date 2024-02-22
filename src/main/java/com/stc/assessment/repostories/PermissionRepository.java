package com.stc.assessment.repostories;

import com.stc.assessment.entities.Permission;
import com.stc.assessment.enums.PermissionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query("SELECT p FROM Permission p JOIN p.group g JOIN g.items item WHERE p.userEmail = :userEmail AND item.id = :itemId AND p.permissionLevel = :permissionLevel")
    Optional<Permission> findByUserEmailAndItemIdAndPermissionLevel(@Param("userEmail") String userEmail, @Param("itemId") Long itemId, @Param("permissionLevel") PermissionLevel permissionLevel);
}

