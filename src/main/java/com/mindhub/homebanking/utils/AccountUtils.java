package com.mindhub.homebanking.utils;
import java.util.Random;

public final class AccountUtils {
    public static String getAccountNumber() {
        Random random = new Random();
        String accountNumber = "VIN-" + (random.nextInt(90000000) + 100000);
        return accountNumber;
    }


}
