package com.bank.bank_management.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.bank.bank_management.dto.BankRequest;
import com.bank.bank_management.entity.Bank;
import com.bank.bank_management.dto.ResponseStructure;
import com.bank.bank_management.service.BankService;

@RestController
@RequestMapping("/api/banks")
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    // Create a bank
    @PostMapping
    public ResponseEntity<ResponseStructure<Bank>> createBank(
            @Valid @RequestBody BankRequest request) {

        Bank bank = bankService.createBank(request);

        return ResponseEntity.status(201).body(
        		ResponseStructure.success("Bank created successfully", bank));
    }

    // Get all banks
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Bank>>> getAllBanks() {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Banks retrieved successfully",
                        bankService.getAllBanks()));
    }

    // Get bank by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Bank>> getBankById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Bank retrieved successfully",
                        bankService.getBankById(id)));
    }

    // Get bank by IFSC
    @GetMapping("/ifsc/{ifsc}")
    public ResponseEntity<ResponseStructure<Bank>> getBankByIfsc(
            @PathVariable String ifsc) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Bank retrieved successfully",
                        bankService.getBankByIfsc(ifsc)));
    }

    // Get bank by contact
    @GetMapping("/contact/{contact}")
    public ResponseEntity<ResponseStructure<Bank>> getBankByContact(
            @PathVariable String contact) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Bank retrieved successfully",
                        bankService.getBankByContact(contact)));
    }

    // Search banks by city
    @GetMapping("/search/city")
    public ResponseEntity<ResponseStructure<List<Bank>>> getBanksByCity(
            @RequestParam String city) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Banks retrieved by city",
                        bankService.getBanksByCity(city)));
    }

    // Search banks by street
    @GetMapping("/search/address")
    public ResponseEntity<ResponseStructure<List<Bank>>> getBanksByAddress(
            @RequestParam String street) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Banks retrieved by street",
                        bankService.getBanksByAddress(street)));
    }

    // Pagination
    @GetMapping("/page")
    public ResponseEntity<ResponseStructure<Page<Bank>>> getBanksWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Banks retrieved successfully",
                        bankService.getBanksWithPagination(page, size)));
    }

    // Sorting
    @GetMapping("/sort")
    public ResponseEntity<ResponseStructure<List<Bank>>> getBanksWithSorting(
            @RequestParam(defaultValue = "bankName") String field) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Banks sorted successfully",
                        bankService.getBanksWithSorting(field)));
    }

    // Update a bank and its address
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<Bank>> updateBank(
            @PathVariable Long id,
            @Valid @RequestBody BankRequest request) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Bank updated successfully",
                        bankService.updateBank(id, request)));
    }

    // Delete a bank
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<Void>> deleteBank(
            @PathVariable Long id) {

        bankService.deleteBank(id);

        return ResponseEntity.ok(
        		ResponseStructure.success("Bank deleted successfully", null));
    }
}