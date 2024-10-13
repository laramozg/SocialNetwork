package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.ProfileDto;
import org.example.mapper.ProfileMapper;
import org.example.model.Profile;
import org.example.model.User;
import org.example.repository.ProfileRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final ProfileMapper profileMapper = ProfileMapper.INSTANCE;

    @Autowired
    public ProfileService(UserRepository userRepository, ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    public ProfileDto getProfileByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Profile profile = profileRepository.findByUserId(user)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));

        return profileMapper.toDto(profile);
    }

    public ProfileDto createProfile(ProfileDto profileDto) {
        Profile profile = profileMapper.toEntity(profileDto);
        return profileMapper.toDto(profileRepository.save(profile));
    }

    public ProfileDto findById(Integer id) {
        return profileRepository.findById(id)
                .map(profileMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
    }

    public ProfileDto updateProfile(Integer id, ProfileDto updatedProfile) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profile not found"));
        profile.setFirstName(updatedProfile.getFirstName());
        profile.setLastName(updatedProfile.getLastName());
        profile.setEmail(updatedProfile.getEmail());
        return profileMapper.toDto(profileRepository.save(profile));
    }

    public void deleteProfile(Integer id) {
        findById(id);
        profileRepository.deleteById(id);
    }
}
