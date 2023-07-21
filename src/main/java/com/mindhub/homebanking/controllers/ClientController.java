package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountRepository accountRepository;


    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAll();

    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id){

        return clientService.getClientDTO(id);
    }

    @Autowired

    private PasswordEncoder passwordEncoder;

    @PostMapping("/clients")

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


        if (clientService.findByEmail(email) !=  null) {

            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);

        }

        Client client =new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientService.save(client);

        Account account= new Account("", LocalDate.now(),0.0,true, AccountType.SAVINGS);

        Random random= new Random();
        String accountNumber = "VIN-" + (random.nextInt(90000000) + 100000);

         while (accountRepository.findByNumber(accountNumber) != null ){
             Random random2= new Random();
             String accountNumber2 = "VIN-" + (random2.nextInt(90000000) + 100000);
             }
        account.setNumber(accountNumber);
        client.addAccount(account);
        accountRepository.save(account);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
    @GetMapping("/clients/current")
    public ClientDTO getAuthenticatedClientDTO(Authentication authentication){
        return new ClientDTO(clientService.findByEmail(authentication.getName()));
    }


}
