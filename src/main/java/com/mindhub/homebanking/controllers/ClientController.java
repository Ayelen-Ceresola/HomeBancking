package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping ("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll()
                .stream()
                .map(client -> new ClientDTO(client))
                .collect(Collectors.toList());
    }
    @RequestMapping ("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){
        return clientRepository.findById(id).map(clientDTO -> new ClientDTO(clientDTO)).orElse(null);
    }

    @Autowired

    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/clients", method = RequestMethod.POST)

    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isBlank()){
            return new ResponseEntity<>("Missing first name", HttpStatus.FORBIDDEN);
        }
        if (lastName.isBlank()){
            return new ResponseEntity<>("Missing last name", HttpStatus.FORBIDDEN);
        }
        if (email.isBlank()){
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }
        if (password.isBlank()){
            return new ResponseEntity<>("password", HttpStatus.FORBIDDEN);
        }


        if (clientRepository.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }

        Client client =new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        Account account= new Account("", LocalDate.now(),0.0);

        Random random= new Random();
        String accountNumber = "VIN-" + (random.nextInt(90000000) + 100000);

         while (accountRepository.findByNumber(accountNumber) == accountNumber ){
             Random random2= new Random();
             String accountNumber2 = "VIN-" + (random2.nextInt(90000000) + 100000);
             }
        account.setNumber(accountNumber);
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    @RequestMapping("/clients/current")
    public ClientDTO getAuthenticatedClientDTO(Authentication authentication){
        return new ClientDTO(clientRepository.findByEmail(authentication.getName()));
    }


}
