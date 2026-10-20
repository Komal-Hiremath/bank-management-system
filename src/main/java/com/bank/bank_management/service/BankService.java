package com.bank.bank_management.service;

import java.util.List;
import org.springframework.data.domain.Page;
import com.bank.bank_management.dto.BankRequest;
import com.bank.bank_management.entity.Bank;

public interface BankService {
    Bank createBank(BankRequest request);
    List<Bank> getAllBanks();
    Bank getBankById(Long id);
    Bank getBankByIfsc(String ifsc);
    Bank getBankByContact(String contact);
    List<Bank> getBanksByCity(String city);
    List<Bank> getBanksByAddress(String street);
    Page<Bank> getBanksWithPagination(int page, int size);
    List<Bank> getBanksWithSorting(String field);
    Bank updateBank(Long id, BankRequest request);
    void deleteBank(Long id);
}