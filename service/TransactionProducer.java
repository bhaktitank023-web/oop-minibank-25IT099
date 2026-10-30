
package service;

public class TransactionProducer implements Runnable {

    private final TransactionBuffer buffer;

    public TransactionProducer(TransactionBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.produce("Transaction-" + i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Producer interrupted.");
        }
    }
}
