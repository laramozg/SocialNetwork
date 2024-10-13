package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + id + "' not found"));
    }

    public UserDto saveUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(user));
    }

    public UserDto updateUser(Integer id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with username '" + id + "' not found"));
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        return userMapper.toDto(userRepository.save(user));
    }

    public void deleteUser(Integer id)  {
        getUserById(id);
        userRepository.deleteById(id);
    }
}