package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime date;
    private Double currentBalance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")

    private Account account;
    private boolean isActive;


    public Transaction (){}

    public Transaction(TransactionType type, Double amount,String description,LocalDateTime date,Double currentBalance,boolean isActive){
        this.type = type;
        this.amount= amount;
        this.date = date;
        this.description= description;
        this.currentBalance= currentBalance;
        this.isActive= isActive;

    }
    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getCurrentBalance() {return currentBalance;}

    public void setCurrentBalance(Double currentBalance) {this.currentBalance = currentBalance;}

    public boolean isActive() {return isActive;}

    public void setActive(boolean active) {this.isActive = active;}

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }


}
