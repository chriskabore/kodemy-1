package com.bt.dev.kodemy.users.services;

import com.bt.dev.kodemy.users.exceptions.InvalidPermissionDataException;
import com.bt.dev.kodemy.users.exceptions.PermissionInUseException;
import com.bt.dev.kodemy.users.exceptions.PermissionNotFoundException;
import com.bt.dev.kodemy.users.model.Permission;
import com.bt.dev.kodemy.users.repositories.PermissionRepository;
import com.google.common.base.Strings;
import dtos.PermissionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PermissionService {

    private PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository){
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getPermissionsList(){
        return permissionRepository.findAll();
    }

    public Permission getPermissionByKey(String permissionKey){
        if(Strings.isNullOrEmpty(permissionKey)){
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }

        Optional<Permission> optionalPermission = permissionRepository.findByPermission(permissionKey);
        if (optionalPermission.isPresent()) {
            return optionalPermission.get();
        }
        throw new PermissionNotFoundException(String.format("Permission not found for permission key = %s",
                permissionKey));
    }

    @Transactional
    public Permission createPermission(PermissionDTO permissionDTO){

        if (permissionDTO == null) {
            throw new InvalidPermissionDataException("Permission data cannot be null or empty");
        }

        String permissionKey = permissionDTO.getPermission();

        if (Strings.isNullOrEmpty(permissionKey)) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }

        // check permission
        Optional<Permission> optionalPermission = permissionRepository.findByPermission(permissionKey);
        if (optionalPermission.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission %s already existing with the same key with Id = %s",
                    permissionKey, optionalPermission.get().getPermissionId()));
        }

        Permission permission = new Permission();
        permission.setPermission(permissionKey);

        permission.setNote(permissionDTO.getNote());
        permission.setEnabled(permissionDTO.isEnabled());

        Permission createdPermission = permissionRepository.save(permission);

        log.info(String.format("Created permission %s with Id = %s", permissionKey, createdPermission.getPermissionId()));
        return createdPermission;
    }

    @Transactional
    public Permission updatePermission(PermissionDTO permissionDTO){
        if (permissionDTO == null) {
            throw new InvalidPermissionDataException("Permission data cannot be null");
        }

        Long permissionId = permissionDTO.getPermissionId();
        Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);
        if (!optionalPermission.isPresent()) {
            throw new PermissionNotFoundException(String.format("The permission with the id = %s has not been found",
                    permissionId));
        }

        Permission permission = optionalPermission.get();
        String permissionKey = permissionDTO.getPermission();

        // check if exists a different configuration with the same permissionKey
        Optional<Permission> permissionByKeyOpt = permissionRepository.findByPermission(permissionKey);
        if (permissionByKeyOpt.isPresent()) {
            Permission permission1 = permissionByKeyOpt.get();
            if (!permission1.getPermissionId().equals(permission.getPermissionId())) {
                throw new InvalidPermissionDataException(String.format("Exists already a permission with the key %s." +
                        " Use another key", permissionKey));
            }
        }

        // update permission
        permission.setPermission(permissionDTO.getPermission());
        permission.setEnabled(permissionDTO.isEnabled());
        permission.setNote(permissionDTO.getNote());

        Permission updatedPermission = permissionRepository.save(permission);
        log.info(String.format("Permission with id = %s has been updated.", permission.getPermissionId()));

        return updatedPermission;
    }


    @Transactional
    public void deletePermissionByKey(String permissionKey) {
        if (Strings.isNullOrEmpty(permissionKey)) {
            throw new InvalidPermissionDataException("Permission key cannot be null or empty");
        }

        // check permission
        Optional<Permission> optionalPermission = permissionRepository.findByPermission(permissionKey);
        if (!optionalPermission.isPresent()) {
            throw new PermissionNotFoundException(String.format("Permission %s not found", permissionKey));
        }

        Permission permission = optionalPermission.get();

        // check usage
        Long permissionUsesCount = permissionRepository.countPermissionUsage(permission.getPermissionId());
        if (permissionUsesCount > 0) {
            // permission cannot be deleted
            throw new PermissionInUseException(String.format("The permission with key %s is in used (%s configuration rows)",
                    permissionKey, permissionUsesCount));
        }

        permissionRepository.delete(permission);

        log.info(String.format("Deleted permission with key %s", permission.getPermission()));
    }

}
