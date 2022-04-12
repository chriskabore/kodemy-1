package com.bt.dev.kodemy.users.services;

import com.bt.dev.kodemy.users.exceptions.*;
import com.bt.dev.kodemy.users.model.Permission;
import com.bt.dev.kodemy.users.model.Role;
import com.bt.dev.kodemy.users.repositories.PermissionRepository;
import com.bt.dev.kodemy.users.repositories.RoleRepository;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {

    private RoleRepository roleRepository;
    private PermissionRepository permissionRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository ){
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public List<Role> getRoleList(){
        return roleRepository.findAll();
    }

    public Role getRoleById(Long roleId){

        if (roleId == null) {
            throw new InvalidRoleIdentifierException("Id role cannot be null");
        }
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(optionalRole.isPresent()){
            return optionalRole.get();
        }
        throw new RoleNotFoundException(String.format("Role not found for Id = %s", roleId));
    }

    public static void validateRoleName(String roleName) {
        if (Strings.isNullOrEmpty(roleName)) {
            throw new InvalidRoleDataException(String.format("Invalid role name: %s", roleName));
        }
    }

    @Transactional
    public Role createRole(String roleName){
        validateRoleName(roleName);

        if(roleRepository.findByRoleName(roleName).isPresent()){
            String errorMessage = String.format("The role %s already exists", roleName);
            log.error(errorMessage);
            throw new RoleInUseException(errorMessage);
        }

        Role newRole = new Role(roleName);
        newRole = roleRepository.save(newRole);
        return newRole;
    }

    @Transactional
    public void deleteRole(Long roleId){

        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(!optionalRole.isPresent()){
            String errorMessage = String.format("Role not found for id = %s. Role cannot be deleted", roleId);
            log.error(errorMessage);
            throw new RoleNotFoundException(errorMessage);
        }

        Role roleToBeDeleted = optionalRole.get();

        // check that role is not in use
        Long roleUsesCount = roleRepository.countRoleUsage(roleId);
        if(roleUsesCount>0){
            String errorMessage = String.format("The role %s %s is in use (%s users_roles configuration rows)" +
                    " and cannot be deleted", roleToBeDeleted.getRoleId(),
                    roleToBeDeleted.getRoleName(), roleUsesCount);
            log.error(errorMessage);
            throw new RoleInUseException(errorMessage);
        }

        roleRepository.deleteById(roleId);
        log.info(String.format("Role %s has been deleted successfully", roleId));

    }

    public static void validatePermissionKey(String permissionKey){
        if(Strings.isNullOrEmpty(permissionKey)){
            throw new InvalidPermissionDataException("Permission cannot be null or empty");
        }
    }

    public Role addPermissionToRole(Long roleId, String permissionKey){
        validatePermissionKey(permissionKey);

        // get role
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(!optionalRole.isPresent()){
            String errorMesage = String.format("Role not found with Id = %s", roleId);
            log.error(errorMesage);
            throw new RoleNotFoundException(errorMesage);
        }
        Role role = optionalRole.get();

        // check if the permission exists
        Permission permission;

        Optional<Permission> optionalPermission = permissionRepository.findByPermission(permissionKey);
        if(optionalPermission.isPresent()){
            permission = optionalPermission.get();
        }else{
            permission = new Permission();
            permission.setPermission(permissionKey);
            permission = permissionRepository.save(permission);
        }

        // check if role already contains the given permission
        if(role.getPermissions().contains(permission)){
            String errorMessage = String.format("The permission %s has been already" +
                    " associated on the role %s", permission.getPermission(), role.getRoleName());
            log.error(errorMessage);
            throw new InvalidPermissionDataException(errorMessage);
        }

        role.getPermissions().add(permission);
        roleRepository.save(role);

        log.info(String.format("Added permission %s on role id = %s", permissionKey, roleId));
        return roleRepository.findById(roleId).get();
    }

    @Transactional
    public Role removePermissionFromRole(Long roleId, String permissionKey){
        validatePermissionKey(permissionKey);
        // check role existance
        Optional<Role> optionalRole = roleRepository.findById(roleId);
        if(!optionalRole.isPresent()){
            String errorMessage = String.format("Role not found with Id = %s", roleId);
            log.error(errorMessage);
            throw new RoleNotFoundException(errorMessage);
        }

        Role role = optionalRole.get();

        // check permission's existance
        Optional<Permission> optionalPermission = permissionRepository.findByPermission(permissionKey);
        if(!optionalPermission.isPresent()){
            String errorMessage = String.format("Permission not found with Id = %s on role %s",
                    permissionKey, roleId);
            log.error(errorMessage);
            throw new PermissionNotFoundException(errorMessage);
        }

        Permission permission = optionalPermission.get();

        role.getPermissions().remove(permission);
        roleRepository.save(role);

        log.info(String.format("Removed permission %s from role id = %s", permissionKey, roleId));
        return roleRepository.findById(roleId).get();
    }


    
}
