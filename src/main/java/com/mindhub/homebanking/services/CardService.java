package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;

public interface CardService {
    Card findByClientAndColorAndType(Client client, CardColor cardColor, CardType cardType);
    void save(Card card);
    Card findByNumber(String cardNumber);

}
