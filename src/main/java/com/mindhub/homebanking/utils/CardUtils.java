package com.mindhub.homebanking.utils;
import java.util.Random;

public final class CardUtils {
    public static StringBuilder getStringBuilder() {
        Random randomNumber = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int number = randomNumber.nextInt(9000) + 1000;
            sb.append(number);
            if (i < 3) {
                sb.append(" ");
            }
        }
        return sb;
    }


    public static int getCvv() {
        Random randomCvv = new Random();
        int cvv = randomCvv.nextInt(900) + 100;
        return cvv;
    }

}
