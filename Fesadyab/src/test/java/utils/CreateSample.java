package utils;

import domain.Transaction;
import domain.TransactionEngine;

public class CreateSample {
    public static Transaction createSampleTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        return  transaction;
    }

    public static Transaction createSampleTransaction(int id) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(id);
        return  transaction;
    }

    public static Object createSampleObject() {
        return new Object();
    }
}
