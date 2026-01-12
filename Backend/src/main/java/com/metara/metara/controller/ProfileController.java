package com.metara.metara.controller;

import com.metara.metara.models.dto.ProfileDto;
import com.metara.metara.models.entity.Profile;
import com.metara.metara.models.entity.User;
import com.metara.metara.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/profiles")
@Tag(name="Profile Management", description = "API Profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;


    //CREATE

    @Operation(summary = "Create new Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Proifle created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or profile already exists")
    })
    @PostMapping("/create")
    public ResponseEntity<?> createProfile(@Valid @RequestBody ProfileDto profileDto){
        try{
            Profile newProfile = new Profile();
            newProfile.setFirstName(profileDto.getFirstName());
            newProfile.setLastName(profileDto.getLastName());
            newProfile.setPhoneNumber(profileDto.getPhoneNumber());
            newProfile.setUser(profileDto.getUser());

            Profile createProfile = profileService.createProfile(newProfile);
            return ResponseEntity.status(HttpStatus.CREATED).body(createProfile);
        }
        catch (RuntimeException e){
            return  ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Get all profiles", description = "Returns all profiles without pagination")
    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles(){
        List<Profile> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    @Operation(summary = "Get profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile found"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProfileById(@Parameter(description = "Profile ID") @PathVariable Long id){
        try{
            Profile profile = profileService.getProfileByIdOrThrow(id);
            return ResponseEntity.ok(profile);
        }
        catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));

        }
    }

    @Operation(summary = "Update profile", description = "Updates profile information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "Profile ID")
            @PathVariable Long id,
            @RequestBody Profile updatedProfile) {
        try {
            Profile profile = profileService.updateProfile(id, updatedProfile);
            return ResponseEntity.ok(profile);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(summary = "Delete profile", description = "Deletes profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProfile(
            @Parameter(description = "Profile ID")
            @PathVariable Long id) {
        try {
            profileService.deleteProfile(id);
            return ResponseEntity.ok(Map.of("message", "Profile deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }


}
