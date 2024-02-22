package com.stc.assessment.controllers;

import com.stc.assessment.dto.FolderDto;
import com.stc.assessment.dto.SpaceDto;
import com.stc.assessment.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spaces")
public class SpaceController {

    private final ItemService itemService;

    @Autowired
    public SpaceController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSpace(
            @RequestParam(value = "name", defaultValue = "stc-assessments") String spaceName
    ) {
        try {
            SpaceDto spaceDto = itemService.createSpaceWithPermissions(spaceName);
            return new ResponseEntity<>(spaceDto, HttpStatus.CREATED);
        } catch (Exception e) {
            // Handle exceptions
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/createFolder")
    public ResponseEntity<?> createFolder(
            @RequestParam String folderName,
            @RequestParam String userEmail,
            @RequestParam(value = "spaceName", defaultValue = "stc-assessments") String spaceName
    ) {
        try {
            FolderDto folder = itemService.createFolder(spaceName, folderName, userEmail);
            return new ResponseEntity<>(folder, HttpStatus.CREATED);
        } catch (SecurityException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

