package com.ga.bank;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Account {
    public static int count = 15;

    private String IBAN = "IBN"+count;
    private double Balance = 0;
    private String AccountType;
    private String cardType;
//    private String cardNumber;
//    private Date expireDate;
//    private String CVV;
    private boolean Active = true;
    private int overdraftTimes = 0;
    File accountsfile = new File("assets/Accounts.txt");


    public String getAccountType() {
        return AccountType;
    }

    public void setAccountType(String accountType) {
        AccountType = accountType;
    }

    private static final String[] AccountTypes = {"checking", "saving"};
    private static final String[] limitNaming = {"0: Withdraw Limit Per Day", "1: Transfer Limit Per Day",
            "2: Transfer Limit Per Day (Own Account)", "3: Deposit Limit Per Day"};
    private static final Map<String, List<Double>> cardTypes = Map.of(
            "Mastercard", List.of(20000.00, 40000.00, 80000.00, 100_000.00),
            "Mastercard Titanium", List.of(10000.00, 20000.00, 40000.00, 100_000.00),
            "Mastercard Platinum", List.of(5000.00, 10000.00, 20000.00, 100_000.00)
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

    public void Active(boolean active) {
        Active = active;
    }

    public void createAccount() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsfile, true))) {
            writer.write("IBN" + (++count) + "," + Balance + "," + AccountType + "," + cardType + "," + Active + "," + overdraftTimes);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchAccount(String IBAN) {
        try (BufferedReader reader = new BufferedReader(new FileReader(accountsfile))) {
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null){
                //Read the info from the file, -1 to keep empty places
                lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                if(lineArray.get(0).equals(IBAN)) {
                    // Ask for user info
                    this.IBAN = lineArray.get(0);
                    this.Balance = Double.parseDouble(lineArray.get(1));
                    this.AccountType = lineArray.get(2);
                    this.cardType = lineArray.get(3);
                    this.Active = Boolean.parseBoolean(lineArray.get(4));
                    this.overdraftTimes = Integer.parseInt(lineArray.get(5));
                }
            }
        }catch (IOException e) {
            System.err.println("Error reading to file: " + e.getMessage());
        }
    }

    public boolean deposit(double amount, double dayAmount){
        boolean value = false;
        if(this.Active){
            try (BufferedReader reader = new BufferedReader(new FileReader(accountsfile))) {
                List<String> allLines = new ArrayList<>();
                String line;
                List<String> lineArray;
                double limit = cardTypes.get(cardType).get(3);

                while ((line = reader.readLine()) != null){
                    //Read the info from the file, -1 to keep empty places
                    lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                    if(lineArray.get(0).equals(IBAN)) {
                        if (amount + dayAmount <= limit) {
                            Balance += amount;
                            lineArray.set(1, String.valueOf(this.Balance));
                            new TransactionHistory.TransactionRecord(IBAN, "deposit", LocalDateTime.now(), amount, Balance);
                            value = true;
                        } else {
                            System.out.println("This exceed the limit of deposing per day");
                        }
                    }

                    allLines.add(String.join(",", lineArray));
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsfile))){
                    for (String l : allLines) {
                        writer.write(l);
                        writer.newLine();
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading to file: " + e.getMessage());
            }
        }else {
            System.out.println("account in deactivate");
        }
        return value;
    }

    public void withdraw(double amount, double dayAmount){
        if (this.Active) {
            try (BufferedReader reader = new BufferedReader(new FileReader(accountsfile))) {
                List<String> allLines = new ArrayList<>();
                String line;
                List<String> lineArray;
                double limit = cardTypes.get(cardType).get(0);
                System.out.println(limit);

                while ((line = reader.readLine()) != null){
                    //Read the info from the file, -1 to keep empty places
                    lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                    if(lineArray.get(0).equals(IBAN)) {
                        if(amount + dayAmount <= limit){
                            if(Balance - amount >= 0){
                                Balance -= amount;
                                lineArray.set(1, String.valueOf(this.Balance));
                                new TransactionHistory.TransactionRecord(this.IBAN, "withdraw", LocalDateTime.now(), amount, this.Balance);
                            } else {
                                if(Balance>0 && amount - Balance > maxOverdraftAmount){
                                    System.out.println("You couldn't overdraft more than 100");
                                }else if(Balance < 0 && amount > maxOverdraftAmount){
                                    System.out.println("You couldn't overdraft more than 100");
                                }else{
                                    Balance -= (amount + 35);
                                    lineArray.set(1, String.valueOf(this.Balance));
                                    this.overdraftTimes++;
                                    lineArray.set(5, String.valueOf(this.overdraftTimes));
                                    new TransactionHistory.TransactionRecord(this.IBAN, "withdraw", LocalDateTime.now(), amount, this.Balance);
                                    if (overdraftTimes >= maxOverdraftCount) {
                                        System.out.println("Your account is deactivated due to overdrafting 2 times");
                                        this.Active = false;
                                        lineArray.set(4, String.valueOf(this.Active));
                                    }
                                }
                            }
                        }else {
                            System.out.println("This exceed the limit of deposing per day");
                        }
                    }

                    allLines.add(String.join(",", lineArray));
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsfile))){
                    for (String l : allLines) {
                        writer.write(l);
                        writer.newLine();
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading to file: " + e.getMessage());
            }
        } else {
            System.out.println("Your account is already deactivated");
        }
    }


    private void depositTransfer(double amount,String transferType){
        try (BufferedReader reader = new BufferedReader(new FileReader(accountsfile))) {
            List<String> allLines = new ArrayList<>();
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null){
                //Read the info from the file, -1 to keep empty places
                lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                if(lineArray.get(0).equals(IBAN)) {
                        Balance += amount;
                        lineArray.set(1, String.valueOf(this.Balance));
                        new TransactionHistory.TransactionRecord(this.IBAN, transferType, LocalDateTime.now(), amount, this.Balance);
                }
                allLines.add(String.join(",", lineArray));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsfile))){
                for (String l : allLines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading to file: " + e.getMessage());
        }
    }

    private boolean withdrawTransfer(double amount, String transferType){
        boolean applicable = false;
        try (BufferedReader reader = new BufferedReader(new FileReader(accountsfile))) {
            List<String> allLines = new ArrayList<>();
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null){
                //Read the info from the file, -1 to keep empty places
                lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                if(lineArray.get(0).equals(IBAN)) {
                    if(Balance - amount >= 0){
                        Balance -= amount;
                        lineArray.set(1, String.valueOf(this.Balance));
                        new TransactionHistory.TransactionRecord(this.IBAN, transferType, LocalDateTime.now(), amount, this.Balance);
                        applicable = true;
                    } else {
                        if(Balance>0 && amount - Balance > maxOverdraftAmount){
                            System.out.println("You couldn't overdraft more than 100");
                        }else if(Balance < 0 && amount > maxOverdraftAmount){
                            System.out.println("You couldn't overdraft more than 100");
                        }else{
                            Balance -= (amount + 35);
                            lineArray.set(1, String.valueOf(this.Balance));
                            new TransactionHistory.TransactionRecord(this.IBAN, transferType, LocalDateTime.now(), amount, this.Balance);
                            this.overdraftTimes++;
                            lineArray.set(5, String.valueOf(this.overdraftTimes));
                            applicable = true;
                            if (overdraftTimes >= maxOverdraftCount) {
                                System.out.println("Your account is deactivated due to overdrafting 2 times");
                                this.Active = false;
                                lineArray.set(4, String.valueOf(this.Active));
                            }
                        }
                    }
                }

                allLines.add(String.join(",", lineArray));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(accountsfile))){
                for (String l : allLines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading to file: " + e.getMessage());
        }
        return applicable;
    }

    public void internalTransfer(double amount, String toAccountIBAN, double thisDayAmount){
        Account toAccount = new Account();
        toAccount.fetchAccount(toAccountIBAN);
        if(this.Active && toAccount.Active){
                if(thisDayAmount + amount <= cardTypes.get(this.cardType).get(2)){
                 if(this.withdrawTransfer(amount, "Internal Transfer"))
                    toAccount.depositTransfer(amount, "Receive Internal Transfer");
                }else{
                    System.out.println("This exceed your cards per day limits");
                }
        } else{
            System.out.println("One of your accounts is already deactivated");
        }
    }

    public void externalTransfer(double amount, String toAccountIBAN, double thisDayAmount){
        Account toAccount = new Account();
        toAccount.fetchAccount(toAccountIBAN);
        if(this.Active && toAccount.Active){
            if(thisDayAmount + amount <= cardTypes.get(this.cardType).get(1)){
                if(this.withdrawTransfer(amount, "External Transfer"))
                    toAccount.depositTransfer(amount, "Receive External Transfer");
            }else{
                System.out.println("This exceed cards per day limits");
            }
        } else{
            System.out.println("One of the accounts is already deactivated");
        }
    }
}
