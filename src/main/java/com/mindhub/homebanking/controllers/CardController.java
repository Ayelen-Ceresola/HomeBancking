package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.utils.CardUtils.getCvv;
import static com.mindhub.homebanking.utils.CardUtils.getStringBuilder;


@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;
    @Autowired
    private ClientService clientService;


    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCardCurrentClient(Authentication authentication, @RequestParam CardColor cardColor, @RequestParam CardType cardType) {


        Client client = clientService.findByEmail(authentication.getName());
        if (client.getCards().stream().filter(card -> card.getType() == cardType && card.getColor() == cardColor && card.isActive() ).count()>= 1) {
            return new ResponseEntity<>("Forbidden", HttpStatus.FORBIDDEN);

        } else {

            Card newCard = new Card(client.getFirstName() + " " + client.getLastName(), cardType, cardColor, "", 0, LocalDate.now().plusYears(5), LocalDate.now(), true);


            int cvv = getCvv();

            StringBuilder sb = getStringBuilder();

            String cardNumber = sb.toString();

            while (cardService.findByNumber(cardNumber) != null) {
                sb = getStringBuilder();

                cardNumber = sb.toString();

            }

            newCard.setNumber(cardNumber);
            newCard.setCvv(cvv);
            client.addCard(newCard);
            cardService.save(newCard);

            return new ResponseEntity<>("Card create",HttpStatus.CREATED);

        }
    }
    @GetMapping("/clients/current/cards")
    public List<CardDTO> getCurrentCardIsActive(Authentication authentication){
        Client client = clientService.findByEmail(authentication.getName());
        List<CardDTO> cardDTOList= client.getCards().stream().filter(card -> card.isActive())
                .map(card -> new CardDTO(card))
                .collect(Collectors.toList());
        return cardDTOList;

    }

    @PatchMapping("/clients/current/cards/delete")
    public ResponseEntity<Object> deleteCard(Authentication authentication, @RequestParam String cardNumber) {

        if (cardNumber.isBlank()){
            return new ResponseEntity<>("Missing card number", HttpStatus.FORBIDDEN);
        }
        if(!authentication.isAuthenticated()){
            return new ResponseEntity<>("Client is not authenticated", HttpStatus.FORBIDDEN);
        }

        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findByNumber(cardNumber);

        if(client.getCards().stream().noneMatch(card1 -> card1.getNumber().equals(cardNumber))){
            return new ResponseEntity<>("card does not belong to you", HttpStatus.FORBIDDEN);
        }

        card.setIsActive(false);
        cardService.save(card);
        return new ResponseEntity<>("Card deleted", HttpStatus.OK);
    }

    @PostMapping("/cards/renew")
    public ResponseEntity<Object> renewCard(Authentication authentication, @RequestParam String cardNumber){
        if (cardNumber.isBlank()){
            return new ResponseEntity<>("Missing card number", HttpStatus.FORBIDDEN);
        }
        if(!authentication.isAuthenticated()){
            return new ResponseEntity<>("Client is not authenticated", HttpStatus.FORBIDDEN);
        }
        Client client = clientService.findByEmail(authentication.getName());
        Card card = cardService.findByNumber(cardNumber);

        if(!client.getCards().contains(card)){
            return new ResponseEntity<>("card does not belong to you", HttpStatus.FORBIDDEN);
        }

        if(!card.getThruDate().isBefore(LocalDate.now())){
            return new ResponseEntity<>("Card is not expired", HttpStatus.FORBIDDEN);
        }
        card.setIsActive(false);
        cardService.save(card);

        createCardCurrentClient(authentication,card.getColor(),card.getType());
        return new ResponseEntity<>("renewed card", HttpStatus.CREATED);


    }




}
