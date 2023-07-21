package com.mindhub.homebanking;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTest {
    @Test

    public void cardNumberIsCreated(){
        String cardNumber = CardUtils.getStringBuilder().toString();
        assertThat(cardNumber,is(not(emptyOrNullString())));

    }

    @Test

    public void cvvNumberIsCreated(){
        int cvvNumber = CardUtils.getCvv();
        assertThat(String.valueOf(cvvNumber).length(), is(3));

    }

    @Test

    public void cvvNumberExist(){
        int cvvNumber = CardUtils.getCvv();
        assertThat(String.valueOf(cvvNumber), is(not(emptyOrNullString())));

    }

}
