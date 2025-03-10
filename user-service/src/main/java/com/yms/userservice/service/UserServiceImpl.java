package com.yms.userservice.service;

import com.yms.userservice.dto.UserDto;
import com.yms.userservice.entities.User;
import com.yms.userservice.entities.UserRole;
import com.yms.userservice.exceptions.EmailAlreadyUsedException;
import com.yms.userservice.exceptions.UserNotFoundException;
import com.yms.userservice.mapper.UserMapper;
import com.yms.userservice.repository.UserRepository;
import com.yms.userservice.service.abstracts.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto save(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException("Email Already Exists");
        }

        user.setRole(UserRole.TEAM_LEADER);
        user.setActive(true);

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found!"));

    }

    @Override
    public void deleteById(Long id) {
        if (userRepository.findById(id).isPresent()) {
            throw new UserNotFoundException("User with ID " + id + " not found!");
        }
        userRepository.deleteById(id);
    }


    @Override
    public List<UserDto> findAll() {
        return userMapper.toUserDto(userRepository.findAll());
    }
}
