package com.stc.assessment.controllers;

import com.stc.assessment.dto.FileDto;
import com.stc.assessment.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class FileGraphQLController {

    private final ItemService itemService;

    @Autowired
    public FileGraphQLController(ItemService itemService) {
        this.itemService = itemService;
    }

    @QueryMapping
    public FileDto fileMetadata(@Argument Long fileId, @Argument String userEmail) {
        return itemService.getFileMetadata(fileId, userEmail);
    }
}
