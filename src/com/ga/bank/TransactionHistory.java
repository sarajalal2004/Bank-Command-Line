package com.ga.bank;

import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class TransactionHistory {

    public class TransactionRecord{
        private String type;
        private Date datetime;
        private double instanceBalance;
    }

//    private static final Map<String, ArrayList<Object>> timeFilters = Map.of(
//            "today", List.of(LocalDate.now()),
//            "yesterday", LocalDate.now().minusDays(1),
//            "last week",
//
//    );

    private Account account;
    private ArrayList<TransactionRecord> transactionList;

    public void getDetail(){

    }

    public void getDetail(Date datetime){

    }

}
