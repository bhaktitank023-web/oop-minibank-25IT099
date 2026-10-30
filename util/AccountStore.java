
package util;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Comparator;

import model.Account;

public class AccountStore {

    // Store accounts using account number as key
    private HashMap<String, Account> accounts = new HashMap<>();

    // Store transaction history
    private ArrayList<String> transactionHistory = new ArrayList<>();

    // Add account
    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
    }

    // Find account by account number
    public Account get(String accountNumber) {
        return accounts.get(accountNumber);
    }

    // Get all accounts
    public ArrayList<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    // Add transaction to history
    public void addTransaction(String transaction) {
        transactionHistory.add(transaction);
    }

    // Get transaction history
    public ArrayList<String> getTransactionHistory() {
        return new ArrayList<>(transactionHistory);
    }

    // Sort accounts by balance (highest first)
    public ArrayList<Account> sortedByBalance() {

        ArrayList<Account> sortedAccounts = getAll();

        sortedAccounts.sort(
            Comparator.comparingDouble(Account::getBalance).reversed()
        );

        return sortedAccounts;
    }
}
