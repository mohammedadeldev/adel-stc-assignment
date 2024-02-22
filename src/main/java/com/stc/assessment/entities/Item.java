package com.stc.assessment.entities;

import com.stc.assessment.enums.ItemType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType type;

    private String name;

    @Column(nullable = true)
    private Long parentId;

    @OneToMany(mappedBy = "parentId")
    private List<Item> children;

    @ManyToOne
    @JoinColumn(name = "permission_group_id")
    private PermissionGroup permissionGroup;
}
