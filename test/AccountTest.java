import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import service.SafeTransfer;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {
    @Test
    void depositAndWithdrawUpdateBalance() throws Exception {
        Account account = new SavingsAccount("Test User", 2000, 500);
        account.deposit(500);
        account.withdraw(300);
        assertEquals(2200.0, account.getBalance());
    }

    @Test
    void depositRejectsZeroOrNegativeAmount() {
        Account account = new SavingsAccount("Test User", 2000, 500);
        assertThrows(InvalidAmountException.class, () -> account.deposit(0));
        assertThrows(InvalidAmountException.class, () -> account.deposit(-10));
    }

    @Test
    void withdrawalRejectsInsufficientFundsAndMinimumBalanceViolation() {
        Account account = new SavingsAccount("Test User", 2000, 1000);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(1500));
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(1001));
        assertEquals(2000.0, account.getBalance());
    }

    @Test
    void safeTransferMovesMoneyBetweenAccounts() throws Exception {
        Account sender = new SavingsAccount("Sender", 3000, 500);
        Account receiver = new SavingsAccount("Receiver", 1000, 500);
        SafeTransfer.transferSafely(sender, receiver, 1000);
        assertEquals(2000.0, sender.getBalance());
        assertEquals(2000.0, receiver.getBalance());
    }
}
