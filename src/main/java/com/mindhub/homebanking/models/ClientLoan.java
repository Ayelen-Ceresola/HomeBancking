package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class ClientLoan {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native" )

    private long id;
    private Double amount;
    private int payment;
    private int remainingPayments;
    private Double remainingBalance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan_id")
    private Loan loan;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    public ClientLoan(){}


    public ClientLoan(Double amount, int payment, int remainingPayments, Double remainingBalance) {
        this.amount = amount;
        this.payment = payment;
        this.remainingPayments= remainingPayments;
        this.remainingBalance = remainingBalance;
    }

    public long getId() {
        return id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getRemainingPayments() {return remainingPayments;}

    public void setRemainingPayments(int remainingPayments) {this.remainingPayments = remainingPayments;}

    public Double getRemainingBalance() {return remainingBalance;}

    public void setRemainingBalance(Double remainingBalance) {this.remainingBalance = remainingBalance;}
}


