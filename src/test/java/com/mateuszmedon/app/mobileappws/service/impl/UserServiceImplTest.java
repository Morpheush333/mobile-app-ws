package com.mateuszmedon.app.mobileappws.service.impl;

import com.mateuszmedon.app.mobileappws.exceptions.UserServiceException;
import com.mateuszmedon.app.mobileappws.io.entity.AddressEntity;
import com.mateuszmedon.app.mobileappws.io.entity.UserEntity;
import com.mateuszmedon.app.mobileappws.io.repositories.PasswordResetTokenRepository;
import com.mateuszmedon.app.mobileappws.io.repositories.UserRepository;
import com.mateuszmedon.app.mobileappws.shared.AmazonSES;
import com.mateuszmedon.app.mobileappws.shared.Utils;
import com.mateuszmedon.app.mobileappws.shared.dto.AddressDto;
import com.mateuszmedon.app.mobileappws.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    AmazonSES amazonSES;

    String userId = "d2a234fds6cs5vsg4g";
    String encryptedPassword = "p3q3jhh1jh7jh2vj2v";

    UserEntity userEntity;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFirstName("Mateusz");
        userEntity.setLastName("Medon");
        userEntity.setUserId(userId);
        userEntity.setEncryptedPassword(encryptedPassword);
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("sd6vs7sd2vd8vsd8443");
        userEntity.setAddresses(getAddressesEntity());
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
    final void testCreateUser_CreateUserServiceException(){

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Mateusz");
        userDto.setLastName("Medon");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        assertThrows(UserServiceException.class,
                ()-> {
                    userService.createUser(userDto);
                });
    }

    @Test
    final void testCreateUser(){

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn("235kjb43");
        when(utils.generateUserId(anyInt())).thenReturn(userId);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encryptedPassword);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        Mockito.doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

        UserDto userDto = new UserDto();
        userDto.setAddresses(getAddressesDto());
        userDto.setFirstName("Mateusz");
        userDto.setLastName("Medon");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");


        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
        assertEquals(userEntity.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(storedUserDetails.getAddresses().size(), userEntity.getAddresses().size());
        verify(utils, times(storedUserDetails.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("12345678");
        verify(userRepository, times(1)).save(any(UserEntity.class));


    }

    private List<AddressDto> getAddressesDto() {
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDto.setCity("Vancouver");
        addressDto.setCountry("Canada");
        addressDto.setPostalCode("ABC123");
        addressDto.setStreetName("123 Street name");

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity("Vancouver");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(addressDto);
        addresses.add(billingAddressDto);

        return addresses;
    }

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDto> addresses = getAddressesDto();

        Type listType = new TypeToken<List<AddressEntity>>() {
        }.getType();

        return new ModelMapper().map(addresses, listType);
    }

}