package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientLoanService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class ClientLoanController {

    @Autowired
    ClientLoanService clientLoanService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ClientService clientService;

    @PostMapping("/clientLoan/payments/{id}")
    public ResponseEntity<Object> payLoan(Authentication authentication, @RequestParam Long loanId, @RequestParam String account, @PathVariable Long id) {
        if (authentication == null) {
            return new ResponseEntity<>("Client must be authenticated", HttpStatus.FORBIDDEN);
        }
        if (loanId == null) {
            return new ResponseEntity<>("Missing loan information", HttpStatus.FORBIDDEN);
        }
        if (account.isBlank()) {
            return new ResponseEntity<>("Missing account", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Account accountPayment = accountService.findByNumber(account);
        ClientLoan clientLoan = clientLoanService.findById(loanId);
        Double payment = clientLoan.getAmount() / clientLoan.getPayment();

        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (accountPayment == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (accountPayment.getBalance() < payment) {
            return new ResponseEntity<>("Insufficient amount", HttpStatus.FORBIDDEN);
        }
        if (clientLoan.getAmount() <= 0) {
            return new ResponseEntity<>("Fully paid loan", HttpStatus.FORBIDDEN);
        }
        Transaction transaction = new Transaction(TransactionType.DEBIT, payment, "payment made", LocalDateTime.now(), accountPayment.getBalance() - payment, true);
        accountPayment.setBalance(accountPayment.getBalance() - payment);
        clientLoan.setRemainingPayments(clientLoan.getRemainingPayments() - 1);
        clientLoan.setRemainingBalance(clientLoan.getRemainingBalance() - payment);
        accountPayment.addTransaction(transaction);
        accountService.save(accountPayment);
        clientLoanService.save(clientLoan);
        transactionService.save(transaction);
        return new ResponseEntity<>("Loan payment correct", HttpStatus.OK);

    }

    @GetMapping("/client/loans/{id}")
    public ResponseEntity<Object> getOneLoan(@PathVariable Long id, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        ClientLoan loan = clientLoanService.findById(id);
        if (loan == null) {
            return new ResponseEntity<>("not loan", HttpStatus.NOT_FOUND);
        }
        if (!client.getLoan().contains(loan)) {
            return new ResponseEntity<>("the loan does not belong to you", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new ClientLoanDTO(loan), HttpStatus.OK);
    }
}
