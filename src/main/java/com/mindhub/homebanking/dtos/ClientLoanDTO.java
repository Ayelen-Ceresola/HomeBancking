package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.Collections;
import java.util.List;

public class ClientLoanDTO{

    private long id;
    private long loanId;
    private String loanName;
    private double amount;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan){

        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayment();


    }

    public long getId() {
        return id;
    }

    public long getLoanId() {
        return loanId;
    }


    public String getLoanName() {
        return loanName;
    }


    public double getAmount() {
        return amount;
    }


    public Integer getPayments() {
        return payments;
    }

}
