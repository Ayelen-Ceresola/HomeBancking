package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCardCurrentClient(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType) {



        Client client= clientRepository.findByEmail(authentication.getName());

        if (cardRepository.findByClientAndColorAndType(client,cardColor,cardType )!= null){

            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);

        }else {

//        Set<Card> clientCards = client.getCards();
//
//        long debitGoldCount = clientCards.stream()
//                .filter(card -> card.getType() == CardType.DEBIT && card.getColor() == CardColor.GOLD)
//                .count();
//        long debitPlatinumCount = clientCards.stream()
//                .filter(card -> card.getType() == CardType.DEBIT && card.getColor() == CardColor.TITANIUM)
//                .count();
//        long debitSilverCount = clientCards.stream()
//                .filter(card -> card.getType() == CardType.DEBIT && card.getColor() == CardColor.SILVER)
//                .count();
//        long creditGoldCount = clientCards.stream()
//                .filter(card -> card.getType() == CardType.CREDIT && card.getColor() == CardColor.GOLD)
//                .count();
//        long creditPlatinumCount = clientCards.stream()
//                .filter(card -> card.getType() == CardType.CREDIT && card.getColor() == CardColor.TITANIUM)
//                .count();
//        long creditSilverCount = clientCards.stream()
//                .filter(card -> card.getType() == CardType.CREDIT && card.getColor() == CardColor.SILVER)
//                .count();
//
//        if (cardType == CardType.DEBIT) {
//            if (CardColor.GOLD == CardColor.GOLD && debitGoldCount == 1) {
//                return new ResponseEntity<>("Forbidden: The client already has a gold debit card", HttpStatus.FORBIDDEN);
//            } else if (CardColor.TITANIUM == CardColor.TITANIUM && debitPlatinumCount == 1) {
//                return new ResponseEntity<>("Forbidden: The client already has a platinum debit card", HttpStatus.FORBIDDEN);
//            } else if (CardColor.SILVER == CardColor.SILVER && debitSilverCount == 1) {
//                return new ResponseEntity<>("Forbidden: The client already has a silver debit card", HttpStatus.FORBIDDEN);
//            }
//        } else if (cardType == CardType.CREDIT) {
//            if (CardColor.GOLD == CardColor.GOLD && creditGoldCount == 1) {
//                return new ResponseEntity<>("Forbidden: The client already has a gold credit card", HttpStatus.FORBIDDEN);
//            } else if (CardColor.TITANIUM == CardColor.TITANIUM && creditPlatinumCount == 1) {
//                return new ResponseEntity<>("Forbidden: The client already has a platinum credit card", HttpStatus.FORBIDDEN);
//            } else if (CardColor.SILVER == CardColor.SILVER && creditSilverCount == 1) {
//                return new ResponseEntity<>("Forbidden: The client already has a silver credit card", HttpStatus.FORBIDDEN);
//            }
//        }

            Card newCard = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, "", 0, LocalDateTime.now().plusYears(5), LocalDateTime.now());


            Random randomCvv = new Random();
            int cvv = randomCvv.nextInt(900) + 100;

            Random randomNumber = new Random();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < 4; i++) {
                int number = randomNumber.nextInt(9000) + 1000;
                sb.append(number);
                if (i < 3) {
                    sb.append(" ");
                }
            }

            String cardNumber = sb.toString();

            while (cardRepository.findByNumber(cardNumber) != null) {
                Random randomNumber2 = new Random();
                StringBuilder sb2 = new StringBuilder();

                for (int i = 0; i < 4; i++) {
                    int number = randomNumber2.nextInt(9000) + 1000;
                    sb.append(number);
                    if (i < 3) {
                        sb2.append("-");

                    }
                }
                String cardNumber2 = sb.toString();
            }

            newCard.setNumber(cardNumber);
            newCard.setCvv(cvv);
            client.addCard(newCard);
            cardRepository.save(newCard);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }
    }
}
