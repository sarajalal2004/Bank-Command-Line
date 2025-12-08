package com.ga.bank;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Account {
    private static int count = 0;

    private String IBAN = "IBN"+count;
    private double Balance = 0;
    private String AccountType;
    private String cardType;
//    private String cardNumber;
//    private Date expireDate;
//    private String CVV;
    private boolean Active = true;

    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    private static final String[] AccountTypes = {"checking", "saving"};
    private static final String[] limitNaming = {"Withdraw Limit Per Day", "Transfer Limit Per Day",
            "Transfer Limit Per Day (Own Account)", "Deposit Limit Per Day",
            "Deposit Limit Per Day (Own Account)"};
    private static final Map<String, List<Double>> cardTypes = Map.of(
            "Mastercard", List.of(20000.00, 40000.00, 80000.00, 100_000.00, 200_000.00),
            "Mastercard Titanium", List.of(10000.00, 20000.00, 40000.00, 100_000.00, 200_000.00),
            "Mastercard Platinum", List.of(5000.00, 10000.00, 20000.00, 100_000.00, 200_000.00)
    );
    private static final double maxOverdraftAmount = 100;
    private static final double maxOverdraftCount = 2;
    private static final double overdraftCharge = 35;

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

//    public String getCardNumber() {
//        return cardNumber;
//    }
//
//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public Date getExpireDate() {
//        return expireDate;
//    }
//
//    public void setExpireDate(Date expireDate) {
//        this.expireDate = expireDate;
//    }
//
//    public String getCVV() {
//        return CVV;
//    }
//
//    public void setCVV(String CVV) {
//        this.CVV = CVV;
//    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

    public void createAccount(){

    }

    public void deposit(double amount){

    }

    public void withdraw(double amount){

    }

    public void internalTransfer(double amount,Account fromAccount, Account toAccount){

    }

    public void externalTransfer(double amount, Account fromAccount, Account toAccount){

    }
}
