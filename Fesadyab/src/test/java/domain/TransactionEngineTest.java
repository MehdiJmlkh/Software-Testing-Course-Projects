package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static utils.CreateSample.*;

public class TransactionEngineTest {
    private TransactionEngine transactionEngine;

    @BeforeEach
    void setup() {
        transactionEngine = new TransactionEngine();
    }

    @Test
    void getAverageTransactionAmountByAccount_SomeMatchingHistory_ReturnsAverageTransactionAmount() {
        List<Transaction> transactions = List.of(
                createSampleTransaction(1, 5),
                createSampleTransaction(2, 10),
                createSampleTransaction(1, 20)
        );
        transactionEngine.transactionHistory.addAll(transactions);
        int result = transactionEngine.getAverageTransactionAmountByAccount(1);
        assertEquals(12, result);
    }

    @Test
    void getAverageTransactionAmountByAccount_NoMatchingHistory_ReturnsZero() {
        List<Transaction> transactions = List.of(
                createSampleTransaction(1, 5),
                createSampleTransaction(2, 10)
        );
        transactionEngine.transactionHistory.addAll(transactions);
        int result = transactionEngine.getAverageTransactionAmountByAccount(3);
        assertEquals(0, result);
    }

    @Test
    void getTransactionPatternAboveThreshold_EmptyHistory_ReturnsZero() {
        transactionEngine.transactionHistory.clear();
        int threshold = createSamplePositiveInteger();

        int result = transactionEngine.getTransactionPatternAboveThreshold(threshold);

        assertEquals(0, result);

    }

    @Test
    void getTransactionPatternAboveThreshold_WhenSomeBelowThreshold_ReturnsPattern() {
        List<Transaction> transactions = List.of(
                createSampleTransaction(1, 1, 5),
                createSampleTransaction(2, 1, 15),
                createSampleTransaction(3, 1, 25),
                createSampleTransaction(3, 1, 40)
        );
        transactionEngine.transactionHistory.addAll(transactions);

        int result = transactionEngine.getTransactionPatternAboveThreshold(15);

        assertEquals(20, result);
    }

    @Test
    void getTransactionPatternAboveThreshold_WithoutPattern_ReturnsZero() {
        List<Transaction> transactions = List.of(
                createSampleTransaction(1, 1, 5),
                createSampleTransaction(2, 1, 15),
                createSampleTransaction(3, 1, 25),
                createSampleTransaction(4, 1, 35)
        );
        transactionEngine.transactionHistory.addAll(transactions);

        int result = transactionEngine.getTransactionPatternAboveThreshold(0);

        assertEquals(0, result);
    }

    @Test
    void detectFraudulentTransaction_SuspiciousTransaction_ReturnsFraudScore() {
        Transaction transaction = createSampleTransaction(30, true);
        TransactionEngine transactionEngineStub = spy(TransactionEngine.class);
        when(transactionEngineStub.getAverageTransactionAmountByAccount(transaction.getAccountId()))
                .thenReturn(10);

        int result = transactionEngineStub.detectFraudulentTransaction(transaction);

        assertEquals(10, result);
    }

    @Test
    void detectFraudulentTransaction_NotDebitTransaction_ReturnsZero() {
        Transaction transaction = createSampleTransaction(30, false);
        TransactionEngine transactionEngineStub = spy(TransactionEngine.class);
        when(transactionEngineStub.getAverageTransactionAmountByAccount(transaction.getAccountId()))
                .thenReturn(10);

        int result = transactionEngineStub.detectFraudulentTransaction(transaction);

        assertEquals(0, result);
    }

    @Test
    void detectFraudulentTransaction_DebitTransactionWithoutExtraAmount_ReturnsZero() {
        Transaction transaction = createSampleTransaction(10, true);
        TransactionEngine transactionEngineStub = spy(TransactionEngine.class);
        when(transactionEngineStub.getAverageTransactionAmountByAccount(transaction.getAccountId()))
                .thenReturn(10);

        int result = transactionEngineStub.detectFraudulentTransaction(transaction);

        assertEquals(0, result);
    }

    @Test
    void addTransaction_DuplicatedTransaction_DoesNotAddTransactionAndReturnsZero() {
        Transaction transaction = createSampleTransaction();
        transactionEngine.transactionHistory.add(transaction);

        int result = transactionEngine.addTransactionAndDetectFraud(transaction);

        assertEquals(0, result);
        assertEquals(1, transactionEngine.transactionHistory.size());
    }

    @Test
    void addTransaction_FraudulentTransaction_AddsTransactionAndReturnsFraudScore() {
        Transaction transaction = createSampleTransaction();
        TransactionEngine transactionEngineStub = spy(TransactionEngine.class);
        when(transactionEngineStub.detectFraudulentTransaction(transaction))
                .thenReturn(10);

        int result = transactionEngineStub.addTransactionAndDetectFraud(transaction);

        assertEquals(1, transactionEngineStub.transactionHistory.size());
        assertTrue(transactionEngineStub.transactionHistory.contains(transaction));
        assertEquals(10, result);
    }

    @Test
    void addTransaction_NormalTransaction_AddsTransactionAndReturnsTransactionPattern() {
        Transaction transaction = createSampleTransaction();
        TransactionEngine transactionEngineStub = spy(TransactionEngine.class);
        when(transactionEngineStub.detectFraudulentTransaction(transaction))
                .thenReturn(0);
        when(transactionEngineStub.getTransactionPatternAboveThreshold(transactionEngineStub.THRESHOLD))
                .thenReturn(20);

        int result = transactionEngineStub.addTransactionAndDetectFraud(transaction);

        assertEquals(1, transactionEngineStub.transactionHistory.size());
        assertTrue(transactionEngineStub.transactionHistory.contains(transaction));
        assertEquals(20, result);
    }
}
