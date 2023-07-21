package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.AccountUtils.getAccountNumber;


@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @GetMapping("/accounts")
    public List<AccountDTO> getClients(){
        return accountService.findAll();
    }

    @GetMapping("/accounts/{id}")
        public ResponseEntity<Object> getClient(Authentication authentication,@PathVariable Long id){

        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findById(id);
        if (account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.NOT_FOUND);
        }
        if (!client.getAccounts().contains(account)) {
            return new ResponseEntity<>("Is not your account", HttpStatus.FORBIDDEN);
        }
        if (!account.isActive()) {
            return new ResponseEntity<>("Inactive account", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new AccountDTO(account), HttpStatus.OK);
    }


    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccountCurrentClient(Authentication authentication,@RequestParam AccountType accountType) {

         Client client= clientService.findByEmail(authentication.getName());

        if (client.getAccounts().stream().filter(account -> !account.isActive()).count() > 3) {
            return new ResponseEntity<>("Forbidden: Maximum number of accounts reached", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account("", LocalDate.now(), 0.0,true, accountType);

        String accountNumber = getAccountNumber();

        while (accountService.findByNumber(accountNumber) != null) {
            getAccountNumber();
        }

        newAccount.setNumber(accountNumber);
        client.addAccount(newAccount);
        accountService.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccountIsActive(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        List<Account> accountList= client.getAccounts().stream().filter(account -> account.isActive()).collect(Collectors.toList());
        return accountList.stream().map(AccountDTO::new).collect(Collectors.toList());
    }

    @PatchMapping("/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number) {

        if (number.isBlank()) {
            return new ResponseEntity<>(" Missing account number", HttpStatus.FORBIDDEN);
        }
        if (!authentication.isAuthenticated()) {
            return new ResponseEntity<>("Client is not authenticated", HttpStatus.FORBIDDEN);
        }

        Account account = accountService.findByNumber(number);
        List<Transaction> transactionList = account.getTransactions().stream().filter(transaction -> transaction.isActive())
                .collect(Collectors.toList());

        if (account.getBalance() != 0) {
            return new ResponseEntity<>("Your balance is not at 0", HttpStatus.FORBIDDEN);
        }
        for (Transaction transaction : transactionList) {
            transaction.setActive(false);
        }

        account.setActive(false);
        transactionService.saveAll(transactionList);
        accountService.save(account);
        return new ResponseEntity<>("Account deleted", HttpStatus.OK);


    }

}
