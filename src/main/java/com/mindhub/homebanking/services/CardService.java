package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.*;

public interface CardService {
    Card findByClientAndColorAndTypeAndIsActive(Client client, CardColor cardColor, CardType cardType, boolean isActive);
    void save(Card card);
    Card findByNumber(String cardNumber);


}
