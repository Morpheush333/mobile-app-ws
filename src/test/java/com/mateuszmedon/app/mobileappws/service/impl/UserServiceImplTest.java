package com.mateuszmedon.app.mobileappws.service.impl;

import com.mateuszmedon.app.mobileappws.io.entity.UserEntity;
import com.mateuszmedon.app.mobileappws.io.repositories.PasswordResetTokenRepository;
import com.mateuszmedon.app.mobileappws.io.repositories.UserRepository;
import com.mateuszmedon.app.mobileappws.shared.Utils;
import com.mateuszmedon.app.mobileappws.shared.dto.AddressDto;
import com.mateuszmedon.app.mobileappws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    String userId = "d2a234fds6cs5vsg4g";
    String encryptedPassword = "p3q3jhh1jh7jh2vj2v";

    UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Mateusz");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("sd6vs7sd2vd8vsd8443");
    }

    @Test
    final void testGetUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Mateusz", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UsernameNotFoundException(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                ()-> {
                    userService.getUser("test@test.com");
        });
    }

    @Test
    final void testCreateUser(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("235kjb43");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);

        UserDto userDto = new UserDto();
        userDto.setAddresses(addresses);

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());

    }

}