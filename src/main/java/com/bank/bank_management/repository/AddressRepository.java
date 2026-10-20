package com.bank.bank_management.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bank.bank_management.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByBankBankId(Long bankId);
    List<Address> findByCityIgnoreCase(String city);
    List<Address> findByCityIgnoreCaseAndStreetContainingIgnoreCase(String city, String street);
}