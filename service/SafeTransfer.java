package service;

import model.Account;
import exception.BankException;

/** Performs transfers while acquiring account locks in a stable order. */
public final class SafeTransfer {
    private SafeTransfer() { }

    public static void transferSafely(Account from, Account to, long amount)
            throws BankException {
        if (from == null || to == null) {
            throw new BankException("Account cannot be null.");
        }
        if (from == to) {
            throw new BankException("Cannot transfer to the same account.");
        }

        Account first = from.getAccountNumber().compareTo(to.getAccountNumber()) < 0
                ? from : to;
        Account second = first == from ? to : from;

        synchronized (first) {
            synchronized (second) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        }
    }
}
