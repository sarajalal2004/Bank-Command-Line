package com.ga.bank;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.DisplayName;
import org.junit.runners.MethodSorters;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountTest {

    // DemoTest parameters
    // Username: TestUser
    // Password: test123
    // Role: Customer
    // Account: IBN2017     Saving    Mastercard Titanium
    // Username: TestUser2
    // Password: test123
    // Role: Customer
    // Account: IBN2017     Checking    Mastercard

    String IBAN= "IBN2017";
    Account account = new Account();
    Banker banker = new Banker();

    @org.junit.Test
    @DisplayName("When overdraft fees is applied.")
    public final void overdraftFeesApplied() throws Exception{
        double overdraftAmount = 30;
        account.fetchAccount(IBAN);
        double preBalance =  account.getBalance();
        account.withdraw(overdraftAmount, 0); // assume this is the first withdraw in this day
        double postBalance =  account.getBalance();

        //reset the changes
        account.withdraw(overdraftAmount, 0);
        banker.accountReactivation(IBAN);

        Assert.assertEquals(overdraftAmount + 35, preBalance - postBalance, 0.001);
    }

    String IBAN2= "IBN2018";
    Account account2 = new Account();

    @org.junit.Test
    @DisplayName("Don't allow overdraft more than 100.")
    public final void preventOver100Overdraft() {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream original = System.out;
        System.setOut(new PrintStream(out));

        try {
            double overdraftAmount2 = 130;
            account2.fetchAccount(IBAN2);
            double preBalance = account2.getBalance();
            account2.withdraw(overdraftAmount2, 0); // assume this is the first withdraw in this day

            Assert.assertEquals(
                    "You couldn't overdraft more than 100"+ System.lineSeparator(), // because it appends line automatically
                    out.toString()
            );

        } finally {
            System.setOut(original);
        }
    }
}