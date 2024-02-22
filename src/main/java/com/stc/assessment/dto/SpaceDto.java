package com.stc.assessment.dto;

import com.stc.assessment.enums.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpaceDto {
    private Long id;
    private String name;
    private ItemType type;
    private PermissionGroupDto permissionGroup;
}
