package com.stc.assessment.repostories;

import com.stc.assessment.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    @Query("SELECT f FROM File f WHERE f.id = :fileId")
    Optional<File> findFileMetadataById(@Param("fileId") Long fileId);
}
