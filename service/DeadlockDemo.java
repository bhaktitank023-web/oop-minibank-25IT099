
package service;

public class DeadlockDemo {

    private final Object accountA = new Object();
    private final Object accountB = new Object();

    public void startDeadlock() {

        Thread thread1 = new Thread(() -> {

            synchronized (accountA) {
                System.out.println("Thread 1 locked Account A");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                System.out.println("Thread 1 waiting for Account B");

                synchronized (accountB) {
                    System.out.println("Thread 1 locked Account B");
                }
            }
        });

        Thread thread2 = new Thread(() -> {

            synchronized (accountB) {
                System.out.println("Thread 2 locked Account B");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }

                System.out.println("Thread 2 waiting for Account A");

                synchronized (accountA) {
                    System.out.println("Thread 2 locked Account A");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}

