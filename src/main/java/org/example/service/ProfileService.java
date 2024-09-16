package org.example.service;

import org.example.dao.ProfileDao;
import org.example.dto.ProfileDto;
import org.example.mapper.ProfileMapper;
import org.example.model.Profile;

import java.sql.SQLException;
import java.util.Optional;

public class ProfileService {
    private final ProfileDao profileDao;
    private final ProfileMapper profileMapper = ProfileMapper.INSTANCE;

    public ProfileService(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    public ProfileDto getProfileByUserId(Integer userId) throws SQLException {
        Profile profile = profileDao.findByUserId(userId);
        return profileMapper.toDto(profile);
    }

    public int saveProfile(ProfileDto profileDto) throws SQLException {
        Profile profile = profileMapper.toEntity(profileDto);
        return profileDao.save(profile);
    }

    public void updateProfile(ProfileDto profileDto) throws SQLException {
        Profile profile = profileMapper.toEntity(profileDto);
        profileDao.update(profile);
    }

    public void deleteProfile(Integer userId) throws SQLException {
        profileDao.deleteByUserId(userId);
    }
}
