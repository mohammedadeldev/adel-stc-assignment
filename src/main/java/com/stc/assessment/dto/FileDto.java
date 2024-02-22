package com.stc.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class FileDto {
    private Long id;
    private String name;
    private String itemType;
    // Include other file-related information as needed
}
