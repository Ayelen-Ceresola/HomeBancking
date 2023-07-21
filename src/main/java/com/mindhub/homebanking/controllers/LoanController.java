package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/api")
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanRepository clientLoanRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/loans")

    public List<LoanDTO> loanDTOList(){
        return loanService.findAll();
    }
    @Transactional
    @PostMapping("/loans")

    public ResponseEntity<Object> loanRequest(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        if (loanApplicationDTO.getDestinationAccount() == null) {
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getLoanTypeId() == null) {
            return new ResponseEntity<>("Missing loan type", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() == null) {
            return new ResponseEntity<>("Missing payments", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() == null) {
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }

        Loan loanType = loanService.findById(loanApplicationDTO.getLoanTypeId());
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(loanApplicationDTO.getDestinationAccount());

        if (loanType == null) {
            return new ResponseEntity<>("Incorrect loan type", HttpStatus.FORBIDDEN);
        }

        if (loanApplicationDTO.getAmount() < 10000) {
            return new ResponseEntity<>("amount less than allowed", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Incorrect payments amount", HttpStatus.FORBIDDEN);
        }
        if (!loanType.getPayments().contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("The amount of payments is incorrect", HttpStatus.FORBIDDEN);
        }
        if (loanApplicationDTO.getAmount() > loanType.getMaxAmount()) {
            return new ResponseEntity<>("The amount entered exceeds the maximum amount", HttpStatus.FORBIDDEN);
        }
        if (client.getAccounts().stream().noneMatch(account1 -> account1.getNumber().equals(loanApplicationDTO.getDestinationAccount()))) {
            return new ResponseEntity<>("The account does not belong to the customer", HttpStatus.FORBIDDEN);
        }

        Double interestsLoan = (loanApplicationDTO.getAmount() * loanType.getPercentage()) + (loanApplicationDTO.getAmount());
        ClientLoan newLoan = new ClientLoan(interestsLoan, loanApplicationDTO.getPayments(),loanApplicationDTO.getPayments(),loanApplicationDTO.getAmount());
        Transaction newTransaction = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loanType.getName() + " Loan approved", LocalDateTime.now(), account.getBalance()+ loanApplicationDTO.getAmount(),true) ;

        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        client.addClientLoan(newLoan);
        loanType.addClientLoan(newLoan);
        account.addTransaction(newTransaction);
        transactionService.save(newTransaction);
        clientLoanRepository.save(newLoan);
        accountService.save(account);
        loanService.save(loanType);
        clientService.save(client);

        return new ResponseEntity<>("Loan credited to account", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/loans/create")
    public ResponseEntity<Object> loanCreateAdmin(@RequestBody LoanDTO loanDTO, Authentication authentication){

        Client client = clientService.findByEmail(authentication.getName());
        if(client == null){
            return new ResponseEntity<>("no autenticado", HttpStatus.FORBIDDEN);
        }

        if (loanDTO.getName() == null) {
            return new ResponseEntity<>("Missing loan name", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getName().isBlank()) {
            return new ResponseEntity<>("Missing loan name", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPayments() == null) {
            return new ResponseEntity<>("Missing payments", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getMaxAmount() <= 0){
            return new ResponseEntity<>("Missing amount", HttpStatus.FORBIDDEN);
        }
        if (loanDTO.getPercentage() <= 0.0){
            return new ResponseEntity<>("Missing percentage", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getPayments(),loanDTO.getPercentage());
        loanService.save(loan);
        return new ResponseEntity<>("Loan created",HttpStatus.CREATED);

    }

}
