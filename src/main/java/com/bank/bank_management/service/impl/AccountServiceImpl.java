package com.bank.bank_management.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.bank_management.dto.AccountRequest;
import com.bank.bank_management.dto.DepositRequest;
import com.bank.bank_management.dto.TransferRequest;
import com.bank.bank_management.dto.WithdrawRequest;
import com.bank.bank_management.entity.Account;
import com.bank.bank_management.entity.Bank;
import com.bank.bank_management.enums.AccountType;
import com.bank.bank_management.exception.DuplicateResourceException;
import com.bank.bank_management.exception.InsufficientBalanceException;
import com.bank.bank_management.exception.InvalidOperationException;
import com.bank.bank_management.exception.ResourceNotFoundException;
import com.bank.bank_management.repository.AccountRepository;
import com.bank.bank_management.repository.BankRepository;
import com.bank.bank_management.service.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;

    @Value("${bank.minimum-balance.savings:0.00}")
    private BigDecimal savingsMinimumBalance;

    @Value("${bank.minimum-balance.current:0.00}")
    private BigDecimal currentMinimumBalance;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            BankRepository bankRepository) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    // Create an account
    @Override
    public Account createAccount(AccountRequest request) {

        if (accountRepository.existsByAccountNumber(
                request.getAccountNumber())) {
            throw new DuplicateResourceException(
                    "Account number already exists");
        }

        Bank bank = bankRepository.findById(request.getBankId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Bank not found with ID: "
                                        + request.getBankId()));

        BigDecimal balance = request.getBalance();

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException(
                    "Initial balance cannot be negative");
        }

        validateMinimumBalance(request.getAccountType(), balance);

        Account account = new Account();
        account.setAccountNumber(request.getAccountNumber());
        account.setAccountHolder(request.getAccountHolder());
        account.setAccountType(request.getAccountType());
        account.setBalance(balance);
        account.setBank(bank);

        return accountRepository.save(account);
    }

    // Retrieve all accounts
    @Override
    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Retrieve account by ID
    @Override
    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found with ID: " + id));
    }

    // Retrieve account by account number
    @Override
    @Transactional(readOnly = true)
    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Account not found: " + accountNumber));
    }

    // Retrieve accounts belonging to a bank
    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByBankId(Long bankId) {

        if (!bankRepository.existsById(bankId)) {
            throw new ResourceNotFoundException(
                    "Bank not found with ID: " + bankId);
        }

        return accountRepository.findByBankBankId(bankId);
    }

    // Retrieve accounts by type
    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsByType(AccountType accountType) {
        return accountRepository.findByAccountType(accountType);
    }

    // Retrieve accounts whose balance exceeds a specified amount
    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsWithBalanceGreaterThan(
            BigDecimal balance) {

        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOperationException(
                    "Search balance cannot be negative");
        }

        return accountRepository.findByBalanceGreaterThan(balance);
    }

    // Sort accounts by a permitted field
    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsWithSorting(String field) {

        List<String> allowedFields = List.of(
                "accountId",
                "accountNumber",
                "accountHolder",
                "accountType",
                "balance");

        if (!allowedFields.contains(field)) {
            throw new InvalidOperationException(
                    "Invalid sorting field: " + field);
        }

        return accountRepository.findAll(
                Sort.by(Sort.Direction.ASC, field));
    }

    // Deposit money
    @Override
    public Account deposit(DepositRequest request) {

        Account account = getAccountByNumber(
                request.getAccountNumber());

        BigDecimal amount = request.getAmount();

        validatePositiveAmount(amount, "Deposit");

        account.setBalance(account.getBalance().add(amount));

        return accountRepository.save(account);
    }

    // Withdraw money
    @Override
    public Account withdraw(WithdrawRequest request) {

        Account account = getAccountByNumber(
                request.getAccountNumber());

        BigDecimal amount = request.getAmount();

        validatePositiveAmount(amount, "Withdrawal");

        BigDecimal newBalance = account.getBalance().subtract(amount);

        BigDecimal minimumBalance =
                getMinimumBalance(account.getAccountType());

        if (newBalance.compareTo(minimumBalance) < 0) {
            throw new InsufficientBalanceException(
                    "Withdrawal would violate the minimum balance requirement");
        }

        account.setBalance(newBalance);

        return accountRepository.save(account);
    }

    // Transfer money between two accounts
    @Override
    public void transfer(TransferRequest request) {

        String senderNumber = request.getSenderAccountNumber();
        String receiverNumber = request.getReceiverAccountNumber();

        if (senderNumber.equals(receiverNumber)) {
            throw new InvalidOperationException(
                    "Sender and receiver accounts must be different");
        }

        BigDecimal amount = request.getAmount();
        validatePositiveAmount(amount, "Transfer");

        Account sender = getAccountByNumber(senderNumber);
        Account receiver = getAccountByNumber(receiverNumber);

        BigDecimal senderNewBalance =
                sender.getBalance().subtract(amount);

        BigDecimal minimumBalance =
                getMinimumBalance(sender.getAccountType());

        if (senderNewBalance.compareTo(minimumBalance) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance for transfer");
        }

        sender.setBalance(senderNewBalance);
        receiver.setBalance(receiver.getBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    // Delete an account
    @Override
    public void deleteAccount(Long id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
    }

    // Validate that the amount is greater than zero
    private void validatePositiveAmount(
            BigDecimal amount, String operation) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOperationException(
                    operation + " amount must be greater than zero");
        }
    }

    // Validate the opening balance
    private void validateMinimumBalance(
            AccountType accountType, BigDecimal balance) {

        BigDecimal minimumBalance = getMinimumBalance(accountType);

        if (balance.compareTo(minimumBalance) < 0) {
            throw new InvalidOperationException(
                    "Initial balance is below the minimum required balance");
        }
    }

    // Return the configured minimum balance for an account type
    private BigDecimal getMinimumBalance(AccountType accountType) {

        if (accountType == AccountType.SAVINGS) {
            return savingsMinimumBalance;
        }

        if (accountType == AccountType.CURRENT) {
            return currentMinimumBalance;
        }

        return BigDecimal.ZERO;
    }
}