
package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import model.Account;

public class StatePersister {

    // Save account array
    public static void save(Account[] accounts, Path file) {
        try {
            Path parent = file.getParent();
            if (parent != null) Files.createDirectories(parent);

            try (ObjectOutputStream out =
                     new ObjectOutputStream(
                         new FileOutputStream(file.toFile()))) {

                out.writeObject(accounts);
                System.out.println("Accounts saved successfully.");
            }

        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }

    // Load account array
    public static Account[] load(Path file) {
        if (!Files.exists(file)) {
            return new Account[0];
        }

        try (ObjectInputStream in =
                 new ObjectInputStream(
                     new FileInputStream(file.toFile()))) {

            return (Account[]) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading accounts: " + e.getMessage());
            return new Account[0];
        }
    }

    // Save account list
    public static void saveList(ArrayList<Account> accounts, Path file) {
        save(accounts.toArray(new Account[0]), file);
    }

    // Load accounts into a list
    public static ArrayList<Account> loadList(Path file) {
        Account[] accounts = load(file);
        ArrayList<Account> result = new ArrayList<>();

        for (Account account : accounts) {
            result.add(account);
        }

        return result;
    }
}
