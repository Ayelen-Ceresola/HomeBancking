package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {
    List<AccountDTO> findAll();
    AccountDTO getAccountDTO(Long id);
    void save(Account account);
    Account findByNumber(String accountNumber);
    Account findById(Long id);


}
