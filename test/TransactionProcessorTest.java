import model.Account;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import service.TransactionProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionProcessorTest {
    @Test
    void batchTransactionsCompleteWithExpectedBalance() throws Exception {
        Account account = new SavingsAccount("Batch User", 1000, 0);
        TransactionProcessor processor = new TransactionProcessor();
        CountDownLatch latch = new CountDownLatch(100);
        List<Runnable> transactions = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            transactions.add(() -> {
                try { account.deposit(10); }
                catch (Exception e) { throw new RuntimeException(e); }
                finally { latch.countDown(); }
            });
        }
        processor.submitBatch(transactions);
        boolean completed = latch.await(10, java.util.concurrent.TimeUnit.SECONDS);
        processor.stop();
        assertEquals(true, completed);
        assertEquals(2000.0, account.getBalance());
    }
}
