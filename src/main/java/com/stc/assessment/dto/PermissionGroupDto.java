package com.stc.assessment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionGroupDto {
    private Long id;
    private String groupName;
    private Set<PermissionDto> permissions;
}
