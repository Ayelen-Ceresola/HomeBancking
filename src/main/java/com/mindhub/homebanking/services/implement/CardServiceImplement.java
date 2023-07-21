package com.mindhub.homebanking.services.implement;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public Card findByClientAndColorAndTypeAndIsActive(Client client, CardColor cardColor, CardType cardType, boolean isActive) {
        return cardRepository.findByClientAndColorAndTypeAndIsActive(client,cardColor ,cardType, isActive );
    }

    @Override
    public void save(Card card) {cardRepository.save(card);}

    @Override
    public Card findByNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }


}
