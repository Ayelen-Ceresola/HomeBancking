package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping("/accounts")
    public List<AccountDTO> getClients(){
        return accountRepository.findAll()
                .stream()
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toList());
    }

    @RequestMapping("/accounts/{id}")
        public AccountDTO getClient(@PathVariable Long id){
        return accountRepository.findById(id).map(accountDto ->new AccountDTO(accountDto)).orElse(null);


    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccountCurrentClient(Authentication authentication) {

         Client client= clientRepository.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Forbidden: Maximum number of accounts reached", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account("", LocalDate.now(), 0.0);

        Random random = new Random();
        String accountNumber = "VIN-" + (random.nextInt(90000000) + 100000);

        while (accountRepository.findByNumber(accountNumber) == accountNumber) {
            Random random2 = new Random();
            String accountNumber2 = "VIN-" + (random2.nextInt(90000000) + 100000);
        }
        newAccount.setNumber(accountNumber);
        client.addAccount(newAccount);
        accountRepository.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    }
