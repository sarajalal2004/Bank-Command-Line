package com.ga.bank;

import java.util.Optional;

public class Customer extends User{

    @Override
    public String toString() {
        return "I am a customer, my account: " + Optional.ofNullable(this.getCheckingAccount()).orElse("Don't have checking account");
    }
}
