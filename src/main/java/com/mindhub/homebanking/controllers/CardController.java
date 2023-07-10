package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
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
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity<Object> createCardCurrentClient(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType) {



        Client client= clientService.findByEmail(authentication.getName());

        if (cardService.findByClientAndColorAndType(client,cardColor,cardType )!= null){

            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);

        }else {

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

            while (cardService.findByNumber(cardNumber) != null) {
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
            cardService.save(newCard);

            return new ResponseEntity<>(HttpStatus.CREATED);

        }
    }
}
