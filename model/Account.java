package model;

import java.io.Serializable;
import java.util.Objects;
import model.annotation.Id;
import model.annotation.Positive;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;

public abstract class Account implements Transactable, InterestBearing, Serializable, Comparable<Account> {

    private static final long serialVersionUID = 1L;
    @Id
    private final String accountNumber;
    private String ownerName;
    @Positive
    private long balance;
    private boolean active;

    private static long accountCounter = 0;

    private static synchronized String generateAccountNumber() {
        accountCounter++;
        return String.format("AC%04d", accountCounter);
    }

    /** Keeps generated account numbers unique after loading serialized accounts. */
    public static synchronized void synchronizeAccountCounter(String existingAccountNumber) {
        if (existingAccountNumber == null || !existingAccountNumber.matches("AC\\d+")) {
            return;
        }
        try {
            long existingNumber = Long.parseLong(existingAccountNumber.substring(2));
            accountCounter = Math.max(accountCounter, existingNumber);
        } catch (NumberFormatException ignored) {
            // Ignore account IDs whose numeric suffix is outside the long range.
        }
    }


    // ==========================================
    // CONSTRUCTORS
    // ==========================================

    public Account(String ownerName, long openingBalance) {

        this.accountNumber = generateAccountNumber();
        this.ownerName = ownerName;
        this.balance = openingBalance;
        this.active = true;
    }


    // Constructor chaining
    public Account(String ownerName) {

        this(ownerName, 0);
    }


    // Constructor used for equality testing
    Account(
            String accountNumber,
            String ownerName,
            long balance
    ) {

        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
        this.active = true;
    }


    // ==========================================
    // PRACTICAL 9 - SYNCHRONIZED TRANSACTIONS
    // ==========================================

    @Override
    public synchronized void deposit(long amount)
            throws InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Deposit amount must be greater than zero."
            );
        }

        balance += amount;
    }

    @Override
    public synchronized boolean withdraw(long amount)
            throws InsufficientFundsException, InvalidAmountException {

        if (amount <= 0) {
            throw new InvalidAmountException(
                    "Withdrawal amount must be greater than zero."
            );
        }

        if (balance < amount) {
            long shortfall = amount - balance;
            throw new InsufficientFundsException(shortfall);
        }

        if (!canWithdraw(amount)) {
            throw new InsufficientFundsException(amount);
        }

        balance -= amount;
        return true;
    }



    // ==========================================
    // PRACTICAL 5 - ABSTRACT METHODS
    // ==========================================

    @Override
    public abstract double interestRate();


    public abstract boolean canWithdraw(long amount);


    // ==========================================
    // PRACTICAL 6 - YEARLY INTEREST
    // ==========================================

    @Override
    public double getBalance() {
        return balance;
    }


    // ==========================================
    // GETTERS
    // ==========================================

    public String getAccountNumber() {

        return accountNumber;
    }


    public String getOwnerName() {

        return ownerName;
    }


    public boolean isActive() {

        return active;
    }


    // ==========================================
    // PRACTICAL 3 - toString()
    // ==========================================

    @Override
    public String toString() {

        return accountNumber
                + " | "
                + ownerName
                + " | "
                + balance;
    }


    // ==========================================
    // PRACTICAL 3 - equals()
    // ==========================================

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account other = (Account) o;

        return accountNumber.equals(
                other.accountNumber
        );
    }


    // ==========================================
    // PRACTICAL 3 - hashCode()
    // ==========================================

    @Override
    public int hashCode() {

        return Objects.hash(accountNumber);
    }

    
public void transfer(Account to, long amount) throws BankException {
        if (to == null) {
            throw new BankException("Destination account is missing.");
        }
        if (to == this) {
            throw new BankException("Cannot transfer to the same account.");
        }

        Account first = this.accountNumber.compareTo(to.accountNumber) < 0 ? this : to;
        Account second = first == this ? to : this;

        // A consistent lock order prevents deadlocks for opposite-direction transfers.
        synchronized (first) {
            synchronized (second) {
                withdraw(amount);
                to.deposit(amount);
            }
        }
    }

@Override
public int compareTo(Account other) {
    return this.accountNumber.compareTo(other.accountNumber);
}
}