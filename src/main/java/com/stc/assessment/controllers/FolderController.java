package com.stc.assessment.controllers;

import com.stc.assessment.dto.FolderDto;
import com.stc.assessment.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/folders")
public class FolderController {

    private final ItemService itemService;

    @Autowired
    public FolderController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/createFolder")
    public ResponseEntity<?> createFolder(
            @RequestParam String folderName,
            @RequestParam String userEmail,
            @RequestParam(value = "spaceName", defaultValue = "stc-assessments") String spaceName
    ) {
        try {
            FolderDto folderDto = itemService.createFolder(spaceName, folderName, userEmail);
            return new ResponseEntity<>(folderDto, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
