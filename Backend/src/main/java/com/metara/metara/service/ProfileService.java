package com.metara.metara.service;

import com.metara.metara.models.entity.Profile;
import com.metara.metara.repository.ProfileRepository;
import com.metara.metara.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;

    // CREATE
    public Profile createProfile(Profile profile){
        if(profile.getUser() != null){
            if (userRepository.existsByEmail(profile.getUser().getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            if (userRepository.existsByUsername(profile.getUser().getUsername())) {
                throw new RuntimeException("Username already exists");
            }
        }

        return profileRepository.save(profile);
    }

    // READ

    public Optional<Profile> getProfileById(Long id) { return  profileRepository.findByUserId(id);}

    public Profile getProfileByIdOrThrow(Long id){return profileRepository.findByUserId(id).orElseThrow(() -> new RuntimeException("User not found with id" + id));}

    public List<Profile> getAllProfiles(){ return profileRepository.findAll();}


    // UPDATE
    public Profile updateProfile(Long id, Profile updatedProfile){
        Profile existingProfile = getProfileByIdOrThrow(id);

        if(updatedProfile.getUser() != null && !updatedProfile.getUser().equals(existingProfile.getUser())){
            if(profileRepository.existsByUser(updatedProfile.getUser())){
                throw new RuntimeException("User already have Profile");
            }
            existingProfile.setUser(updatedProfile.getUser());
        }

        existingProfile.setFirstName(updatedProfile.getFirstName());
        existingProfile.setLastName(updatedProfile.getLastName());
        existingProfile.setPhoneNumber(updatedProfile.getPhoneNumber());

        return profileRepository.save(existingProfile);
    }

    // DELETE
    public void deleteProfile(Long id){
        if(!profileRepository.existsById(id)){
            throw new RuntimeException("Profile not found with id:" + id);
        }
        profileRepository.deleteById(id);
    }
}
