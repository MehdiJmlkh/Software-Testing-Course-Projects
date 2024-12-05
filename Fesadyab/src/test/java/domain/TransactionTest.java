package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static utils.CreateSample.*;

public class TransactionTest {
    private Transaction transaction;

    @BeforeEach
    void setup() {
        transaction = createSampleTransaction();
    }

    @Test
    void checkEquality_EqualTransaction_ReturnsTrue() {
        Transaction transaction1 = createSampleTransaction(transaction.getTransactionId());
        assertEquals(transaction, transaction1);
    }

    @Test
    void checkEquality_NotEqualTransaction_ReturnsFalse() {
        Transaction transaction1 = createSampleTransaction(transaction.getTransactionId() + 1);
        assertNotEquals(transaction, transaction1);
    }

    @Test
    void checkEquality_NotTransactionObject_ReturnsFalse() {
        Object object = createSampleObject();
        assertNotEquals(transaction, object);
    }

    // Just to get 100% line coverage in PIT
    @Test
    void settersAndGetters_work() {
        transaction.setTransactionId(1);
        transaction.setAmount(2);
        transaction.setAccountId(3);
        transaction.setDebit(false);

        assertEquals(1, transaction.getTransactionId());
        assertEquals(2, transaction.getAmount());
        assertEquals(3, transaction.getAccountId());
        assertFalse(transaction.isDebit());
    }
}
