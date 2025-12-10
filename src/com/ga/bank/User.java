package com.ga.bank;
import java.security.MessageDigest;
import java.util.Base64;
import java.io.*;
import java.util.*;

public abstract class User implements IUser{
    private String username;
    private String password;
    private int age;
    private String address;
    private String role = "Customer";
    private int loginTrial = 3;
    private boolean logged = false;
    private String checkingAccount ="";
    private String savingAccount ="";

    private static int maxLoginAttempts = 3;
    private static int waitTimeInMin = 1;
    File Usersfile = new File("assets/Users.txt");


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCheckingAccount() {
        return checkingAccount;
    }

    public void setCheckingAccount(String checkingAccount) {
        this.checkingAccount = checkingAccount;
    }

    public String getSavingAccount() {
        return savingAccount;
    }

    public void setSavingAccount(String savingAccount) {
        this.savingAccount = savingAccount;
    }

    public String hashPassword(String password) throws Exception{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashed = md.digest(password.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(hashed);
    }

    public boolean userFetch(String username){
        try (BufferedReader reader = new BufferedReader(new FileReader(Usersfile))) {
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null){
                //Read the info from the file, -1 to keep empty places
                lineArray= Arrays.stream(line.split(",", -1)).toList();

                if(lineArray.get(0).equals(username)) {
                    return true;
                }
            }


        }catch (IOException e) {
            System.err.println("Error reading to file: " + e.getMessage());
        }
        return false;
    }

    public int userFetch(String username, String password) throws Exception{
        int found = -1;
        try (BufferedReader reader = new BufferedReader(new FileReader(Usersfile))) {
            List<String> allLines = new ArrayList<>();
            String line;
            List<String> lineArray;

                while ((line = reader.readLine()) != null){
                    //Read the info from the file, -1 to keep empty places
                    lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                    if(lineArray.get(0).equals(username)) {
                        // Ask for user info
                        this.username = lineArray.get(0);
                        this.age = Integer.parseInt(lineArray.get(1));
                        this.password = lineArray.get(2);
                        this.address = lineArray.get(3);
                        this.role = lineArray.get(4);
                        this.loginTrial = Integer.parseInt(lineArray.get(5)) + 1;
                        this.checkingAccount = lineArray.get(6);
                        this.savingAccount = lineArray.get(7);
                        if(password.equals(lineArray.get(2))){
                            found = 0;
                        }else{
                            lineArray.set(5, String.valueOf(this.loginTrial));
                            found = 1;
                            if(this.loginTrial > 3) {
                                System.out.println("...........You have to wait 1 min...........");
                                try {
                                    Thread.sleep(60000); // Wait 3 seconds
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();   // restore flag
                                    System.out.println("Sleep was interrupted");
                                }
                                this.loginTrial = 0;
                                lineArray.set(5, String.valueOf(this.loginTrial));
                                found = 2;
                            }
                        }
                    }
                    allLines.add(String.join(",", lineArray));
                }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(Usersfile))){
                for (String l : allLines) {
                    writer.write(l);
                    writer.newLine();
                }
            }

        }catch (IOException e) {
            System.err.println("Error reading to file: " + e.getMessage());
        }

        return found;
    }

    @Override
    public boolean signup() throws Exception{
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Usersfile, true))) {
            Scanner read = new Scanner(System.in);

            // Ask for user info
            System.out.println("\t\t\tSignup\t\t\t");
            System.out.println("Please fill information below: ");
            System.out.print("Username: ");
            this.username = read.next();
            System.out.print("Age: ");
            this.age = read.nextInt();
            System.out.print("Password: ");
            this.password =  hashPassword(read.next());
            System.out.print("Address: ");
            this.address = read.next();

            //Add the info to the file
            if(!userFetch(this.username)){
                writer.write(username + "," + age + "," + password + "," + address + "," + role + ",0,,");
                writer.newLine();
                this.logged = true;
            }else {
                System.out.println("This username is already used please try another one");
            }
        }catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        return this.logged;
    }

    @Override
    public boolean login() throws Exception{
        Scanner read = new Scanner(System.in);

        // Ask for user info
        System.out.println("\t\t\tlogin\t\t\t");
        System.out.println("Please fill information below: ");
        System.out.print("Username: ");
        this.username = read.next();
        System.out.print("Password: ");
        this.password = read.next();

        int flag = userFetch(this.username, hashPassword(this.password));
        if(flag == 0){
            System.out.println("\n||||||||||||| logged successfully, Hello " + this.getUsername()+ " 👋🏻 |||||||||||");
            this.logged = true;
        } else if(flag == -1) {
            System.out.println("* No user with this username please signup first *");
        } else if(flag == 1){
            System.out.println("* Wrong username or password *");
        }else{
            System.out.println("* You could try again now *");
        }

        return this.logged;
    }

    public void addAccount(){
        if(logged){
            try (BufferedReader reader = new BufferedReader(new FileReader(Usersfile))) {
                List<String> allLines = new ArrayList<>();
                String line;
                List<String> lineArray;
                Scanner read = new Scanner(System.in);
                Account account = new Account();


                while ((line = reader.readLine()) != null){
                    //Read the info from the file, -1 to keep empty places
                    lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));
                    if(lineArray.get(0).equals(this.username)) {
                        System.out.println("enter");
                        // Ask for user info
                        System.out.print("what type of account do you want to create:\n1:Checking\n2:Saving\nEnter the chosen number: ");
                        int type = read.nextInt();
                        System.out.print("what type of card do you want:\n1:Mastercard\n2:Mastercard Titanium\n3:Mastercard Platinum\nEnter the chosen number: ");
                        int cardType = read.nextInt();
                        if(type == 1) {
                            if(this.checkingAccount.isEmpty()){
                            this.checkingAccount = account.getIBAN();
                            account.setAccountType("checking");
                            lineArray.set(6, account.getIBAN());
                            } else {
                                System.out.println("Already have checking account");
                            }
                        }else if (type == 2) {
                            if(this.savingAccount.isEmpty()){
                            this.savingAccount = account.getIBAN();
                            account.setAccountType("saving");
                            lineArray.set(7, account.getIBAN());
                            } else {
                                System.out.println("Already have saving account");
                            }
                        }else {
                            System.out.println("Uncorrect request");
                        }

                        if(cardType == 1)
                            account.setCardType("Mastercard");
                        else if(cardType == 2)
                            account.setCardType("Mastercard Titanium");
                        else if(cardType == 3)
                            account.setCardType("Mastercard Platinum");
                        else
                            System.out.println("Uncorrect card type");

                        account.createAccount();
                    }
                    allLines.add(String.join(",", lineArray));
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(Usersfile))){
                    for (String l : allLines) {
                        writer.write(l);
                        writer.newLine();
                    }
                }

                File file = new File("assets/transactionHistory/" + account.getIBAN() + ".txt");
                file.createNewFile();

            }catch (IOException e) {
                System.err.println("Error reading to file: " + e.getMessage());
            }
        }
    }

    abstract public String toString();

}
