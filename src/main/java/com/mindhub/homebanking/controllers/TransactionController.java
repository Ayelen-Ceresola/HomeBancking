package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.NewTransactionDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")

public class TransactionController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    @RequestMapping(path = "/transactions", method = RequestMethod.POST)

    public ResponseEntity<Object> sendTransaction(Authentication authentication, @RequestBody NewTransactionDTO newTransactionDTO) {

        Account numberSourceAccount = accountRepository.findByNumber(newTransactionDTO.getNumberSourceAccount());
        Account numberDestinationAccount = accountRepository.findByNumber(newTransactionDTO.getNumberDestinationAccount());
        Double amount = newTransactionDTO.getAmount();
        String description = newTransactionDTO.getDescription();

        if (newTransactionDTO.getNumberSourceAccount().isBlank()) {
            return new ResponseEntity<>("Missing origin account", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getAmount() == null) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getAmount() <= 0.0){
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Need to enter the description", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getNumberDestinationAccount().equals(newTransactionDTO.getNumberSourceAccount())) {
            return new ResponseEntity<>("Source account and destination account are the same", HttpStatus.FORBIDDEN);
        }
        if (numberSourceAccount == null) {
            return new ResponseEntity<>("Source account does not exist", HttpStatus.FORBIDDEN);
        }
        if (numberDestinationAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        Client client = clientRepository.findByEmail(authentication.getName());
        if (client.getAccounts().stream().filter(item -> item.getNumber().equals(newTransactionDTO.getNumberSourceAccount())).collect(Collectors.toSet()).isEmpty()) {

            return new ResponseEntity<>("The source account does not belong to you", HttpStatus.FORBIDDEN);

        }
        if (newTransactionDTO.getAmount() > numberSourceAccount.getBalance()) {
            return new ResponseEntity<>("The amount is higher than the balance", HttpStatus.FORBIDDEN);

        }

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, Double.parseDouble("-" + amount), newTransactionDTO.getNumberDestinationAccount() + ": " + description, LocalDateTime.now());
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, newTransactionDTO.getNumberSourceAccount() + ": " + description, LocalDateTime.now());

        numberSourceAccount.setBalance(numberSourceAccount.getBalance() - amount);
        numberDestinationAccount.setBalance(numberDestinationAccount.getBalance() + amount);

        numberSourceAccount.addTransaction(transactionDebit);
        numberDestinationAccount.addTransaction(transactionCredit);

        transactionRepository.save(transactionDebit);
        transactionRepository.save(transactionCredit);

        return new  ResponseEntity<>("Transaction realizada", HttpStatus.CREATED);



    }


}
