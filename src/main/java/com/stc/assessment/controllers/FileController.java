package com.stc.assessment.controllers;

import com.stc.assessment.dto.FileDto;
import com.stc.assessment.entities.File;
import com.stc.assessment.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {

    private final ItemService itemService;

    @Autowired
    public FileController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFile(
            @RequestParam("folderName") String folderName,
            @RequestParam("fileName") String fileName,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("file") MultipartFile file) {
        try {
            FileDto fileDto = itemService.createFile(folderName, fileName, userEmail, file);
            return new ResponseEntity<>(fileDto, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to read file content", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/download/{fileId}/{userEmail}")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId, @PathVariable String userEmail) {
        try {
            File file = itemService.getFile(fileId, userEmail);
            ByteArrayResource resource = new ByteArrayResource(file.getBinaryFile());

            // Assuming you have a method to get the filename from the file's metadata

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getItem().getName() + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}

