package com.bank.bank_management.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bank_management.dto.AddressRequest;
import com.bank.bank_management.entity.Address;
import com.bank.bank_management.entity.Bank;
import com.bank.bank_management.exception.ResourceNotFoundException;
import com.bank.bank_management.repository.AddressRepository;
import com.bank.bank_management.repository.BankRepository;
import com.bank.bank_management.service.AddressService;

@Service
@Transactional
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final BankRepository bankRepository;

    public AddressServiceImpl(
            AddressRepository addressRepository,
            BankRepository bankRepository) {
        this.addressRepository = addressRepository;
        this.bankRepository = bankRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found with ID: " + id));
    }

    @Override
    public Address updateAddress(Long id, AddressRequest request) {

        Address address = getAddressById(id);

        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());

        return addressRepository.save(address);
    }

    @Override
    @Transactional(readOnly = true)
    public Address getAddressByBankId(Long bankId) {

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found with ID: " + bankId));

        return addressRepository.findByBankBankId(bank.getBankId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Address not found for bank ID: " + bankId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAddressesByCity(String city) {
        return addressRepository.findByCityIgnoreCase(city);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Address> getAddressesByCityAndStreet(
            String city, String street) {

        return addressRepository
                .findByCityIgnoreCaseAndStreetContainingIgnoreCase(
                        city, street);
    }
}