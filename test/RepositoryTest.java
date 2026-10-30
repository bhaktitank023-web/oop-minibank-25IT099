import model.Account;
import model.Customer;
import model.SavingsAccount;
import org.junit.jupiter.api.Test;
import util.Repository;

import static org.junit.jupiter.api.Assertions.*;

class RepositoryTest {
    @Test
    void repositoryStoresFindsAndDeletesAccountsByAnnotatedId() {
        Repository<Account> repository = new Repository<>();
        Account account = new SavingsAccount("Riya", 2500, 500);
        repository.save(account);
        assertSame(account, repository.findById(account.getAccountNumber()));
        assertEquals(1, repository.count());
        assertTrue(repository.delete(account.getAccountNumber()));
        assertEquals(0, repository.count());
    }

    @Test
    void sameGenericRepositoryWorksForCustomers() {
        Repository<Customer> repository = new Repository<>();
        Customer customer = new Customer("Bhakti", "b@example.com", "9876543210");
        repository.save(customer);
        assertSame(customer, repository.findById(customer.getCustomerId()));
    }

    @Test
    void boundedAverageCalculatesCorrectly() {
        assertEquals(25.0, Repository.average(new Integer[]{10, 20, 30, 40}));
    }
}
