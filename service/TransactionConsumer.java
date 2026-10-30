
package service;

public class TransactionConsumer implements Runnable {

    private final TransactionBuffer buffer;

    public TransactionConsumer(TransactionBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= 5; i++) {
                buffer.consume();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Consumer interrupted.");
        }
    }
}
