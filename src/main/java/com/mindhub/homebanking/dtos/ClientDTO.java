package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Set<AccountDTO> accounts;

        private Set<ClientLoanDTO> loans;



        public ClientDTO(){}

        public ClientDTO(Client client) {
            this.id = client.getId();
            this.firstName = client.getFirstName();
            this.lastName = client.getLastName();
            this.email = client.getEmail();
            this.accounts= client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
            this.loans= client.getLoan().stream().map(loan-> new ClientLoanDTO(loan)).collect(Collectors.toSet());
        }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }
}
