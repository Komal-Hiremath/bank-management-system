package com.bank.bank_management.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.bank_management.entity.Bank;

public interface BankRepository extends JpaRepository<Bank, Long> {
    boolean existsByIfsc(String ifsc);
    boolean existsByContact(String contact);
    Optional<Bank> findByIfsc(String ifsc);
    Optional<Bank> findByContact(String contact);
    List<Bank> findByAddressCityIgnoreCase(String city);
    List<Bank> findByAddressStreetContainingIgnoreCase(String street);
}