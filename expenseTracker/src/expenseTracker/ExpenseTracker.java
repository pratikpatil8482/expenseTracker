package expenseTracker;

import java.io.*;
import java.util.*;

class Transaction {
    String type;
    String category;
    double amount;
    String month;

    public Transaction(String type, String category, double amount, String month) {
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.month = month;
    }
}

public class ExpenseTracker {
    static List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Expense Tracker ---");
            System.out.println("1. Add Transaction");
            System.out.println("2. Load from File");
            System.out.println("3. Show Monthly Summary");
            System.out.println("4. View All Transactions");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> addTransaction(sc);
                case 2 -> {
                    System.out.print("Enter file path: ");
                    loadFromFile(sc.nextLine());
                }
                case 3 -> showMonthlySummary();
                case 4 -> viewAllTransactions();
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    static void addTransaction(Scanner sc) {
        System.out.print("Enter month (e.g., May): ");
        String month = sc.nextLine();

        System.out.print("Type (income/expense): ");
        String type = sc.nextLine().toLowerCase();

        System.out.print("Category: ");
        String category = sc.nextLine();

        System.out.print("Amount: ");
        double amount = Double.parseDouble(sc.nextLine());

        Transaction t = new Transaction(type, category, amount, month);
        transactions.add(t);

        try (FileWriter fw = new FileWriter("transactions.txt", true)) {
            fw.write(type + "," + category + "," + amount + "," + month + "\n");
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }

        System.out.println("Transaction added and saved.");
    }

    static void loadFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 4) continue;

                String type = parts[0];
                String category = parts[1];
                double amount = Double.parseDouble(parts[2]);
                String month = parts[3];

                Transaction t = new Transaction(type, category, amount, month);
                transactions.add(t);
            }

            System.out.println("Data loaded from file.");
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }

    static void showMonthlySummary() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }

        Map<String, Double> incomeMap = new HashMap<>();
        Map<String, Double> expenseMap = new HashMap<>();

        for (Transaction t : transactions) {
            if (t.type.equals("income")) {
                incomeMap.put(t.month, incomeMap.getOrDefault(t.month, 0.0) + t.amount);
            } else {
                expenseMap.put(t.month, expenseMap.getOrDefault(t.month, 0.0) + t.amount);
            }
        }

        Set<String> allMonths = new HashSet<>();
        allMonths.addAll(incomeMap.keySet());
        allMonths.addAll(expenseMap.keySet());

        for (String month : allMonths) {
            double income = incomeMap.getOrDefault(month, 0.0);
            double expense = expenseMap.getOrDefault(month, 0.0);
            System.out.println(month + " - Income: ₹" + income + " | Expense: ₹" + expense);
        }
    }

    static void viewAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No transactions available.");
            return;
        }

        System.out.println("\n--- All Transactions ---");
        for (Transaction t : transactions) {
            System.out.println(t.type.toUpperCase() + " | " + t.category + " | ₹" + t.amount + " | " + t.month);
        }
    }
}
