import java.awt.*;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import javax.swing.*;

public class FinanceTrackerGUI extends JFrame {
    private JTextField amountField, categoryField, dateField;
    private JComboBox<String> typeBox;
    private JButton addButton, predictButton, showButton, tipsButton;
    private JLabel resultLabel;
    private JTextArea historyArea, tipsArea;
    private DBHelper db;
    private DecimalFormat currencyFormat;
    
    public FinanceTrackerGUI() {
        db = new DBHelper();
        currencyFormat = new DecimalFormat("#,##0.00");
        
        setTitle("Personal Finance Tracker");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Transaction"));
        
        amountField = new JTextField();
        categoryField = new JTextField();
        dateField = new JTextField(LocalDate.now().toString());
        typeBox = new JComboBox<>(new String[]{"income", "expense"});
        addButton = new JButton("Add Transaction");
        predictButton = new JButton("Predict Savings");
        showButton = new JButton("Show History");
        tipsButton = new JButton("Budget Tips");
        
        inputPanel.add(new JLabel("Amount (₹):"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeBox);
        inputPanel.add(addButton);
        inputPanel.add(predictButton);
        inputPanel.add(showButton);
        inputPanel.add(tipsButton);
        
        // Result Panel
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("Prediction & Analysis"));
        resultLabel = new JLabel("Prediction will appear here.", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        
        // History Panel
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        historyArea = new JTextArea(8, 40);
        historyArea.setEditable(false);
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        
        // Tips Panel
        JPanel tipsPanel = new JPanel(new BorderLayout());
        tipsPanel.setBorder(BorderFactory.createTitledBorder("Budget Tips & Analysis"));
        tipsArea = new JTextArea(6, 40);
        tipsArea.setEditable(false);
        tipsArea.setFont(new Font("Arial", Font.PLAIN, 12));
        tipsArea.setBackground(new Color(255, 255, 240)); // Light yellow background
        tipsPanel.add(new JScrollPane(tipsArea), BorderLayout.CENTER);
        
        // Layout
        add(inputPanel, BorderLayout.NORTH);
        add(resultPanel, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        bottomPanel.add(historyPanel);
        bottomPanel.add(tipsPanel);
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Event Listeners
        addButton.addActionListener(e -> addTransaction());
        predictButton.addActionListener(e -> predictSavings());
        showButton.addActionListener(e -> showHistory());
        tipsButton.addActionListener(e -> showBudgetTips());
        
        // Auto-show history on startup
        showHistory();
    }
    
    private void addTransaction() {
        String date = dateField.getText().trim();
        String category = autoCategorize(categoryField.getText().trim());
        double amount;
        
        // Validate inputs
        if (date.isEmpty() || category.isEmpty()) {
            showMessage("Please fill in all fields!", Color.RED);
            return;
        }
        
        try {
            amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                showMessage("Amount must be positive!", Color.RED);
                return;
            }
        } catch (NumberFormatException ex) {
            showMessage("Invalid amount format!", Color.RED);
            return;
        }
        
        String type = (String) typeBox.getSelectedItem();
        db.addTransaction(date, category, amount, type);
        
        // Clear fields after successful addition
        amountField.setText("");
        categoryField.setText("");
        dateField.setText(LocalDate.now().toString());
        
        showMessage("Transaction added successfully!", Color.GREEN);
        showHistory(); // Refresh history
    }
    
    private String autoCategorize(String input) {
        input = input.toLowerCase();
        if (input.contains("food") || input.contains("restaurant") || input.contains("meal") || input.contains("grocery")) return "Food";
        if (input.contains("rent") || input.contains("lease") || input.contains("housing")) return "Housing";
        if (input.contains("bus") || input.contains("taxi") || input.contains("train") || input.contains("fuel") || input.contains("transport")) return "Transport";
        if (input.contains("medical") || input.contains("doctor") || input.contains("medicine") || input.contains("health")) return "Healthcare";
        if (input.contains("movie") || input.contains("game") || input.contains("entertainment") || input.contains("fun")) return "Entertainment";
        if (input.contains("cloth") || input.contains("shopping") || input.contains("dress")) return "Shopping";
        if (input.contains("salary") || input.contains("wage") || input.contains("pay")) return "Salary";
        if (input.contains("investment") || input.contains("dividend") || input.contains("interest")) return "Investment";
        
        // Capitalize first letter
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
    
    private void predictSavings() {
        String month = LocalDate.now().toString().substring(0, 7); // YYYY-MM
        double income = 0, expense = 0;
        
        try (ResultSet rs = db.getMonthlySummary(month)) {
            while (rs.next()) {
                if ("income".equals(rs.getString("type"))) {
                    income = rs.getDouble("total");
                } else if ("expense".equals(rs.getString("type"))) {
                    expense = rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            showMessage("Error calculating prediction!", Color.RED);
            return;
        }
        
        double predictedSavings = income - expense;
        String formattedSavings = currencyFormat.format(Math.abs(predictedSavings));
        
        if (predictedSavings < 0) {
            resultLabel.setForeground(Color.RED);
            resultLabel.setText("<html><center>⚠️ WARNING! You're overspending<br/>Deficit: ₹" + formattedSavings + "</center></html>");
        } else if (predictedSavings == 0) {
            resultLabel.setForeground(Color.ORANGE);
            resultLabel.setText("<html><center>⚖️ Breaking Even<br/>No savings this month</center></html>");
        } else {
            resultLabel.setForeground(new Color(0, 128, 0)); // Dark green
            resultLabel.setText("<html><center>✅ Great! You're saving<br/>Predicted savings: ₹" + formattedSavings + "</center></html>");
        }
        
        // Show additional analysis
        showBudgetAnalysis(income, expense, month);
    }
    
    private void showBudgetAnalysis(double income, double expense, String month) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== MONTHLY ANALYSIS (").append(month).append(") ===\n");
        analysis.append("Income: ₹").append(currencyFormat.format(income)).append("\n");
        analysis.append("Expenses: ₹").append(currencyFormat.format(expense)).append("\n");
        analysis.append("Savings Rate: ").append(income > 0 ? String.format("%.1f%%", ((income - expense) / income) * 100) : "N/A").append("\n\n");
        
        // Category breakdown
        try (ResultSet rs = db.getCategoryExpenses(month)) {
            analysis.append("TOP EXPENSE CATEGORIES:\n");
            int count = 0;
            while (rs.next() && count < 5) {
                String category = rs.getString("category");
                double amount = rs.getDouble("total");
                double percentage = expense > 0 ? (amount / expense) * 100 : 0;
                analysis.append(String.format("• %s: ₹%s (%.1f%%)\n", 
                    category, currencyFormat.format(amount), percentage));
                count++;
            }
        } catch (SQLException e) {
            analysis.append("Error loading category analysis.\n");
        }
        
        tipsArea.setText(analysis.toString());
    }
    
    private void showBudgetTips() {
        StringBuilder tips = new StringBuilder();
        tips.append("=== BUDGET TIPS & RECOMMENDATIONS ===\n\n");
        
        String currentMonth = LocalDate.now().toString().substring(0, 7);
        double income = 0, expense = 0;
        
        try (ResultSet rs = db.getMonthlySummary(currentMonth)) {
            while (rs.next()) {
                if ("income".equals(rs.getString("type"))) {
                    income = rs.getDouble("total");
                } else if ("expense".equals(rs.getString("type"))) {
                    expense = rs.getDouble("total");
                }
            }
        } catch (SQLException e) {
            tips.append("Error loading financial data for tips.\n");
            tipsArea.setText(tips.toString());
            return;
        }
        
        // General tips based on current situation
        if (income - expense < 0) {
            tips.append("🚨 URGENT ACTIONS NEEDED:\n");
            tips.append("• Review and cut non-essential expenses immediately\n");
            tips.append("• Look for additional income sources\n");
            tips.append("• Create a strict budget and stick to it\n\n");
        } else if ((income - expense) / income < 0.2) {
            tips.append("⚠️ LOW SAVINGS RATE:\n");
            tips.append("• Aim to save at least 20% of your income\n");
            tips.append("• Track daily expenses more carefully\n");
            tips.append("• Consider the 50/30/20 rule (needs/wants/savings)\n\n");
        } else {
            tips.append("✅ GOOD FINANCIAL HEALTH:\n");
            tips.append("• Great job maintaining positive savings!\n");
            tips.append("• Consider investing your surplus\n");
            tips.append("• Build an emergency fund (6 months expenses)\n\n");
        }
        
        // Category-specific tips
        try (ResultSet rs = db.getCategoryExpenses(currentMonth)) {
            tips.append("CATEGORY-SPECIFIC TIPS:\n");
            while (rs.next()) {
                String category = rs.getString("category").toLowerCase();
                double amount = rs.getDouble("total");
                double percentage = expense > 0 ? (amount / expense) * 100 : 0;
                
                if (percentage > 30 && category.contains("food")) {
                    tips.append("• Food expenses are high (").append(String.format("%.1f%%", percentage))
                        .append(") - try meal planning and cooking at home\n");
                } else if (percentage > 40 && category.contains("housing")) {
                    tips.append("• Housing costs are high (").append(String.format("%.1f%%", percentage))
                        .append(") - consider if this is sustainable\n");
                } else if (percentage > 15 && category.contains("entertainment")) {
                    tips.append("• Entertainment spending is high (").append(String.format("%.1f%%", percentage))
                        .append(") - look for free/cheaper alternatives\n");
                }
            }
        } catch (SQLException e) {
            tips.append("Error loading category tips.\n");
        }
        
        tips.append("\nGENERAL MONEY-SAVING TIPS:\n");
        tips.append("• Use the 24-hour rule for non-essential purchases\n");
        tips.append("• Automate your savings\n");
        tips.append("• Review subscriptions monthly\n");
        tips.append("• Compare prices before major purchases\n");
        tips.append("• Set specific financial goals\n");
        
        tipsArea.setText(tips.toString());
    }
    
    private void showHistory() {
        historyArea.setText("");
        StringBuilder history = new StringBuilder();
        history.append(String.format("%-12s | %-15s | %10s | %-10s\n", "DATE", "CATEGORY", "AMOUNT", "TYPE"));
        history.append("─".repeat(60)).append("\n");
        
        try (ResultSet rs = db.getAllTransactions()) {
            while (rs.next()) {
                history.append(String.format("%-12s | %-15s | %10s | %-10s\n",
                        rs.getString("date"),
                        rs.getString("category"),
                        "₹" + currencyFormat.format(rs.getDouble("amount")),
                        rs.getString("type").toUpperCase()));
            }
        } catch (SQLException e) {
            history.append("Error loading transaction history!");
        }
        
        historyArea.setText(history.toString());
    }
    
    private void showMessage(String message, Color color) {
        resultLabel.setForeground(color);
        resultLabel.setText(message);
        
        // Auto-clear message after 3 seconds
        Timer timer = new Timer(3000, e -> {
            if (resultLabel.getText().equals(message)) {
                resultLabel.setText("Ready for next transaction...");
                resultLabel.setForeground(Color.BLACK);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FinanceTrackerGUI().setVisible(true);
        });
    }
}
