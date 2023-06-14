package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

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

}
