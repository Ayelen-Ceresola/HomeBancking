package com.mindhub.homebanking.dtos;

public class NewTransactionDTO {

    private Double amount;
    private String numberSourceAccount;
    private String numberDestinationAccount;
    private String description;

    public NewTransactionDTO() {}

    public Double getAmount() {
        return amount;
    }

    public String getNumberSourceAccount() {
        return numberSourceAccount;
    }

    public String getNumberDestinationAccount() {
        return numberDestinationAccount;
    }

    public String getDescription() {
        return description;
    }
}
