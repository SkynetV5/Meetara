package com.metara.metara.service;

import com.metara.metara.models.entity.Role;
import com.metara.metara.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    // CREATE
    public Role createRole(Role role){
        return roleRepository.save(role);
    }


    // READ

    public Optional<Role> getRoleById(Long id) {return roleRepository.findById(id);}

    public Role getRoleByIdOrThrow(Long id) { return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found with id" + id));}

    public Optional<Role> getRoleByName(String name) {return roleRepository.findByName(name);}

    public boolean getExistsByName(String name) { return roleRepository.existsByName(name);}

    public Set<Role> getFindRolesByUserId( Long userId) {return roleRepository.findRolesByUserId(userId);}

    public List<Role> getFindAllByOrderByNameAsc() { return roleRepository.findAllByOrderByNameAsc();}

    public List<Role> getFindByNameIn(Set<String> names) { return roleRepository.findByNameIn(names);}

    public List<Role> getAllRoles() {return roleRepository.findAll();}
    //UPDATE

    public Role updateRole(Long id, Role updatedRole){
        Role existingRole = getRoleByIdOrThrow(id);

        if(!updatedRole.getName().equals(existingRole.getName().toUpperCase())){
            if(roleRepository.existsByName(updatedRole.getName().toUpperCase())){
                throw new RuntimeException("Role already exists");
            }
            existingRole.setName(updatedRole.getName().toUpperCase());
        }


        existingRole.setUsers(updatedRole.getUsers());


        return roleRepository.save(existingRole);
    }

    public void deleteRole(Long id){
        if(!roleRepository.existsById(id)){
            throw new RuntimeException("Role not foud with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}
