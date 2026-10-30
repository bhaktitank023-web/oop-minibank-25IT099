package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;

public class ReportGenerator {

    public static void generate(Path logsFolder, Path reportFile) {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        try (DirectoryStream<Path> stream =
                 Files.newDirectoryStream(logsFolder)) {

            for (Path file : stream) {

                if (!Files.isRegularFile(file)
                        || !file.getFileName().toString().endsWith(".log")) {
                    continue;
                }

                BasicFileAttributes attributes =
                    Files.readAttributes(file, BasicFileAttributes.class);

                System.out.println("\nLog File: " + file.getFileName());
                System.out.println("Size: " + attributes.size() + " bytes");
                System.out.println("Last Modified: "
                                   + attributes.lastModifiedTime());

                try (BufferedReader reader =
                         Files.newBufferedReader(file)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        String[] parts = line.trim().split("\\s+");

                        if (parts.length != 3) {
                            continue;
                        }

                        try {
                            long amount = Long.parseLong(parts[2]);

                            if (parts[0].equalsIgnoreCase("DEPOSIT")) {
                                totalDeposits += amount;
                            } else if (parts[0].equalsIgnoreCase("WITHDRAW")) {
                                totalWithdrawals += amount;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Invalid amount: " + line);
                        }
                    }
                }
            }

            long netChange = totalDeposits - totalWithdrawals;

            try (BufferedWriter writer = Files.newBufferedWriter(
                    reportFile,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                writer.write("MINIBANK TRANSACTION REPORT");
                writer.newLine();
                writer.write("---------------------------");
                writer.newLine();
                writer.write("Total Deposits: " + totalDeposits);
                writer.newLine();
                writer.write("Total Withdrawals: " + totalWithdrawals);
                writer.newLine();
                writer.write("Net Change: " + netChange);
                writer.newLine();
            }

            System.out.println("\nReport generated successfully.");
            System.out.println("Total Deposits: " + totalDeposits);
            System.out.println("Total Withdrawals: " + totalWithdrawals);
            System.out.println("Net Change: " + netChange);

        } catch (IOException e) {
            System.out.println("Report error: " + e.getMessage());
        }
    }
}
