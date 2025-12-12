package com.ga.bank;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionHistory {
    private String IBAN;
    private String transactionFileName;
    private ArrayList<TransactionRecord> transactionList = new ArrayList<>();;

    public static class TransactionRecord{
        private String type;
        private LocalDateTime date;
        private double amount;
        private double instanceBalance;

        // for data only
        public TransactionRecord(String type, LocalDateTime date, double amount, double instanceBalance){
            this.type = type;
            this.date = date;
            this.amount = amount;
            this.instanceBalance = instanceBalance;
        }

        // For new records
        public TransactionRecord(String IBAN, String type, LocalDateTime date, double amount, double instanceBalance){
            File transctionfile = new File("assets/transactionHistory/" + IBAN + ".txt");

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(transctionfile, true))) {
                writer.write(type + "," + date + "," + amount + "," + instanceBalance);
                writer.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public TransactionHistory(String IBAN) {
        this.IBAN = IBAN;
        transactionFileName = "assets/transactionHistory/" + IBAN + ".txt";
        System.out.println(transactionFileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(transactionFileName))) {
            String line;
            List<String> lineArray;

            while ((line = reader.readLine()) != null) {
                //Read the info from the file, -1 to keep empty places
                lineArray = new ArrayList<>(Arrays.asList(line.split(",", -1)));

                transactionList.add(new TransactionRecord(lineArray.get(0), LocalDateTime.parse(lineArray.get(1)), Double.parseDouble(lineArray.get(2)), Double.parseDouble(lineArray.get(3))));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double AmountThisDay(String type){
        return transactionList.stream().filter(transactionRecord -> transactionRecord.type.equals(type) && transactionRecord.date.toLocalDate().equals(LocalDate.now())).map(transactionRecord -> transactionRecord.amount).reduce(0.0, (total, amount) -> total + amount);
    }

    private static final Map<String, List<LocalDateTime>> timeFilters = Map.of(
            "today", List.of(LocalDate.now().atStartOfDay(),LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1)),
            "yesterday", List.of(LocalDate.now().minusDays(1).atStartOfDay(),LocalDate.now().atStartOfDay().minusNanos(1)),
            "last week", List.of(LocalDate.now().minusWeeks(1).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1)),
            "last month", List.of(LocalDate.now().minusMonths(1).atStartOfDay(), LocalDate.now().plusDays(1).atStartOfDay().minusNanos(1))
    );

    public void printDetail(){
        System.out.println("Transaction Type\t\t\t*\tDate and time\t\t*\tBalance");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        for(TransactionRecord record : transactionList){
            if(record.type.equals("deposit"))
                System.out.println(record.type + "\t\t\t\t\t\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
            else if(record.type.equals("withdraw"))
                System.out.println(record.type + "\t\t\t\t\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
            else if(record.type.equals("Internal Transfer") || record.type.equals("External Transfer"))
                System.out.println(record.type + "\t\t\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
            else
                System.out.println(record.type + "\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
        }
    }

    public void printDetail(LocalDateTime fromDateTime, LocalDateTime toDateTime){
        System.out.println("Transaction Type\t\t\t*\tDate and time\t\t*\tBalance");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        for(TransactionRecord record : transactionList.stream().filter(transactionRecord -> transactionRecord.date.isAfter(fromDateTime.minusNanos(1)) && transactionRecord.date.isBefore(toDateTime.plusNanos(1))).toList()){
            if(record.type.equals("deposit"))
                System.out.println(record.type + "\t\t\t\t\t\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
            else if(record.type.equals("withdraw"))
                System.out.println(record.type + "\t\t\t\t\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
            else if(record.type.equals("Internal Transfer") || record.type.equals("External Transfer"))
                System.out.println(record.type + "\t\t\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
            else
                System.out.println(record.type + "\t*\t" + record.date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + "\t*\t" + record.instanceBalance);
        }
    }

    public void filterWithWords(){
        Scanner read = new Scanner(System.in);
        System.out.print("\n1:today\n2:yesterday\n3:last week\n4:last month\nEnter you choice: ");
        int value = read.nextInt();
        if(value == 1)
            printDetail(timeFilters.get("today").get(0), timeFilters.get("today").get(1));
        else if(value == 2)
            printDetail(timeFilters.get("yesterday").get(0), timeFilters.get("yesterday").get(1));
        else if(value == 3)
            printDetail(timeFilters.get("last week").get(0), timeFilters.get("last week").get(1));
        else if(value == 4)
            printDetail(timeFilters.get("last month").get(0), timeFilters.get("last month").get(1));
        else
            System.out.println("Wrong Input");
    }
}
