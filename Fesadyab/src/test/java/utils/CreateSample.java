package utils;

import domain.Transaction;

public class CreateSample {
    public static Transaction createSampleTransaction() {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(1);
        return  transaction;
    }

    public static Transaction createSampleTransaction(int transactionId) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        return  transaction;
    }

    public static Transaction createSampleTransaction(int accountId, int amount) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        return  transaction;
    }

    public static Transaction createSampleTransaction(int transactionId,int accountId, int amount) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(transactionId);
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        return  transaction;
    }
    public static Transaction createSampleTransaction(int amount, boolean debit) {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1);
        transaction.setAmount(amount);
        transaction.setDebit(debit);
        return  transaction;
    }

    public static int createSamplePositiveInteger() {
        return 5;
    }

    public static Object createSampleObject() {
        return new Object();
    }
}
