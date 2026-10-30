
package service;

import java.util.LinkedList;
import java.util.Queue;

public class TransactionBuffer {

    private final Queue<String> transactions = new LinkedList<>();
    private final int capacity;

    public TransactionBuffer(int capacity) {
        this.capacity = capacity;
    }

    // Producer adds transactions
    public synchronized void produce(String transaction)
            throws InterruptedException {

        while (transactions.size() == capacity) {
            wait();
        }

        transactions.add(transaction);

        System.out.println("Produced: " + transaction);

        notify();
    }

    // Consumer removes transactions
    public synchronized String consume()
            throws InterruptedException {

        while (transactions.isEmpty()) {
            wait();
        }

        String transaction = transactions.remove();

        System.out.println("Consumed: " + transaction);

        notify();

        return transaction;
    }
}

