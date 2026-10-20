package com.bank.bank_management.service;

import java.util.List;

import com.bank.bank_management.dto.AddressRequest;
import com.bank.bank_management.entity.Address;

public interface AddressService {
    Address getAddressById(Long id);
    Address updateAddress(Long id, AddressRequest request);
    Address getAddressByBankId(Long bankId);
    List<Address> getAddressesByCity(String city);
    List<Address> getAddressesByCityAndStreet(String city, String street);
}