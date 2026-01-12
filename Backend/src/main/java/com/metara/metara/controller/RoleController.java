package com.metara.metara.controller;

import com.metara.metara.models.dto.RoleDto;
import com.metara.metara.models.entity.Role;
import com.metara.metara.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("api/roles")
@Tag(name= "Role Management", description = "API Users")
public class RoleController {

    @Autowired
    private RoleService roleService;

    // CREATE
    @Operation(summary = "Create new Role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Role created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or role already exists")
    })
    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody RoleDto roleDto){
        try{
            Role newRole = new Role();
            newRole.setName(roleDto.getName().toUpperCase());
            newRole.setUsers(roleDto.getUsers());

            Role createRole = roleService.createRole(newRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(createRole);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all roles")
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> roles  = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @Operation(summary = "Get role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role found"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@Parameter(description = "Role ID") @PathVariable Long id){
        try{
            Role role = roleService.getRoleByIdOrThrow(id);
            return ResponseEntity.ok(role);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/exists")
    public boolean existsByName(@RequestParam String name) {
        return roleService.getExistsByName(name.toUpperCase());
    }


    @GetMapping("/user/{userId}")
    public Set<Role> getRolesByUserId(@PathVariable Long userId) {
        return roleService.getFindRolesByUserId(userId);
    }

    @GetMapping("/orderByNameAsc")
    public List<Role> getAllRolesByOrderByNameAsc() {
        return roleService.getFindAllByOrderByNameAsc();
    }
    @PostMapping("/by-names")
    public List<Role> getRolesByNames(@RequestBody Set<String> names) {
        return roleService.getFindByNameIn(names);
    }

    @Operation(summary = "Search roles by name")
    @GetMapping("/name/{name}")
    public ResponseEntity<?> getRoleByName(
            @Parameter(description = "Name")
            @PathVariable String name
    ){
        return roleService.getRoleByName(name.toUpperCase())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update role", description = "Updates role information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
        @Parameter(description = "Role ID")
        @PathVariable Long id,
        @RequestBody Role updatedRole
        ){
        try{
            Role role = roleService.updateRole(id, updatedRole);
            return ResponseEntity.ok(role);
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete role", description = "Deletes role by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(
            @Parameter(description = "Role ID")
            @PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok(Map.of("message", "Role deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
