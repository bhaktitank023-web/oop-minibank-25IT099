
package service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

import model.Account;

public class TransactionProcessor {

    // Thread pool with 4 worker threads
    private final ExecutorService executor;

    // Thread-safe shared account storage
    private final ConcurrentHashMap<String, Account> sharedAccounts =
            new ConcurrentHashMap<>();

    public TransactionProcessor() {
        executor = Executors.newFixedThreadPool(4);
    }

    // Add account to shared storage
    public void addAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException(
                    "Account cannot be null."
            );
        }

        sharedAccounts.put(account.getAccountNumber(), account);
    }

    // Find account by account number
    public Account getAccount(String accountNumber) {
        return sharedAccounts.get(accountNumber);
    }

    // Get all shared accounts
    public ConcurrentHashMap<String, Account> getSharedAccounts() {
        return new ConcurrentHashMap<>(sharedAccounts);
    }

    // Submit one transaction
    public void submit(Runnable task) {
        if (task == null) {
            throw new IllegalArgumentException(
                    "Transaction cannot be null."
            );
        }

        executor.execute(task);
    }

    // Submit multiple transactions
    public void submitBatch(List<? extends Runnable> transactions) {
        if (transactions == null) {
            throw new IllegalArgumentException(
                    "Transaction list cannot be null."
            );
        }

        for (Runnable transaction : transactions) {
            if (transaction == null) {
                throw new IllegalArgumentException(
                        "Batch cannot contain null transactions."
                );
            }
        }
        for (Runnable transaction : transactions) {
            submit(transaction);
        }
    }

    // Keep compatibility with Practical 10 and 12
    public void submitTransaction(Runnable transaction) {
        submit(transaction);
    }

    // Stop the thread pool and wait for submitted tasks
    public void stop() {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                System.out.println(
                        "Warning: Some transactions were interrupted."
                );
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Keep compatibility with existing project code
    public void shutdown() {
        stop();
    }
}
