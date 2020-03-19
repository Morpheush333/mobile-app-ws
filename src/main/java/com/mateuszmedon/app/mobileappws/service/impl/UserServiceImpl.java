package com.mateuszmedon.app.mobileappws.service.impl;

import com.mateuszmedon.app.mobileappws.UserRepository;
import com.mateuszmedon.app.mobileappws.io.entity.UserEntity;
import com.mateuszmedon.app.mobileappws.service.UserService;
import com.mateuszmedon.app.mobileappws.shared.Utils;
import com.mateuszmedon.app.mobileappws.shared.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Override
    public UserDto createUser(UserDto user) {

        if(userRepository.findByEmail(user.getEmail()) != null) throw new RuntimeException("Record (Email) already exists.");

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(user, userEntity);

        String publicUserId = utils.generateUserId(30);

        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword("test password");

        UserEntity storedUserEntity = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserEntity,returnValue);

        return returnValue;
    }
}
