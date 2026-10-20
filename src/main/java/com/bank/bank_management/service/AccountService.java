package com.bank.bank_management.service;

import java.math.BigDecimal;
import java.util.List;
import com.bank.bank_management.dto.AccountRequest;
import com.bank.bank_management.dto.DepositRequest;
import com.bank.bank_management.dto.TransferRequest;
import com.bank.bank_management.dto.WithdrawRequest;
import com.bank.bank_management.entity.Account;
import com.bank.bank_management.enums.AccountType;

public interface AccountService {
    Account createAccount(AccountRequest request);
    List<Account> getAllAccounts();
    Account getAccountById(Long id);
    Account getAccountByNumber(String accountNumber);
    List<Account> getAccountsByBankId(Long bankId);
    List<Account> getAccountsByType(AccountType accountType);
    List<Account> getAccountsWithBalanceGreaterThan(BigDecimal balance);
    List<Account> getAccountsWithSorting(String field);
    Account deposit(DepositRequest request);
    Account withdraw(WithdrawRequest request);
    void transfer(TransferRequest request);
    void deleteAccount(Long id);
}