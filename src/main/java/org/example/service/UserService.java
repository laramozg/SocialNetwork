package org.example.service;

import org.example.dao.UserDao;
import org.example.dto.UserDto;
import org.example.mapper.UserMapper;
import org.example.model.User;

import java.sql.SQLException;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public UserDto getUserById(Integer id) throws SQLException {
        User user = userDao.findById(id);
        return userMapper.toDto(user);
    }
    public int saveUser(UserDto userDto) throws SQLException {
        User user = userMapper.toEntity(userDto);
        return userDao.save(user);
    }

    public void updateUser(UserDto userDto) throws SQLException {
        User user = userMapper.toEntity(userDto);
        userDao.update(user);
    }

    public void deleteUser(Integer id) throws SQLException {
        userDao.deleteById(id);
    }
}
