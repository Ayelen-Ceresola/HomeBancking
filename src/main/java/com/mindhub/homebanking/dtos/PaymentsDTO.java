package com.mindhub.homebanking.dtos;

public class PaymentsDTO {

    private String cardNumber;
    private int cvv;
    private Double amount;
    private String description;

    public PaymentsDTO(){}

    public String getCardNumber() {return cardNumber;}

    public int getCvv() {return cvv;}

    public Double getAmount() {return amount;}

    public String getDescription() {return description;}

}
