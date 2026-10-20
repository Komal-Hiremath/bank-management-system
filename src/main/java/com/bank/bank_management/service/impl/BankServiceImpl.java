package com.bank.bank_management.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bank_management.dto.AddressRequest;
import com.bank.bank_management.dto.BankRequest;
import com.bank.bank_management.entity.Address;
import com.bank.bank_management.entity.Bank;
import com.bank.bank_management.exception.DuplicateResourceException;
import com.bank.bank_management.exception.InvalidOperationException;
import com.bank.bank_management.exception.ResourceNotFoundException;
import com.bank.bank_management.repository.AddressRepository;
import com.bank.bank_management.repository.BankRepository;
import com.bank.bank_management.service.BankService;

@Service
@Transactional
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;
    private final AddressRepository addressRepository;

    public BankServiceImpl(
            BankRepository bankRepository,
            AddressRepository addressRepository) {

        this.bankRepository = bankRepository;
        this.addressRepository = addressRepository;
    }


    @Override
    public Bank createBank(BankRequest request) {

        if (bankRepository.existsByIfsc(request.getIfsc())) {
            throw new DuplicateResourceException("IFSC code already exists");
        }

        if (bankRepository.existsByContact(request.getContact())) {
            throw new DuplicateResourceException("Contact number already exists");
        }

        Bank bank = new Bank();
        bank.setBankName(request.getBankName());
        bank.setIfsc(request.getIfsc());
        bank.setBranchName(request.getBranchName());
        bank.setContact(request.getContact());

        AddressRequest addressRequest = request.getAddress();

        Address address = new Address();
        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPincode(addressRequest.getPincode());

        // Set both sides of the relationship
        bank.setAddress(address);
        address.setBank(bank);

        // Cascade saves the address along with the bank
        return bankRepository.save(bank);
    }
    
    
    @Override
    @Transactional(readOnly = true)
    public List<Bank> getAllBanks() {
        return bankRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Bank getBankById(Long id) {
        return bankRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Bank getBankByIfsc(String ifsc) {
        return bankRepository.findByIfsc(ifsc)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found with IFSC: " + ifsc));
    }

    @Override
    @Transactional(readOnly = true)
    public Bank getBankByContact(String contact) {
        return bankRepository.findByContact(contact)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found with contact: " + contact));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bank> getBanksByCity(String city) {
        return bankRepository.findByAddressCityIgnoreCase(city);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bank> getBanksByAddress(String street) {
        return bankRepository.findByAddressStreetContainingIgnoreCase(street);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Bank> getBanksWithPagination(int page, int size) {

        if (page < 0 || size <= 0) {
            throw new InvalidOperationException(
                    "Page must be non-negative and size must be positive");
        }

        return bankRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bank> getBanksWithSorting(String field) {

        if (!List.of("bankId", "bankName", "ifsc", "branchName", "contact")
                .contains(field)) {
            throw new InvalidOperationException(
                    "Invalid sorting field: " + field);
        }

        return bankRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    @Override
    public Bank updateBank(Long id, BankRequest request) {

        Bank bank = getBankById(id);

        bankRepository.findByIfsc(request.getIfsc())
                .filter(existing -> !existing.getBankId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "IFSC code already exists");
                });

        bankRepository.findByContact(request.getContact())
                .filter(existing -> !existing.getBankId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Contact number already exists");
                });

        bank.setBankName(request.getBankName());
        bank.setIfsc(request.getIfsc());
        bank.setBranchName(request.getBranchName());
        bank.setContact(request.getContact());

        Address address = bank.getAddress();

        if (address == null) {
            address = new Address();
            address.setBank(bank);
            bank.setAddress(address);
        }

        AddressRequest addressRequest = request.getAddress();

        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());
        address.setState(addressRequest.getState());
        address.setPincode(addressRequest.getPincode());

        return bankRepository.save(bank);
    }

    @Override
    public void deleteBank(Long id) {

        Bank bank = getBankById(id);

        if (bank.getAccounts() != null && !bank.getAccounts().isEmpty()) {
            throw new InvalidOperationException(
                    "Cannot delete a bank that has accounts");
        }

        bankRepository.delete(bank);
    }
}