package com.ga.bank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner read = new Scanner(System.in);
        Customer user = new Customer();
        boolean logged = false;
        boolean exit = false;
        while (!exit) {
            System.out.print("|||||||||||||||||||| Hello User 😁|||||||||||||||||||||||\n1: Signup\n2: login\n3: exit\nEnter you choice: ");
            int value = read.nextInt();
            System.out.println();
            if (value == 1) {
                logged = user.signup();
                user.addAccount();
            } else if (value == 2)
                logged = user.login();
            else if (value == 3)
                exit = true;
            else
                System.out.println("Wrong input, please choose from numbers above🥺");
            while (logged) {
                Account account = new Account();
                String IBAN;
                if (user.getRole().equals("Customer")) {

                    List<String> allAccount = new ArrayList<>();

                    if (!user.getCheckingAccount().isEmpty()) {
                        allAccount.add(user.getCheckingAccount());
                    }

                    if (!user.getSavingAccount().isEmpty()) {
                        allAccount.add(user.getSavingAccount());
                    }

                    System.out.println("||||||||||||||||| Your Accounts |||||||||||||||");
                    if (allAccount.size() == 1) {
                        System.out.println("You have 1 account:");
                        System.out.println("1: " + allAccount.get(0));
                        account.fetchAccount(allAccount.get(0)); // directly fetch
                    } else if (allAccount.size() == 2) {
                        System.out.println("You have 2 accounts:");
                        System.out.println("1: Checking " + allAccount.get(0));
                        System.out.println("2: Saving   " + allAccount.get(1));
                        System.out.print("Enter your choice: ");
                        int choice = read.nextInt();

                        if (choice == 1)
                            account.fetchAccount(allAccount.get(0));
                        else if (choice == 2)
                            account.fetchAccount(allAccount.get(1));
                        else
                            System.out.println("Invalid choice!");
                    } else {
                        System.out.println("You have no accounts!");
                    }
                    System.out.println("||||||||||||||||||||||||||||||| Account transactions |||||||||||||||||||||||||||||||||||");
                    System.out.print("\n1: deposit\n2: withdraw\n3: Internal Transfer\n4: External Transfer\n5: Transaction Details\n6: Add account\n7: logout\nEnter you choice: ");
                    switch (read.nextInt()) {
                        case 1:
                            System.out.print("\t\tDeposit\nEnter amount: ");
                            account.deposit(read.nextInt(), new TransactionHistory(account.getIBAN()).AmountThisDay("deposit"));
                            break;
                        case 2:
                            System.out.print("\t\twithdraw\nEnter amount: ");
                            account.withdraw(read.nextInt(), new TransactionHistory(account.getIBAN()).AmountThisDay("withdraw"));
                            break;
                        case 3:
                            System.out.print("\t\tInternal Transfer\nEnter amount: ");
                            if (allAccount.size() > 1) {
                                if (account.getAccountType().equals("checking"))
                                    account.internalTransfer(read.nextInt(), user.getSavingAccount(), new TransactionHistory(account.getIBAN()).AmountThisDay("Internal Transfer"));
                                else
                                    account.internalTransfer(read.nextInt(), user.getCheckingAccount(), new TransactionHistory(account.getIBAN()).AmountThisDay("Internal Transfer"));
                            } else {
                                System.out.println("You have only one account");
                            }
                            break;
                        case 4:
                            System.out.print("\t\tExternal Transfer\nEnter amount and the IBAN of other account: ");
                            account.externalTransfer(read.nextInt(), read.next(), new TransactionHistory(account.getIBAN()).AmountThisDay("External Transfer"));
                            break;
                        case 5:
                            System.out.print("\t\tTransaction Details\n1: all transactions\n2: filtered transactions\nEnter your choice");
                            int filtration = read.nextInt();
                            if (filtration == 1)
                                new TransactionHistory(account.getIBAN()).printDetail();
                            else if (filtration == 2) {
                                new TransactionHistory(account.getIBAN()).filterWithWords();
                            }
                            break;
                        case 6:
                            System.out.print("\t\tAdd Account");
                            user.addAccount();
                            break;
                        case 7:
                            logged = false;
                            System.out.println("\t\tlogged out Successfully");
                            break;
                        default:
                            System.out.println("No choice with this number");
                    }
                } else if (user.getRole().equals("Banker")) {
                    Banker banker = new Banker();
                    List<String> allAccount = new ArrayList<>();

                    if (!user.getCheckingAccount().isEmpty()) {
                        allAccount.add(user.getCheckingAccount());
                    }

                    if (!user.getSavingAccount().isEmpty()) {
                        allAccount.add(user.getSavingAccount());
                    }

                    System.out.println("||||||||||||||||| Your Accounts |||||||||||||||");
                    if (allAccount.size() == 1) {
                        System.out.println("You have 1 account:");
                        System.out.println("1: " + allAccount.get(0));
                        account.fetchAccount(allAccount.get(0)); // directly fetch
                    } else if (allAccount.size() == 2) {
                        System.out.println("You have 2 accounts:");
                        System.out.println("1: Checking " + allAccount.get(0));
                        System.out.println("2: Saving   " + allAccount.get(1));
                        System.out.print("Enter your choice: ");
                        int choice = read.nextInt();

                        if (choice == 1)
                            account.fetchAccount(allAccount.get(0));
                        else if (choice == 2)
                            account.fetchAccount(allAccount.get(1));
                        else
                            System.out.println("Invalid choice!");
                    } else {
                        System.out.println("You have no accounts!");
                    }
                    System.out.println("||||||||||||||||||||||||||||||| Account transactions |||||||||||||||||||||||||||||||||||");
                    System.out.print("\n1: deposit\n2: withdraw\n3: Internal Transfer\n4: External Transfer\n5: Transaction Details\n6: Add account\n7: logout\n8: grant bank roles\n9: Activate accounts\nEnter you choice: ");
                    switch (read.nextInt()) {
                        case 1:
                            System.out.print("\t\tDeposit\nEnter amount: ");
                            account.deposit(read.nextInt(), new TransactionHistory(account.getIBAN()).AmountThisDay("deposit"));
                            break;
                        case 2:
                            System.out.print("\t\twithdraw\nEnter amount: ");
                            account.withdraw(read.nextInt(), new TransactionHistory(account.getIBAN()).AmountThisDay("withdraw"));
                            break;
                        case 3:
                            System.out.print("\t\tInternal Transfer\nEnter amount: ");
                            if (allAccount.size() > 1) {
                                if (account.getAccountType().equals("checking"))
                                    account.internalTransfer(read.nextInt(), user.getSavingAccount(), new TransactionHistory(account.getIBAN()).AmountThisDay("Internal Transfer"));
                                else
                                    account.internalTransfer(read.nextInt(), user.getCheckingAccount(), new TransactionHistory(account.getIBAN()).AmountThisDay("Internal Transfer"));
                            } else {
                                System.out.println("You have only one account");
                            }
                            break;
                        case 4:
                            System.out.print("\t\tExternal Transfer\nEnter amount and the IBAN of other account: ");
                            account.externalTransfer(read.nextInt(), read.next(), new TransactionHistory(account.getIBAN()).AmountThisDay("External Transfer"));
                            break;
                        case 5:
                            System.out.print("\t\tTransaction Details\n1: all transactions\n2: filtered transactions\nEnter your choice");
                            int filtration = read.nextInt();
                            if (filtration == 1)
                                new TransactionHistory(account.getIBAN()).printDetail();
                            else if (filtration == 2) {
                                new TransactionHistory(account.getIBAN()).filterWithWords();
                            }
                            break;
                        case 6:
                            System.out.print("\t\tAdd Account");
                            user.addAccount();
                            break;
                        case 7:
                            logged = false;
                            System.out.print("\t\tlogged out Successfully");
                            break;
                        case 8:
                            System.out.print("\t\tGrant Bank role\nEnter username: ");
                            banker.grantBankerRole(read.next());
                            break;
                        case 9:
                            System.out.print("\t\tReactivate account\nEnter IBAN: ");
                            banker.accountReactivation(read.next());
                            break;
                        default:
                            System.out.println("No choice with this number");
                    }
                }
            }
        }
    }
}
