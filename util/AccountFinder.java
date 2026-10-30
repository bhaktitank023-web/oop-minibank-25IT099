
package util;

import model.Account;
import exception.AccountNotFoundException;

public class AccountFinder {

    public static Account findByNumber(
            Account[] accounts,
            String accountNumber
    ) throws AccountNotFoundException {

        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        throw new AccountNotFoundException(accountNumber);
    }
}
