import java.sql.*;

public class DBHelper {
    private static final String DB_URL = "jdbc:sqlite:finance.db";
    
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            e.printStackTrace();
        }
    }
    
    public DBHelper() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "date TEXT," +
                    "category TEXT," +
                    "amount REAL," +
                    "type TEXT)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void addTransaction(String date, String category, double amount, String type) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO transactions (date, category, amount, type) VALUES (?, ?, ?, ?)");
            ps.setString(1, date);
            ps.setString(2, category);
            ps.setDouble(3, amount);
            ps.setString(4, type);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ResultSet getMonthlySummary(String month) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement ps = conn.prepareStatement(
            "SELECT type, SUM(amount) as total FROM transactions WHERE strftime('%Y-%m', date) = ? GROUP BY type");
        ps.setString(1, month);
        return ps.executeQuery();
    }
    
    public ResultSet getAllTransactions() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM transactions ORDER BY date DESC");
    }
    
    public ResultSet getCategoryExpenses(String month) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        PreparedStatement ps = conn.prepareStatement(
            "SELECT category, SUM(amount) as total FROM transactions " +
            "WHERE strftime('%Y-%m', date) = ? AND type = 'expense' " +
            "GROUP BY category ORDER BY total DESC");
        ps.setString(1, month);
        return ps.executeQuery();
    }
    
    public double getAverageMonthlyExpense() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(
            "SELECT AVG(monthly_total) as avg_expense FROM (" +
            "SELECT strftime('%Y-%m', date) as month, SUM(amount) as monthly_total " +
            "FROM transactions WHERE type = 'expense' GROUP BY month)");
        
        if (rs.next()) {
            return rs.getDouble("avg_expense");
        }
        return 0.0;
    }
}
