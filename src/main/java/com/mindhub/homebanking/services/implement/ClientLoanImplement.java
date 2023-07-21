package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ClientLoanImplement implements ClientLoanService {

    @Autowired
    ClientLoanRepository clientLoanRepository;


    @Override
    public ClientLoan findById(Long loanToPay) {
        return clientLoanRepository.findById(loanToPay).orElse(null);
    }

    @Override
    public void save(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);

    }
}
