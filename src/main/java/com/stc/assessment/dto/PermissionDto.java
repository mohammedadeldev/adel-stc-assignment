package com.stc.assessment.dto;

import com.stc.assessment.enums.PermissionLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private Long id;
    private String userEmail;
    private PermissionLevel permissionLevel;
}
