
package service;
import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import util.TransactionLog;
import model.Account;
import model.SavingsAccount;
import util.Repository;
import exception.BankException;
import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public class BankService {

    private final Repository<Account> accountRepository;
    private final TransactionProcessor transactionProcessor;
    private final Path logFile =
        Paths.get("data", "logs", "transactions.log");

   private final java.nio.file.Path accountFile =
        java.nio.file.Paths.get("data", "minibank_accounts.dat");

public BankService() {
    accountRepository = new Repository<>();
    transactionProcessor = new TransactionProcessor();

    // Load previously saved accounts
    java.util.ArrayList<Account> savedAccounts =
            util.StatePersister.loadList(accountFile);

    for (Account account : savedAccounts) {
        Account.synchronizeAccountCounter(account.getAccountNumber());
        accountRepository.save(account);
        transactionProcessor.addAccount(account);
    }

    System.out.println(
            "Loaded accounts: " + accountRepository.count()
    );
}

    // Open a new account
    public Account openAccount(String ownerName, long openingBalance) {

        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Owner name cannot be empty."
            );
        }

        if (openingBalance < 0) {
            throw new IllegalArgumentException(
                    "Opening balance cannot be negative."
            );
        }

        Account account = new SavingsAccount(
                ownerName,
                openingBalance,
                1000
        );

        accountRepository.save(account);
        transactionProcessor.addAccount(account);

        return account;
    }

    // Find account
    public Account findAccount(String accountNumber)
            throws AccountNotFoundException {

        Account account = accountRepository.findById(accountNumber);

        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }

        return account;
    }

    // Deposit money
    public void deposit(String accountNumber, long amount)
        throws AccountNotFoundException, InvalidAmountException {

    Account account = findAccount(accountNumber);
    account.deposit(amount);

    TransactionLog.append(
            logFile,
            "DEPOSIT " + accountNumber + " " + amount
    );
}

    // Withdraw money
   public void withdraw(String accountNumber, long amount)
        throws AccountNotFoundException,
               InsufficientFundsException,
               InvalidAmountException {

    Account account = findAccount(accountNumber);
    account.withdraw(amount);

    TransactionLog.append(
            logFile,
            "WITHDRAW " + accountNumber + " " + amount
    );
}

    // Transfer money
    public void transfer(
        String fromAccountNumber,
        String toAccountNumber,
        long amount
) throws BankException {

    Account from = findAccount(fromAccountNumber);
    Account to = findAccount(toAccountNumber);

    SafeTransfer.transferSafely(from, to, amount);

    TransactionLog.append(
            logFile,
            "TRANSFER " + fromAccountNumber
            + " TO " + toAccountNumber
            + " " + amount
    );
}

    // List all accounts
    public ArrayList<Account> listAccounts() {
        return accountRepository.findAll();
    }

    // Get account statement
    public String statement(String accountNumber)
            throws AccountNotFoundException {

        Account account = findAccount(accountNumber);

        return util.StatementFormatter.buildStatement(account);
    }

    // Get total accounts
    public int countAccounts() {
        return accountRepository.count();
    }

    // Get transaction processor
    public TransactionProcessor getTransactionProcessor() {
        return transactionProcessor;
    }

// Process multiple transactions, wait for completion, and report failures.
public void processBatchTransactions(List<? extends Runnable> transactions) {
    if (transactions == null || transactions.isEmpty()) {
        throw new IllegalArgumentException("Transaction list cannot be empty.");
    }

    java.util.concurrent.CountDownLatch latch =
            new java.util.concurrent.CountDownLatch(transactions.size());
    java.util.concurrent.atomic.AtomicInteger failures =
            new java.util.concurrent.atomic.AtomicInteger();
    java.util.concurrent.atomic.AtomicReference<String> firstFailure =
            new java.util.concurrent.atomic.AtomicReference<>();
    java.util.List<Runnable> tasks = new java.util.ArrayList<>();

    for (Runnable transaction : transactions) {
        if (transaction == null) {
            throw new IllegalArgumentException("Batch cannot contain null transactions.");
        }
        tasks.add(() -> {
            try {
                transaction.run();
            } catch (Throwable error) {
                failures.incrementAndGet();
                firstFailure.compareAndSet(null, error.getMessage() == null
                        ? error.getClass().getSimpleName() : error.getMessage());
            } finally {
                latch.countDown();
            }
        });
    }

    transactionProcessor.submitBatch(tasks);
    try {
        boolean completed = latch.await(30, java.util.concurrent.TimeUnit.SECONDS);
        if (!completed) {
            throw new IllegalStateException("Batch processing timed out before all transactions completed.");
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Batch processing was interrupted.", e);
    }

    if (failures.get() > 0) {
        throw new IllegalStateException(failures.get()
                + " transaction(s) failed. First error: " + firstFailure.get());
    }
    System.out.println("All batch transactions completed successfully.");
}

    // Close transaction processor
    public void shutdown() {
        transactionProcessor.shutdown();
    }

    // Save all accounts before closing MiniBank
public void saveAccounts() {
    util.StatePersister.saveList(
            accountRepository.findAll(),
            accountFile
    );

    System.out.println("MiniBank data saved.");
}
}
