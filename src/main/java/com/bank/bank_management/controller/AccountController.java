package com.bank.bank_management.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bank.bank_management.dto.AccountRequest;
import com.bank.bank_management.dto.DepositRequest;
import com.bank.bank_management.dto.TransferRequest;
import com.bank.bank_management.dto.WithdrawRequest;
import com.bank.bank_management.entity.Account;
import com.bank.bank_management.enums.AccountType;
import com.bank.bank_management.dto.ResponseStructure;
import com.bank.bank_management.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Create account
    @PostMapping
    public ResponseEntity<ResponseStructure<Account>> createAccount(
            @Valid @RequestBody AccountRequest request) {

        return ResponseEntity.status(201).body(
        		ResponseStructure.success(
                        "Account created successfully",
                        accountService.createAccount(request)));
    }

    // Get all accounts
    @GetMapping
    public ResponseEntity<ResponseStructure<List<Account>>> getAllAccounts() {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Accounts retrieved successfully",
                        accountService.getAllAccounts()));
    }

    // Get account by ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<Account>> getAccountById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Account retrieved successfully",
                        accountService.getAccountById(id)));
    }

    // Get account by account number
    @GetMapping("/number/{accountNumber}")
    public ResponseEntity<ResponseStructure<Account>> getAccountByNumber(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Account retrieved successfully",
                        accountService.getAccountByNumber(accountNumber)));
    }

    // Get accounts by bank ID
    @GetMapping("/bank/{bankId}")
    public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBankId(
            @PathVariable Long bankId) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Accounts retrieved by bank",
                        accountService.getAccountsByBankId(bankId)));
    }

    // Get accounts by type
    @GetMapping("/type/{accountType}")
    public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByType(
            @PathVariable AccountType accountType) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Accounts retrieved by type",
                        accountService.getAccountsByType(accountType)));
    }

    // Get accounts with balance greater than a specified amount
    @GetMapping("/search/balance")
    public ResponseEntity<ResponseStructure<List<Account>>> getAccountsByBalance(
            @RequestParam BigDecimal greaterThan) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Accounts retrieved by balance",
                        accountService.getAccountsWithBalanceGreaterThan(
                                greaterThan)));
    }

    // Sort accounts
    @GetMapping("/sort")
    public ResponseEntity<ResponseStructure<List<Account>>> getAccountsWithSorting(
            @RequestParam(defaultValue = "accountNumber") String field) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Accounts sorted successfully",
                        accountService.getAccountsWithSorting(field)));
    }

    // Deposit
    @PostMapping("/deposit")
    public ResponseEntity<ResponseStructure<Account>> deposit(
            @Valid @RequestBody DepositRequest request) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Deposit completed successfully",
                        accountService.deposit(request)));
    }

    // Withdraw
    @PostMapping("/withdraw")
    public ResponseEntity<ResponseStructure<Account>> withdraw(
            @Valid @RequestBody WithdrawRequest request) {

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Withdrawal completed successfully",
                        accountService.withdraw(request)));
    }

    // Transfer
    @PostMapping("/transfer")
    public ResponseEntity<ResponseStructure<Void>> transfer(
            @Valid @RequestBody TransferRequest request) {

        accountService.transfer(request);

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Transfer completed successfully", null));
    }

    // Delete account
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<Void>> deleteAccount(
            @PathVariable Long id) {

        accountService.deleteAccount(id);

        return ResponseEntity.ok(
        		ResponseStructure.success(
                        "Account deleted successfully", null));
    }
}