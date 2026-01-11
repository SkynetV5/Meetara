package com.metara.metara.service;


import com.metara.metara.models.entity.Role;
import com.metara.metara.models.entity.User;
import com.metara.metara.repository.RoleRepository;
import com.metara.metara.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;


    // CREATE
    public User createUser(User user){
        validateUserData(user);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if(user.getRoles() == null || user.getRoles().isEmpty()){
            Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Default role not found"));
            user.setRoles(Set.of(userRole));
        }
        return  userRepository.save(user);
    }

    public User registerUser(String email, String username, String password) {
        validateRegistrationData(email, username);

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = roleRepository.findByName("USER").orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRoles(Set.of(userRole));

        return userRepository.save(user);

    }

    // READ

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }
    public User getUserByIdOrThrow(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id" + id));
    }

    public Optional<User> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Page<User> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public List<User> getAllUsersByCreatedAt(LocalDateTime date){
        return userRepository.findByCreatedAtAfter(date);
    }

    public Page<User> getEnabledUsers(boolean enable, Pageable pageable){
        return  userRepository.findByEnabled(enable,pageable);
    }

    // UPDATE
    public User updateUser(Long id, User updatedUser){
        User existingUser = getUserByIdOrThrow(id);

        if(updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())){
            if(userRepository.existsByEmail(updatedUser.getEmail())){
                throw new RuntimeException("Email already exists");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }

        if(updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())){
            if(userRepository.existsByUsername(updatedUser.getUsername())){
                throw  new RuntimeException("Username already exists");
            }
        }
        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()){
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(existingUser);
    }

    public User changePassword(Long userId, String oldPassword, String newPassword){
        User user = getUserByIdOrThrow(userId);

        if(!passwordEncoder.matches(oldPassword,user.getPassword())){
            throw  new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.save(user);
    }

    public User enabledUser(Long userId){
        User user = getUserByIdOrThrow(userId);
        user.setEnabled(true);
        return userRepository.save(user);
    }

    public User unenabledUser(Long userId){
        User user = getUserByIdOrThrow(userId);
        user.setEnabled(false);
        return userRepository.save(user);
    }

    // DELETE

    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new RuntimeException("User not found with id:" + id);
        }
        userRepository.deleteById(id);
    }

    // ROLE MANAGEMENT

    public User assignRoles(Long userId, Set<String>roleNames){
        User user = getUserByIdOrThrow(userId);
        List<Role> roles = roleRepository.findByNameIn(roleNames);
        if(roles.isEmpty()){
            throw new RuntimeException("No valid roles found");
        }
        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    public User addRole(Long userId, String roleName){
        User user = getUserByIdOrThrow(userId);
        Role role = roleRepository.findByName(roleName).orElseThrow(()-> new RuntimeException("Role not found " + roleName));
        user.getRoles().add(role);
        return userRepository.save(user);
    }
    public User removeRole(Long userId, String roleName) {
        User user = getUserByIdOrThrow(userId);
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        user.getRoles().remove(role);
        return userRepository.save(user);
    }

    public Set<Role> getUserRoles(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return user.getRoles();
    }

    public boolean hasRole(Long userId, String roleName) {
        User user = getUserByIdOrThrow(userId);
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    // VALIDATION

    private void validateUserData(User user, Locale locale){
        if (user.getEmail() == null || user.getEmail().isEmpty()){
            throw new RuntimeException(messageSource.getMessage("validation.email.required", null, locale));
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new RuntimeException(messageSource.getMessage("validation.username.required", null, locale));
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException(messageSource.getMessage("validation.email.exists", null, locale));
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException(messageSource.getMessage("validation.username.exists", null, locale));
        }
    }

    private void validateRegistrationData(String email, String username) {
        if (email == null || email.isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
    }

    //STATISTICS

    public long getTotalUsers() {
        return userRepository.count();
    }



}
