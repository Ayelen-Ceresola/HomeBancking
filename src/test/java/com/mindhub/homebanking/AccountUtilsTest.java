package com.mindhub.homebanking;
import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class AccountUtilsTest {

    @Test
    public void accountNumberIsCreated(){
        String accountNumber = CardUtils.getStringBuilder().toString();
        assertThat(accountNumber,is(not(emptyOrNullString())));

    }
}
