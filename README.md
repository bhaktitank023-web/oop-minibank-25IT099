# MiniBank — Java Console Banking Application

MiniBank is an educational, menu-driven Java banking application developed for the **CHARUSAT CEUE203 Object-Oriented Programming practicals**.

The project combines concepts from all 14 practicals into one application, demonstrating object-oriented programming, exception handling, annotations, reflection, collections, serialization, file handling, multithreading, generics, and batch transaction processing.

> **Educational project only:** MiniBank is a local console simulation. It is not connected to a real bank, UPI, payment network, or production banking infrastructure. Do not enter real personal or financial information.

## Features

- Open savings accounts and view account details.
- Deposit and withdraw money.
- Transfer funds between accounts.
- Generate account statements.
- View transaction history and reports.
- Save and reload account data between application runs.
- Generic repository using the `@Id` annotation and reflection.
- Custom exceptions and annotation-based validation.
- Collections framework and account sorting.
- Thread-safe account operations.
- Four-thread transaction pool and batch deposit processing.
- Serialization and file handling.
- JUnit 5 unit tests.

## Technologies Used

| Technology | Purpose |
|---|---|
| Java 17+ | Application development |
| Apache Maven | Build and dependency management |
| JUnit 5 | Unit testing |
| Collections Framework | Account and transaction storage |
| Multithreading | Concurrent transaction processing |
| Serialization | Local account persistence |
| Reflection and Annotations | Generic repository and validation |

## Requirements

- JDK 17 or newer
- Apache Maven 3.9 or newer
- Windows, Linux, or macOS terminal

Verify your installations:

```bash
java -version
javac -version
mvn -version
```

## Project Setup

Clone the repository:

```bash
git clone https://github.com/bhaktitank023-web/oop-minibank-25IT099.git
```

Open the project folder:

```bash
cd oop-minibank-25IT099
```

## Build and Run with Maven

Run the unit tests:

```bash
mvn test
```

Build the application:

```bash
mvn package
```

Run the generated JAR:

```bash
java -jar target/minibank-1.0.0.jar
```

The application creates local account and transaction data when it runs.

## Run with the Included JAR

If the repository includes a root-level `minibank.jar`, run:

```bash
java -jar minibank.jar
```

The included JAR should be rebuilt whenever the source code changes.

## Console Menu

The application provides these options:

1. Open Account
2. Deposit Money
3. Withdraw Money
4. Transfer Money
5. Account Statement
6. List All Accounts
7. Generate Transaction Report
8. Batch Transaction Processing
9. Exit

## Project Structure

```text
MINIBANK/
│
├── model/
│   ├── Account.java
│   ├── Customer.java
│   ├── SavingsAccount.java
│   ├── CurrentAccount.java
│   ├── FixedDepositAccount.java
│   └── annotation/
│
├── service/
│   ├── BankService.java
│   ├── TransactionProcessor.java
│   ├── AccountWorker.java
│   ├── SafeTransfer.java
│   ├── TransactionBuffer.java
│   ├── TransactionProducer.java
│   └── TransactionConsumer.java
│
├── util/
│   ├── Repository.java
│   ├── AccountStore.java
│   ├── StatePersister.java
│   ├── TransactionLog.java
│   ├── ReportGenerator.java
│   └── Validator.java
│
├── exception/
│   ├── BankException.java
│   ├── AccountNotFoundException.java
│   ├── InsufficientFundsException.java
│   └── InvalidAmountException.java
│
├── test/
│   └── JUnit test classes
│
├── data/                 # Runtime-generated local data
├── MiniBank.java         # Application entry point
├── BankInfo.java
├── MenuOption.java
├── pom.xml
├── .gitignore
├── README.md
└── minibank.jar          # Optional runnable JAR
```

## Testing

Run all configured JUnit tests:

```bash
mvn test
```

The project includes tests for account operations, repository functionality, and transaction processing.

Maven displays the test summary and build result in the terminal.

## Practical Coverage

The project integrates the concepts covered in the 14 OOP practicals:

- Practicals 1–4: Java fundamentals, classes, objects, validation and commands.
- Practicals 5–6: Inheritance, abstraction, interfaces and polymorphism.
- Practical 7: Annotations and reflection.
- Practical 8: Exception handling.
- Practicals 9–10: Multithreading, synchronization and transaction processing.
- Practical 11: Serialization, file handling and reporting.
- Practical 12: Collections Framework.
- Practical 13: Generics and Repository pattern.
- Practical 14: Banking service integration, persistence and batch processing.

## Data and Privacy

MiniBank stores account and transaction information locally in its `data/` directory.

Generated local account files, logs, and reports are excluded from GitHub using `.gitignore`.

Do not commit real or sensitive financial information.

## GitHub Contribution and Upload

The repository should include:

- Java source files
- JUnit test files
- `pom.xml`
- `.gitignore`
- `README.md`

Compiled `.class` files, Maven build folders, and generated local banking data should not be committed.

The runnable JAR may be included as an optional release artifact.

## Project Purpose

MiniBank was developed as an academic project to demonstrate the practical application of Java Object-Oriented Programming concepts in an integrated console-based banking simulation.

**Developed for:** CHARUSAT — CEUE203 Object-Oriented Programming Practicals

**Project:** MiniBank — Java Console Banking Application
