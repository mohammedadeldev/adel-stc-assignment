package com.stc.assessment.service;

import com.stc.assessment.dto.*;
import com.stc.assessment.entities.File;
import com.stc.assessment.entities.Item;
import com.stc.assessment.entities.Permission;
import com.stc.assessment.entities.PermissionGroup;
import com.stc.assessment.enums.ItemType;
import com.stc.assessment.enums.PermissionLevel;
import com.stc.assessment.repostories.FileRepository;
import com.stc.assessment.repostories.ItemRepository;
import com.stc.assessment.repostories.PermissionGroupRepository;
import com.stc.assessment.repostories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final PermissionGroupRepository permissionGroupRepository;
    private final PermissionRepository permissionRepository;
    private final FileRepository fileRepository;


    @Autowired
    public ItemService(
            ItemRepository itemRepository,
            PermissionGroupRepository permissionGroupRepository,
            PermissionRepository permissionRepository,
            FileRepository fileRepository
    ) {
        this.itemRepository = itemRepository;
        this.permissionGroupRepository = permissionGroupRepository;
        this.permissionRepository = permissionRepository;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public SpaceDto createSpaceWithPermissions(String spaceName) {
        // Check if the space already exists
        if (itemRepository.existsByName(spaceName)) {
            throw new IllegalStateException("Space already exists");
        }

        // Create a new space item
        Item space = new Item();
        space.setType(ItemType.SPACE);
        space.setName(spaceName);

        // Create a new permission group for the space
        PermissionGroup adminGroup = new PermissionGroup();
        adminGroup.setGroupName("adminGroup");
        permissionGroupRepository.save(adminGroup);

        // Assign the group to the space
        space.setPermissionGroup(adminGroup);

        // Save the space item
        itemRepository.save(space);

        // Create permissions for the group
        createPermissionForGroup(adminGroup, "userView@example.com", PermissionLevel.VIEW);
        createPermissionForGroup(adminGroup, "userEdit@example.com", PermissionLevel.EDIT);

        SpaceDto spaceDto = new SpaceDto();
        spaceDto.setId(space.getId());
        spaceDto.setName(space.getName());
        spaceDto.setType(space.getType());

        PermissionGroupDto permissionGroupDto = new PermissionGroupDto();
        permissionGroupDto.setId(space.getPermissionGroup().getId());
        permissionGroupDto.setGroupName(space.getPermissionGroup().getGroupName());

        Set<PermissionDto> permissionDtos = space.getPermissionGroup().getPermissions().stream()
                .map(permission -> new PermissionDto(permission.getId(), permission.getUserEmail(), permission.getPermissionLevel()))
                .collect(Collectors.toSet());

        permissionGroupDto.setPermissions(permissionDtos);
        spaceDto.setPermissionGroup(permissionGroupDto);
        return spaceDto;
    }

    @Transactional
    public FolderDto createFolder(String spaceName, String folderName, String userEmail) {
        // Find the space by name
        Item space = itemRepository.findByName(spaceName)
                .orElseThrow(() -> new IllegalStateException("Space does not exist"));

        // Check if the user has EDIT permission for this space
        hasPermission(userEmail, space);

        // Check if the folder already exists under the space
        if (itemRepository.existsByNameAndParentId(folderName, space.getId())) {
            throw new IllegalStateException("Folder already exists under this space");
        }

        // Create and save the new folder
        Item folder = new Item();
        folder.setName(folderName);
        folder.setType(ItemType.FOLDER);
        folder.setParentId(space.getId());
        folder.setPermissionGroup(space.getPermissionGroup()); // Inherit permission group from space
        itemRepository.save(folder);

        return new FolderDto(folder.getId(), folder.getName(), spaceName);
    }

    @Transactional
    public FileDto createFile(String folderName, String fileName, String userEmail, MultipartFile fileContent) throws IOException {

        // Find the folder by name
        Item folder = itemRepository.findByName(folderName)
                .orElseThrow(() -> new IllegalStateException("Folder does not exist"));

        // Check if the user has EDIT permission for this folder
        hasPermission(userEmail, folder);

        // Check if the file already exists under the folder
        if (itemRepository.existsByNameAndParentId(fileName, folder.getId())) {
            throw new IllegalStateException("File already exists under this folder");
        }

        // Create and save the new file item
        Item file = new Item();
        file.setName(fileName);
        file.setType(ItemType.FILE);
        file.setParentId(folder.getId());
        file.setPermissionGroup(folder.getPermissionGroup()); // Inherit permission group from folder
        itemRepository.save(file);

        // Convert MultipartFile to bytes safely
        byte[] contentBytes;
        try {
            contentBytes = fileContent.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file content", e);
        }

        // Call saveFileContent
        saveFileContent(file.getId(), contentBytes);

        return new FileDto(file.getId(), fileName, folderName);
    }

    @Transactional(readOnly = true)
    public FileDto getFileMetadata(Long fileId, String userEmail) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new RuntimeException("File not found."));

        hasPermission(userEmail, file.getItem());

        Item item= itemRepository.findById(file.getItem().getId())
                .orElseThrow(() -> new RuntimeException("File not found."));

        return new FileDto(file.getId(), item.getName(), item.getType().name());
    }


    public File getFile(Long fileId, String userEmail) {

        File file = fileRepository.findById(fileId).orElseThrow(() -> new IllegalStateException("File not found."));

        hasPermission(userEmail, file.getItem());

        return file;
    }

    private void hasPermission(String userEmail, Item file) {
        boolean hasEditPermission = permissionRepository.findByUserEmailAndItemIdAndPermissionLevel(
                        userEmail, file.getId(), PermissionLevel.EDIT)
                .isPresent();

        if (!hasEditPermission) {
            throw new SecurityException("User does not have EDIT permission for this file.");
        }
    }

    private void saveFileContent(Long itemId, byte[] content) {
        File fileEntity = new File();
        fileEntity.setBinaryFile(content);

        // Link the file entity with the item entity
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalStateException("Item not found for ID: " + itemId));
        fileEntity.setItem(item);

        fileRepository.save(fileEntity);
    }


    private void createPermissionForGroup(PermissionGroup group, String userEmail, PermissionLevel level) {
        Permission permission = new Permission();
        permission.setUserEmail(userEmail);
        permission.setPermissionLevel(level);
        permission.setGroup(group);
        permissionRepository.save(permission);

        // Add the new permission to the group's permissions set
        group.getPermissions().add(permission);
    }
}
