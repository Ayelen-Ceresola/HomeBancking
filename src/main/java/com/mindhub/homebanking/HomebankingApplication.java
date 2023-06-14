package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return args -> {

			Client client1 = new Client( "Melba","Morel", " melba@mindhub.com");
			Client client2 = new Client( "Rumba","Martinez", " rumbam@mindhub.com");

			Account account1 = new Account("VIN001", LocalDate.now(),5000.00);
			Account account2 = new Account("VIN002",LocalDate.now().plusDays(1),7500.00);

			Transaction transaction1 = new Transaction(TransactionType.DEBIT,-500.00,"debit account", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.CREDIT,1000.00,"credit account", LocalDateTime.now());
			Transaction transaction3= new Transaction(TransactionType.DEBIT,-100.00,"debit account", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,500.00,"credit account", LocalDateTime.now());



			clientRepository.save(client1);
			clientRepository.save(client2);

			client1.addAccount(account1);
			client1.addAccount(account2);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account1.addTransaction(transaction3);
			account1.addTransaction(transaction4);

			accountRepository.save(account1);
			accountRepository.save(account2);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);


		};
	}


}
