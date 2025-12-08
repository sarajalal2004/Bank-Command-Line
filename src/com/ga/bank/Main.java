package com.ga.bank;

public class Main {
    public static void main(String[] args) throws Exception {
        Customer user = new Customer();
        user.signup();
        user.login();
        user.createAccount("saving");
    }
}
