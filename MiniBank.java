import service.SafeTransfer;
import service.TransactionBuffer;
import service.TransactionProducer;
import service.TransactionConsumer;
import service.TransactionProcessor;
import service.AccountWorker;
import service.BankService;
import util.BankSession;
import exception.AccountNotFoundException;
import util.AccountFinder;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import util.AccountStore;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;



// model package
import model.Account;
import model.CurrentAccount;
import model.Customer;
import model.FixedDepositAccount;
import model.Premium;
import model.SavingsAccount;

// service package
import service.WithdrawRule;

// util package
import util.Command;
import util.CommandParser;
import util.ReportGenerator;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;
import util.Validator;

// Static import - Practical 6
import static util.Validator.isValidMobile;

import util.AnnotationValidator;

public class MiniBank {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Practical 14 - Bank Service
BankService bankService = new BankService();

        // ==========================================
        // PRACTICAL 1 - BANK INFORMATION
        // ==========================================

        BankInfo bank =
                new BankInfo("MiniBank", "CHARUSAT");

        System.out.println("=================================");
        System.out.println("        WELCOME TO " + bank.name());
        System.out.println("        Branch: " + bank.branch());
        System.out.println("=================================");


        // ==========================================
        // PRACTICAL 2 - CUSTOMERS
        // ==========================================

        Customer customer1 =
                new Customer(
                        "Riya",
                        "riya@gmail.com",
                        "9876543210"
                );

        Customer customer2 =
                new Customer(
                        "Bhakti",
                        "bhakti@gmail.com",
                        "9876543211"
                );

        System.out.println("\n--- Customers ---");

        System.out.println(
                customer1.getCustomerId()
                + " | "
                + customer1.getName()
        );

        System.out.println(
                customer2.getCustomerId()
                + " | "
                + customer2.getName()
        );


        // ==========================================
        // PRACTICAL 5 - ACCOUNT TYPES
        // ==========================================

        Account[] accounts = {

                new SavingsAccount(
                        "Riya",
                        5000,
                        1000
                ),

                new CurrentAccount(
                        "Bhakti",
                        10000,
                        5000
                ),

                new FixedDepositAccount(
                        "Kavya",
                        4000
                )
        };


        // ==========================================
        // TRANSACTIONS
        // ==========================================

        
try {
    accounts[0].deposit(2000);
    accounts[0].withdraw(1000);

    accounts[1].deposit(3000);
    accounts[1].withdraw(2000);

    accounts[2].deposit(5000);
    accounts[2].withdraw(1000);

} catch (BankException e) {
    System.out.println("Transaction error: " + e.getMessage());
}



        // ==========================================
        // PRACTICAL 5 - POLYMORPHISM
        // ==========================================

        System.out.println("\n--- Practical 5 - Polymorphism ---");

        for (Account account : accounts) {

            System.out.println(
                    account.getAccountNumber()
                    + " | "
                    + account.getOwnerName()
                    + " | Interest Rate: "
                    + account.interestRate()
                    + "%"
            );
        }


        // ==========================================
        // PRACTICAL 6 - DEFAULT INTERFACE METHOD
        // ==========================================

        System.out.println(
                "\n--- Practical 6 - Yearly Interest ---"
        );

        for (Account account : accounts) {

            System.out.println(
                    account.getAccountNumber()
                    + " | Yearly Interest: Rs. "
                    + account.yearlyInterest()
            );
        }


        // ==========================================
        // PRACTICAL 6 - PREMIUM MARKER INTERFACE
        // ==========================================

        System.out.println(
                "\n--- Practical 6 - Premium Check ---"
        );

        if (accounts[0] instanceof Premium) {

            System.out.println(
                    accounts[0].getAccountNumber()
                    + " is a Premium Account."
            );
        }

        if (!(accounts[1] instanceof Premium)) {

            System.out.println(
                    accounts[1].getAccountNumber()
                    + " is not a Premium Account."
            );
        }


        // ==========================================
        // PRACTICAL 6 - ANONYMOUS CLASS
        // ==========================================

        WithdrawRule anonymousRule =
                new WithdrawRule() {

                    @Override
                    public boolean allow(
                            Account account,
                            long amount) {

                        return amount > 0
                                && account.getBalance() >= amount;
                    }
                };

        System.out.println(
                "\n--- Practical 6 - Anonymous WithdrawRule ---"
        );

        System.out.println(
                "Anonymous rule allows withdrawal: "
                + anonymousRule.allow(
                        accounts[0],
                        500
                )
        );


        // ==========================================
        // PRACTICAL 6 - LAMBDA
        // ==========================================

        WithdrawRule lambdaRule =
                (account, amount) ->
                        amount > 0
                        && account.getBalance() >= amount;

        System.out.println(
                "\n--- Practical 6 - Lambda WithdrawRule ---"
        );

        System.out.println(
                "Lambda rule allows withdrawal: "
                + lambdaRule.allow(
                        accounts[0],
                        500
                )
        );


        // ==========================================
        // PRACTICAL 3 - toString()
        // ==========================================

        System.out.println("\n--- Practical 3 ---");

        System.out.println(
                "Account using toString():"
        );

        System.out.println(accounts[0]);


        // ==========================================
        // PRACTICAL 5 - INSTANCEOF
        // ==========================================

        System.out.println(
                "\n--- Practical 5 - instanceof ---"
        );

        if (accounts[0] instanceof SavingsAccount savings) {

            System.out.println(
                    "Account "
                    + savings.getAccountNumber()
                    + " is a Savings Account."
            );

            System.out.println(
                    "Minimum Balance: "
                    + savings.getMinBalance()
            );
        }


        // ==========================================
        // PRACTICAL 3 - ADDRESS & CLONE
        // ==========================================

        Customer.Address address =
                new Customer.Address(
                        "CSPIT Road",
                        "Anand",
                        "388120"
                );

        Customer customerWithAddress =
                new Customer(
                        "Kavya",
                        "kavya@gmail.com",
                        "9876543212",
                        address
                );

        System.out.println("\nCustomer Address:");

        System.out.println(
                customerWithAddress.getAddress().getLine()
                + ", "
                + customerWithAddress.getAddress().getCity()
                + " - "
                + customerWithAddress.getAddress().getPincode()
        );

        Customer clonedCustomer =
                customerWithAddress.clone();

        System.out.println("\nClone Test:");

        System.out.println(
                "Original Customer: "
                + customerWithAddress.getName()
        );

        System.out.println(
                "Cloned Customer: "
                + clonedCustomer.getName()
        );

        System.out.println(
                "Are they the same object? "
                + (customerWithAddress == clonedCustomer)
        );


        // ==========================================
        // PRACTICAL 4 - VALIDATION
        // ==========================================

        System.out.println(
                "\n--- Practical 4 - Validation ---"
        );

        // Static import allows us to call
        // isValidMobile() without Validator.
        System.out.println(
                "Valid Mobile: "
                + isValidMobile("9876543210")
        );

        System.out.println(
                "Valid Email: "
                + Validator.isValidEmail(
                        "bhakti@gmail.com"
                )
        );

        System.out.println(
                "Valid PAN: "
                + Validator.isValidPan(
                        "ABCDE1234F"
                )
        );

        System.out.println(
                "Valid IFSC: "
                + Validator.isValidIfsc(
                        "SBIN0001234"
                )
        );


        // ==========================================
        // PRACTICAL 4 - COMMAND PARSER
        // ==========================================

        Command command =
                CommandParser.parse(
                        "DEPOSIT AC0001 500"
                );

        System.out.println(
                "\n--- Practical 4 - Command ---"
        );

        System.out.println(
                "Transaction Type: "
                + command.type()
        );

        System.out.println(
                "Account Number: "
                + command.accountNumber()
        );

        System.out.println(
                "Amount: "
                + command.amount()
        );


        // ==========================================
        // PRACTICAL 4 - ACCOUNT STATEMENT
        // ==========================================

        System.out.println(
                "\n--- Practical 4 - Account Statement ---"
        );

        System.out.println(
                StatementFormatter.buildStatement(
                        accounts[0]
                )
        );

// ==========================================
// PRACTICAL 7 - ANNOTATION VALIDATION
// ==========================================

System.out.println(
        "\n--- Practical 7 - Annotation Validation ---"
);

// Invalid Account
Account invalidAccount =
        new SavingsAccount(
                "Invalid User",
                -100,
                1000
        );

String[] invalidErrors =
        AnnotationValidator.validate(invalidAccount);

System.out.println("Invalid Account Errors:");

for (String error : invalidErrors) {

    System.out.println(error);
}


// Valid Account
Account validAccount =
        new SavingsAccount(
                "Riya",
                5000,
                1000
        );

String[] validErrors =
        AnnotationValidator.validate(validAccount);

System.out.println("\nValid Account Errors:");

if (validErrors.length == 0) {

    System.out.println("No validation errors.");
}


// ==========================================
// PRACTICAL 7 - MAX LENGTH TEST
// ==========================================

System.out.println("\n--- Practical 7 - MaxLength Test ---");

Customer longNameCustomer = new Customer(
        "ThisCustomerNameIsMuchTooLong",
        "test@gmail.com",
        "9876543210"
);

String[] customerErrors =
        AnnotationValidator.validate(longNameCustomer);

for (String error : customerErrors) {
    System.out.println(error);
}


System.out.println("\n--- Practical 8 - Exception Handling ---");

Account testAccount = new SavingsAccount(
        "Test User",
        1000,
        1000
);

// Test 1: Insufficient funds
try {
    testAccount.withdraw(5000);
} catch (InsufficientFundsException e) {
    System.out.println("Withdrawal failed: " + e.getMessage());
    System.out.println("Shortfall: Rs. " + e.getShortfall());
} catch (InvalidAmountException e) {
    System.out.println("Invalid amount: " + e.getMessage());
}

// Test 2: Invalid deposit
try {
    testAccount.deposit(-500);
} catch (InvalidAmountException e) {
    System.out.println("Deposit failed: " + e.getMessage());
}

// Test 3: Successful deposit
try {
    testAccount.deposit(500);
    System.out.println(
            "Deposit successful. Balance: Rs. "
            + testAccount.getBalance()
    );
} catch (InvalidAmountException e) {
    System.out.println("Deposit failed: " + e.getMessage());
}


System.out.println("\n--- Practical 8 - Account Not Found Test ---");

try {
    AccountFinder.findByNumber(accounts, "AC9999");

} catch (AccountNotFoundException e) {
    System.out.println("Lookup failed: " + e.getMessage());
}


System.out.println("\n--- Practical 8 - Try-with-Resources ---");

try (BankSession session = new BankSession()) {
    session.performOperation();
}


        // ==========================================
        // PRACTICAL 9 - MULTITHREADING
        // ==========================================

        System.out.println("\n--- Practical 9 - Multithreading ---");

        Account threadAccount = new SavingsAccount(
                "Thread Test",
                0,
                0
        );

        int numberOfThreads = 10;
        int depositsPerThread = 1000;

        Thread[] threads = new Thread[numberOfThreads];

        System.out.println(
                "Initial Balance: " + threadAccount.getBalance()
        );

        for (int i = 0; i < numberOfThreads; i++) {

            AccountWorker worker = new AccountWorker(
                    threadAccount,
                    depositsPerThread,
                    1
            );

            threads[i] = new Thread(
                    worker,
                    "Worker-" + (i + 1)
            );

            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Main thread interrupted.");
                return;
            }
        }

        System.out.println(
                "Final Balance: " + threadAccount.getBalance()
        );

        System.out.println(
                "Expected Balance: "
                + (numberOfThreads * depositsPerThread)
        );
// ==========================================
// PRACTICAL 10 - THREAD POOL
// ==========================================
System.out.println("\n--- Practical 10 - Thread Pool ---");

Account poolAccount = new SavingsAccount(
        "Thread Pool Test",
        0,
        0
);

TransactionProcessor processor = new TransactionProcessor();

int tasks = 10;

for (int i = 1; i <= tasks; i++) {
    processor.submitTransaction(
            new AccountWorker(poolAccount, 100, 1)
    );
}

processor.shutdown();

System.out.println("Thread Pool Final Balance: "
        + poolAccount.getBalance());

System.out.println("Expected Balance: 1000");


// ==========================================
// PRACTICAL 10 - PRODUCER CONSUMER
// ==========================================
System.out.println("\n--- Practical 10 - Producer Consumer ---");

TransactionBuffer buffer = new TransactionBuffer(2);

Thread producer = new Thread(
        new TransactionProducer(buffer),
        "Producer"
);

Thread consumer = new Thread(
        new TransactionConsumer(buffer),
        "Consumer"
);

// Start both threads
consumer.start();
producer.start();

// Wait for both threads
try {
    producer.join();
    consumer.join();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.out.println("Main thread interrupted.");
}

System.out.println("Producer Consumer Test Completed.");


// ==========================================
// PRACTICAL 10 - DEADLOCK PREVENTION
// ==========================================
System.out.println("\n--- Practical 10 - Safe Transfer ---");

Account safeA = new SavingsAccount("Safe A", 5000, 1000);
Account safeB = new SavingsAccount("Safe B", 5000, 1000);

Thread transfer1 = new Thread(() -> {
    try {
        for (int i = 0; i < 10; i++) {
            SafeTransfer.transferSafely(safeA, safeB, 100);
        }
    } catch (BankException e) {
        System.out.println("Transfer 1 failed: " + e.getMessage());
    }
}, "Transfer-1");

Thread transfer2 = new Thread(() -> {
    try {
        for (int i = 0; i < 10; i++) {
            SafeTransfer.transferSafely(safeB, safeA, 100);
        }
    } catch (BankException e) {
        System.out.println("Transfer 2 failed: " + e.getMessage());
    }
}, "Transfer-2");

transfer1.start();
transfer2.start();

try {
    transfer1.join();
    transfer2.join();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.out.println("Main thread interrupted.");
}

System.out.println("Account A Balance: " + safeA.getBalance());
System.out.println("Account B Balance: " + safeB.getBalance());
System.out.println("Total Balance: "
        + (safeA.getBalance() + safeB.getBalance()));
System.out.println("Safe Transfer Test Completed.");

// ==========================================
// PRACTICAL 11 - FILE HANDLING AND SERIALIZATION
// ==========================================

System.out.println("\n--- Practical 11 - Serialization ---");

Path accountFile = Paths.get("data", "accounts.dat");
Path logsFolder = Paths.get("data", "logs");
Path reportFile = Paths.get("data", "transaction_report.txt");

// Create required folders
try {
    Files.createDirectories(logsFolder);

    // Save accounts
    StatePersister.save(accounts, accountFile);

    // Load accounts
    Account[] loadedAccounts = StatePersister.load(accountFile);

    System.out.println("\n--- Loaded Accounts ---");

    for (Account account : loadedAccounts) {
        System.out.println(account);
    }

    // Create transaction log entries for demonstration
    Files.deleteIfExists(
        logsFolder.resolve("transactions.log")
);
    Path logFile = logsFolder.resolve("transactions.log");

    TransactionLog.append(
            logFile,
            "DEPOSIT " + accounts[0].getAccountNumber() + " 2000"
    );

    TransactionLog.append(
            logFile,
            "WITHDRAW " + accounts[0].getAccountNumber() + " 1000"
    );

    TransactionLog.append(
            logFile,
            "DEPOSIT " + accounts[1].getAccountNumber() + " 3000"
    );

    TransactionLog.append(
            logFile,
            "WITHDRAW " + accounts[1].getAccountNumber() + " 500"
    );

    // Generate transaction report
    System.out.println("\n--- Practical 11 - Transaction Report ---");

    ReportGenerator.generate(logsFolder, reportFile);

    System.out.println("\nReport saved at: " + reportFile);

} catch (IOException e) {
    System.out.println("Practical 11 error: " + e.getMessage());
}

        // ==========================================
        // PRACTICAL 12 - COLLECTIONS FRAMEWORK
        // ==========================================

        System.out.println("\n--- Practical 12 - Collections Framework ---");

        // Create AccountStore
        AccountStore accountStore = new AccountStore();

        // Create three test accounts
        Account[] collectionAccounts = {
                new SavingsAccount("Riya", 5000, 1000),
                new CurrentAccount("Bhakti", 2000, 500),
                new FixedDepositAccount("Kavya", 9000)
        };

        // Add accounts to HashMap
        for (Account account : collectionAccounts) {
            accountStore.add(account);
        }

        System.out.println("\n--- Accounts Added to HashMap ---");

        for (Account account : accountStore.getAll()) {
            System.out.println(account);
        }

        // Account lookup by account number
        String searchAccount =
                collectionAccounts[1].getAccountNumber();

        System.out.println("\n--- Account Lookup ---");

        Account foundAccount = accountStore.get(searchAccount);

        if (foundAccount != null) {
            System.out.println("Account Found: " + foundAccount);
        } else {
            System.out.println("Account not found.");
        }

        // Store transaction history in ArrayList
        accountStore.addTransaction("DEPOSIT 500");
        accountStore.addTransaction("WITHDRAW 200");
        accountStore.addTransaction("DEPOSIT 1000");

        System.out.println("\n--- Transaction History ---");

        for (String transaction : accountStore.getTransactionHistory()) {
            System.out.println(transaction);
        }

        // Sort accounts using Comparator
        System.out.println("\n--- Accounts Sorted by Balance ---");

        for (Account account : accountStore.sortedByBalance()) {
            System.out.println(
                    account.getOwnerName()
                    + " | Balance: Rs. "
                    + account.getBalance()
            );
        }

        // Sort accounts using Comparable
        System.out.println("\n--- Accounts Sorted by Account Number ---");

        java.util.ArrayList<Account> accountNumberSorted =
                accountStore.getAll();

        java.util.Collections.sort(accountNumberSorted);

        for (Account account : accountNumberSorted) {
            System.out.println(account.getAccountNumber());
        }

        // Practical 12 - ConcurrentHashMap
        System.out.println("\n--- ConcurrentHashMap Test ---");

        TransactionProcessor collectionProcessor =
                new TransactionProcessor();

        for (Account account : collectionAccounts) {
            collectionProcessor.addAccount(account);
        }

        Account concurrentFound =
                collectionProcessor.getAccount(searchAccount);

        System.out.println("Shared Accounts Count: "
                + collectionProcessor.getSharedAccounts().size());

        System.out.println("Concurrent Lookup: "
                + concurrentFound);

        collectionProcessor.shutdown();

        System.out.println("Practical 12 Completed.");

        // ==========================================
// PRACTICAL 13 - GENERICS AND REPOSITORY
// ==========================================

System.out.println("\n--- Practical 13 - Generics and Repository ---");

// Create Repository for Accounts
util.Repository<Account> accountRepository =
        new util.Repository<>();

// Save existing accounts
accountRepository.saveAll(collectionAccounts);

System.out.println("\n--- All Accounts in Repository ---");

for (Account account : accountRepository.findAll()) {
    System.out.println(account);
}

// Find account by ID
String repositorySearchId =
        collectionAccounts[0].getAccountNumber();

Account repositoryFound =
        accountRepository.findById(repositorySearchId);

System.out.println("\n--- Find Account by ID ---");

if (repositoryFound != null) {
    System.out.println("Account Found: " + repositoryFound);
} else {
    System.out.println("Account not found.");
}

// Display repository count
System.out.println("\nTotal Accounts: "
        + accountRepository.count());

// Test delete
String deleteId =
        collectionAccounts[1].getAccountNumber();

boolean deleted = accountRepository.delete(deleteId);

System.out.println("Account Deleted: " + deleted);
System.out.println("Accounts After Delete: "
        + accountRepository.count());

// Test bounded generic method
Integer[] numbers = {10, 20, 30, 40, 50};

double result = util.Repository.average(numbers);

System.out.println("\nAverage of Numbers: " + result);

// ==========================================
// PRACTICAL 13 - GENERIC REPOSITORY CUSTOMER TEST
// ==========================================

System.out.println("\n--- Repository<Customer> Test ---");

// Create Customer Repository
util.Repository<Customer> customerRepository =
        new util.Repository<>();

// Save customers
customerRepository.saveAll(customer1, customer2, customerWithAddress);

// Display all customers
System.out.println("\n--- All Customers ---");

for (Customer customer : customerRepository.findAll()) {
    System.out.println(
            customer.getCustomerId()
            + " | "
            + customer.getName()
    );
}

// Find customer by ID
String customerSearchId = customer1.getCustomerId();

Customer foundCustomer =
        customerRepository.findById(customerSearchId);

System.out.println("\nCustomer Found: "
        + foundCustomer.getName());

// Display count
System.out.println("Total Customers: "
        + customerRepository.count());

System.out.println("Practical 13 Completed.");

// ==========================================
// PRACTICAL 14 - BATCH TRANSACTION PROCESSING
// ==========================================

System.out.println("\n--- Practical 14 - Batch Transaction Processing ---");

// Create a separate account for batch testing
Account batchAccount =
        new SavingsAccount("BatchTest", 1000, 1000);

// Create a separate transaction processor
service.TransactionProcessor batchProcessor =
        new service.TransactionProcessor();

batchProcessor.addAccount(batchAccount);

// Create a list of transactions
java.util.List<Runnable> batchTransactions =
        new java.util.ArrayList<>();

// Wait until all transactions finish
java.util.concurrent.CountDownLatch latch =
        new java.util.concurrent.CountDownLatch(10);

// Add 10 deposit transactions
for (int i = 1; i <= 10; i++) {

    final int transactionNumber = i;

    batchTransactions.add(() -> {
        try {
            batchAccount.deposit(100);

            System.out.println(
                    Thread.currentThread().getName()
                    + " completed Transaction "
                    + transactionNumber
            );

        } catch (Exception e) {
            System.out.println(
                    "Transaction failed: " + e.getMessage()
            );

        } finally {
            latch.countDown();
        }
    });
}

// Submit all transactions
batchProcessor.submitBatch(batchTransactions);

try {
    boolean completed = latch.await(
            30,
            java.util.concurrent.TimeUnit.SECONDS
    );

    if (completed) {
        System.out.println("All batch transactions completed.");
    } else {
        System.out.println("Batch processing timed out.");
    }

} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    System.out.println("Batch processing interrupted.");
}

// Display final result
System.out.println(
        "Initial Balance: Rs. 1000"
);

System.out.println(
        "Final Balance: Rs. " + batchAccount.getBalance()
);

System.out.println(
        "Expected Balance: Rs. 2000"
);

// Stop the separate processor
batchProcessor.stop();

System.out.println("Practical 14 Batch Test Completed.");
  
        // ==========================================
        // PRACTICAL 14 - FINAL MINIBANK MENU
        // ==========================================

        boolean running = true;

        while (running) {

            System.out.println("\n=================================");
            System.out.println("          MINIBANK MENU");
            System.out.println("=================================");
            System.out.println("1. Open Account");
System.out.println("2. Deposit Money");
System.out.println("3. Withdraw Money");
System.out.println("4. Transfer Money");
System.out.println("5. Account Statement");
System.out.println("6. List All Accounts");
System.out.println("7. Generate Transaction Report");
System.out.println("8. Batch Transaction Processing");
System.out.println("9. Exit");
            System.out.println("=================================");

            System.out.print("Enter your choice: ");
            String input = scanner.nextLine();

            try {

                int choice = Integer.parseInt(input);

                switch (choice) {

                    case 1 -> {
                        System.out.print("Enter owner name: ");
                        String name = scanner.nextLine();

                        long amount = readLong(
                                scanner, "Enter opening balance: "
                        );

                        Account account =
                                bankService.openAccount(name, amount);

                        System.out.println("Account created successfully!");
                        System.out.println(account);
                    }

                    case 2 -> {
                        System.out.print("Enter account number: ");
                        String id = scanner.nextLine();

                        long amount = readLong(
                                scanner, "Enter deposit amount: "
                        );

                        bankService.deposit(id, amount);

                        System.out.println("Deposit successful!");
                        System.out.println(bankService.findAccount(id));
                    }

                    case 3 -> {
                        System.out.print("Enter account number: ");
                        String id = scanner.nextLine();

                        long amount = readLong(
                                scanner, "Enter withdrawal amount: "
                        );

                        bankService.withdraw(id, amount);

                        System.out.println("Withdrawal successful!");
                        System.out.println(bankService.findAccount(id));
                    }

                    case 4 -> {
                        System.out.print("Enter sender account number: ");
                        String from = scanner.nextLine();

                        System.out.print("Enter receiver account number: ");
                        String to = scanner.nextLine();

                        long amount = readLong(
                                scanner, "Enter transfer amount: "
                        );

                        bankService.transfer(from, to, amount);

                        System.out.println("Transfer completed!");
                    }

                    case 5 -> {
                        System.out.print("Enter account number: ");
                        String id = scanner.nextLine();

                        System.out.println(
                                bankService.statement(id)
                        );
                    }

                    case 6 -> {
                        System.out.println("\n--- All Bank Accounts ---");

                        ArrayList<Account> allAccounts =
                                bankService.listAccounts();

                        if (allAccounts.isEmpty()) {
                            System.out.println("No accounts available.");
                        } else {
                            for (Account account : allAccounts) {
                                System.out.println(account);
                            }
                        }

                        System.out.println(
                                "Total Accounts: "
                                + bankService.countAccounts()
                        );
                    }

                    case 7 -> {
    ReportGenerator.generate(logsFolder, reportFile);

    System.out.println(
            "Transaction report generated."
    );
}

case 8 -> {
    System.out.println("\n--- Batch Transaction Processing ---");

    System.out.print("Enter account number: ");
    String accountNumber = scanner.nextLine();

    // Verify account exists
    Account menuBatchAccount =
        bankService.findAccount(accountNumber);

    int numberOfTransactions = (int) readLong(
            scanner, "Enter number of transactions: "
    );

    long amount = readLong(
            scanner, "Enter deposit amount for each transaction: "
    );

    if (numberOfTransactions <= 0 || amount <= 0) {
        throw new IllegalArgumentException(
                "Number of transactions and amount must be greater than zero."
        );
    }

   ArrayList<Runnable> menuBatchTransactions = new ArrayList<>();

    for (int i = 1; i <= numberOfTransactions; i++) {
        final int transactionNumber = i;

        menuBatchTransactions.add(() -> {
            try {
                bankService.deposit(accountNumber, amount);

                System.out.println(
                        Thread.currentThread().getName()
                        + " completed Transaction "
                        + transactionNumber
                );

            } catch (Exception e) {
                throw new IllegalStateException(
                        "Transaction " + transactionNumber + " failed: " + e.getMessage(), e
                );
            }
        });
    }

    System.out.println("Submitting batch transactions...");

    bankService.processBatchTransactions(menuBatchTransactions);

    System.out.println("Batch processing finished.");

    System.out.println("Updated Account Balance:");

    System.out.println(menuBatchAccount);
}

                    case 9 -> {
                        bankService.saveAccounts();
                        bankService.shutdown();

                        running = false;

                        System.out.println(
                                "Thank you for using MiniBank!"
                        );
                    }

                    default ->
                            System.out.println(
                                    "Invalid choice. Please select 1 to 9."
                            );
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid numeric choice."
                );

            } catch (BankException e) {

                System.out.println(
                        "Banking error: " + e.getMessage()
                );

            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input: " + e.getMessage());

            } catch (IllegalStateException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }

        scanner.close();
    }

    // Read a valid long amount from the console
    private static long readLong(Scanner scanner, String message) {

        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();

            try {
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }
}

