package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

        private Long id;
        private String number;
        private LocalDate creationDate;
        private Double balance;
        private AccountType type;

        private Set<TransactionDTO> transactions;

        public AccountDTO(){}

        public AccountDTO(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.creationDate= account.getCreationDate();
            this.type= account.getType();
            this.transactions= account.getTransactions()
                    .stream()
                    .map(transaction -> new TransactionDTO(transaction))
                    .collect(Collectors.toSet());
            ;

        }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public AccountType getType() {return type; }
}
