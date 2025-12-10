package com.ga.bank;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Banker extends User{
    private final String role = "Banker";
    File accountsfile = new File("assets/Accounts.txt");
    File Usersfile = new File("assets/Users.txt");


    public void grantBankerRole(String username){
        try (BufferedReader reader = new BufferedReader(new FileReader(Usersfile))) {
            List<String> allLines = new ArrayList<>();
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null){
                //Read the info from the file, -1 to keep empty places
                lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                if(lineArray.get(0).equals(username)) {
                    lineArray.set(4, String.valueOf("Banker"));
                }

                allLines.add(String.join(",", lineArray));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Usersfile))){
                for (String l : allLines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading to file: " + e.getMessage());
        }
    };

    public void accountReactivation(String IBAN){
        try (BufferedReader reader = new BufferedReader(new FileReader(accountsfile))) {
            List<String> allLines = new ArrayList<>();
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null){
                //Read the info from the file, -1 to keep empty places
                lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                if(lineArray.get(0).equals(IBAN)) {
                    Account account = new Account();
                    account.fetchAccount(IBAN);
                    if(!account.isActive()){
                        lineArray.set(4, String.valueOf(true));
                        lineArray.set(5, String.valueOf(0));
                        lineArray.set(1, String.valueOf(0));
                    }else {
                        System.out.println("This account is already active");
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
    }

    @Override
    public String toString() {
        return "I am a banker";
    }
}
