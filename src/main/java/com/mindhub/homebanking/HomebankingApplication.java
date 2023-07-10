package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEnconder;



	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}


	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return args -> {

			Client client1 = new Client( "Melba","Morel", "melba@mindhub.com",passwordEnconder.encode("melba123"));
			Client client2 = new Client( "Rumba","Martinez", "rumbam@mindhub.com", passwordEnconder.encode("123"));
			Client client3 = new Client("Admin","Admin","admin@gmail.com", passwordEnconder.encode("admin1234"));

			Account account1 = new Account("VIN001", LocalDate.now(),5000.00);
			Account account2 = new Account("VIN002",LocalDate.now().plusDays(1),7500.00);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT,-500.00,"debit account", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.CREDIT,1000.00,"credit account", LocalDateTime.now());
			Transaction transaction3= new Transaction(TransactionType.DEBIT,-100.00,"debit account", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,500.00,"credit account", LocalDateTime.now());

			Loan loanMortgage = new Loan("mortgage", 500000.00, Arrays.asList(12, 24, 36, 48, 60));
			Loan loanPersonal = new Loan("personal", 100000.00, Arrays.asList(6,12,24));
			Loan loanCar = new Loan("car",300000.00, Arrays.asList( 6,12,24,36));


			ClientLoan mortgageMelba = new ClientLoan(400000.00,60);
			ClientLoan personalMelba = new ClientLoan(50000.00,12);

			ClientLoan personalRumba = new ClientLoan(100000.00,24);
			ClientLoan carRumba = new ClientLoan(200000.00,36);

			Card card1 = new Card(client1.getFirstName() + " " + client1.getLastName(),CardType.DEBIT,CardColor.GOLD,"1234 5678 9876 5432", 258,LocalDateTime.now().plusYears(5),LocalDateTime.now());
			Card card2 = new Card(client1.getFirstName() + " " + client1.getLastName(),CardType.CREDIT,CardColor.TITANIUM,"1234 5678 1234 5678",147,LocalDateTime.now().plusYears(5),LocalDateTime.now());
			Card card3 = new Card(client2.getFirstName() + " " + client2.getLastName(),CardType.CREDIT,CardColor.SILVER,"9876 5432 1234 5678", 369,LocalDateTime.now().plusYears(5), LocalDateTime.now());

			client1.addAccount(account1);
			client1.addAccount(account2);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account1.addTransaction(transaction4);

			client1.addClientLoan(mortgageMelba);
			client1.addClientLoan(personalMelba);
			loanMortgage.addClientLoan(mortgageMelba);
			loanPersonal.addClientLoan(personalMelba);

			client2.addClientLoan(personalRumba);
			client2.addClientLoan(carRumba);
			loanPersonal.addClientLoan(personalRumba);
			loanCar.addClientLoan(carRumba);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);



			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(client3);

			accountRepository.save(account1);
			accountRepository.save(account2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			loanRepository.save(loanMortgage);
			loanRepository.save(loanPersonal);
			loanRepository.save(loanCar);

			clientLoanRepository.save(mortgageMelba);
			clientLoanRepository.save(personalMelba);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);






		};
	}


}
