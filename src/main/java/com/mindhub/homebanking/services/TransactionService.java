package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {
    void save(Transaction transaction);


    void saveAll(List<Transaction> transactionList);


}
