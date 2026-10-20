package com.bank.bank_management.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class BankRequest {

    @NotBlank(message = "Bank name is required")
    private String bankName;

    @NotBlank(message = "IFSC code is required")
    @Pattern(regexp = "^[A-Za-z]{4}0[A-Za-z0-9]{6}$",
             message = "Invalid IFSC code")
    private String ifsc;

    @NotBlank(message = "Branch name is required")
    private String branchName;

    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "^[0-9]{10}$",
             message = "Contact number must contain exactly 10 digits")
    private String contact;

    @NotNull(message = "Address is required")
    @Valid
    private AddressRequest address;

    public BankRequest() {
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }
}
