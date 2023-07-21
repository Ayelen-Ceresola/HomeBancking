package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public interface ClientLoanService {

    ClientLoan findById(Long loanToPay);

    void save(ClientLoan clientLoan);
}
