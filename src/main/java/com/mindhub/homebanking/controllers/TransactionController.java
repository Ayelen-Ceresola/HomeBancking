package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.NewTransactionDTO;
import com.mindhub.homebanking.dtos.PaymentsDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CardService cardService;

    @Transactional
    @PostMapping("/transactions")

    public ResponseEntity<Object> sendTransaction(Authentication authentication, @RequestBody NewTransactionDTO newTransactionDTO) {


        if (newTransactionDTO.getNumberSourceAccount().isBlank()) {
            return new ResponseEntity<>("Missing origin account", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getNumberDestinationAccount().isBlank()) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getAmount() == null) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getAmount() <= 0.0) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (newTransactionDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Need to enter the description", HttpStatus.FORBIDDEN);
        }

        Account numberSourceAccount = accountService.findByNumber(newTransactionDTO.getNumberSourceAccount());
        Account numberDestinationAccount = accountService.findByNumber(newTransactionDTO.getNumberDestinationAccount());
        Double amount = newTransactionDTO.getAmount();
        String description = newTransactionDTO.getDescription();


        if (newTransactionDTO.getNumberDestinationAccount().equals(newTransactionDTO.getNumberSourceAccount())) {
            return new ResponseEntity<>("Source account and destination account are the same", HttpStatus.FORBIDDEN);
        }
        if (numberSourceAccount == null) {
            return new ResponseEntity<>("Source account does not exist", HttpStatus.FORBIDDEN);
        }
        if (numberDestinationAccount == null) {
            return new ResponseEntity<>("Destination account does not exist", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        if (client.getAccounts().stream().filter(item -> item.getNumber().equals(newTransactionDTO.getNumberSourceAccount())).collect(Collectors.toSet()).isEmpty()) {

            return new ResponseEntity<>("The source account does not belong to you", HttpStatus.FORBIDDEN);

        }
        if (newTransactionDTO.getAmount() > numberSourceAccount.getBalance()) {
            return new ResponseEntity<>("The amount is higher than the balance", HttpStatus.FORBIDDEN);

        }

        Transaction transactionDebit = new Transaction(TransactionType.DEBIT, Double.parseDouble("-" + amount), newTransactionDTO.getNumberDestinationAccount() + ": " + description, LocalDateTime.now(), numberSourceAccount.getBalance() + amount, true);
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, newTransactionDTO.getNumberSourceAccount() + ": " + description, LocalDateTime.now(), numberDestinationAccount.getBalance() - amount, true);

        numberSourceAccount.setBalance(numberSourceAccount.getBalance() - amount);
        numberDestinationAccount.setBalance(numberDestinationAccount.getBalance() + amount);

        numberSourceAccount.addTransaction(transactionDebit);
        numberDestinationAccount.addTransaction(transactionCredit);

        transactionService.save(transactionDebit);
        transactionService.save(transactionCredit);

        return new ResponseEntity<>("Transaction made", HttpStatus.CREATED);

    }
    @Transactional
    @CrossOrigin(origins= "http://127.0.0.1:5500")
    @PostMapping("/transactions/payments")

    public ResponseEntity<Object> sendPayments(@RequestBody PaymentsDTO paymentsDTO) {

        if (paymentsDTO.getCardNumber().isBlank()) {
            return new ResponseEntity<>("Missing card number", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getCvv() <= 0) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getAmount() == null) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getAmount() <= 0.0) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (paymentsDTO.getDescription().isBlank()) {
            return new ResponseEntity<>("Need to enter the description", HttpStatus.FORBIDDEN);
        }

        Card card = cardService.findByNumber(paymentsDTO.getCardNumber());
        Client client = card.getClient();
        Set<Account> accounts = client.getAccounts().stream().filter(account -> account.isActive()).collect(Collectors.toSet());

        Account account = accounts.stream().filter(account1 -> account1.getBalance() >= paymentsDTO.getAmount()).findFirst().orElse(null);

        if (card == null) {
            return new ResponseEntity<>("Card doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!card.getNumber().equals(paymentsDTO.getCardNumber())) {
            return new ResponseEntity<>("wrong card number", HttpStatus.FORBIDDEN);
        }
        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }

        Transaction transaction = new Transaction(TransactionType.DEBIT, paymentsDTO.getAmount(), paymentsDTO.getDescription(), LocalDateTime.now(), account.getBalance() - paymentsDTO.getAmount(), true);

        transaction.setAccount(account);
        transactionService.save(transaction);
        account.setBalance(account.getBalance() - paymentsDTO.getAmount());
        accountService.save(account);

        return new ResponseEntity<>("Transaction made", HttpStatus.CREATED);
    }


}
