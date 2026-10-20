package com.bank.bank_management.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.bank_management.entity.Account;
import com.bank.bank_management.enums.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByBankBankId(Long bankId);
    List<Account> findByAccountType(AccountType accountType);
    List<Account> findByBalanceGreaterThan(BigDecimal balance);
}