package com.mateuszmedon.app.mobileappws.service;

import com.mateuszmedon.app.mobileappws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {

    List<AddressDto> getAddresses(String userId);
    AddressDto getAddress (String addressId);
}
