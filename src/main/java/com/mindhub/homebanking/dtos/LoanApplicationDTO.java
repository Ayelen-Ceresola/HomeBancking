package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    private Long loanTypeId;
    private Double amount;
    private Integer payments;
    private String destinationAccount;
    private Double percentage;

    public LoanApplicationDTO() {}

    public Long getLoanTypeId() {
        return loanTypeId;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getPayments() {
        return payments;
    }

    public String getDestinationAccount() {
        return destinationAccount;
    }

    public Double getPercentage() {return percentage;}
}
