package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;



@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/accounts")
    public List<AccountDTO> getClients(){
        return accountService.findAll();
    }

    @RequestMapping("/accounts/{id}")
        public AccountDTO getClient(@PathVariable Long id){
        return accountService.getAccountDTO(id);
    }


    @RequestMapping(path = "/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccountCurrentClient(Authentication authentication) {

         Client client= clientService.findByEmail(authentication.getName());

        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Forbidden: Maximum number of accounts reached", HttpStatus.FORBIDDEN);
        }
        Account newAccount = new Account("", LocalDate.now(), 0.0);

        Random random = new Random();
        String accountNumber = "VIN-" + (random.nextInt(90000000) + 100000);

        while (accountService.findByNumber(accountNumber) != null) {
            Random random2 = new Random();
            String accountNumber2 = "VIN-" + (random2.nextInt(90000000) + 100000);
        }
        newAccount.setNumber(accountNumber);
        client.addAccount(newAccount);
        accountService.save(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    }
