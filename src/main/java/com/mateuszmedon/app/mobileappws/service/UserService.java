package com.mateuszmedon.app.mobileappws.service;

import com.mateuszmedon.app.mobileappws.shared.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto user);
}
