package com.bank.bank_management.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.bank_management.dto.AddressRequest;
import com.bank.bank_management.entity.Address;
import com.bank.bank_management.dto.ResponseStructure;
import com.bank.bank_management.service.AddressService;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    // Get address by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Address>> getAddressById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Address retrieved successfully",
                        addressService.getAddressById(id)));
    }

    // Update address
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Address>> updateAddress(
            @PathVariable Long id,
            @Valid @RequestBody AddressRequest request) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Address updated successfully",
                        addressService.updateAddress(id, request)));
    }

    // Get address by bank ID
    @GetMapping("/bank/{bankId}")
    public ResponseEntity<ResponseStructure<Address>> getAddressByBankId(
            @PathVariable Long bankId) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Address retrieved successfully",
                        addressService.getAddressByBankId(bankId)));
    }

    // Search addresses by city
    @GetMapping("/search/city")
    public ResponseEntity<ResponseStructure<List<Address>>> getAddressesByCity(
            @RequestParam String city) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Addresses retrieved by city",
                        addressService.getAddressesByCity(city)));
    }

    // Search addresses by city and street
    @GetMapping("/search")
    public ResponseEntity<ResponseStructure<List<Address>>> getAddressesByCityAndStreet(
            @RequestParam String city,
            @RequestParam String street) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Addresses retrieved successfully",
                        addressService.getAddressesByCityAndStreet(
                                city, street)));
    }
}