package hpms.test;

import hpms.util.DBConnection;
import java.sql.*;

/**
 * Test database connection and basic operations
 * Run this after setting up the database to verify everything works
 */
public class DatabaseConnectionTest {

    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("HPMS Database Connection Test");
        System.out.println("=================================\n");

        // Test 1: Connection
        System.out.println("Test 1: Testing database connection...");
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println("✓ Database connection successful!");
            try {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("  - Database: " + meta.getDatabaseProductName());
                System.out.println("  - Version: " + meta.getDatabaseProductVersion());
                System.out.println("  - URL: " + meta.getURL());
            } catch (SQLException e) {
                System.out.println("✗ Could not get database metadata");
                e.printStackTrace();
            }
        } else {
            System.out.println("✗ Database connection failed!");
            System.out.println("\nPlease check:");
            System.out.println("1. MySQL is running (XAMPP Control Panel)");
            System.out.println("2. Database 'hpms_db' exists (run setup_database.bat)");
            System.out.println("3. MySQL username/password in DBConnection.java");
            return;
        }

        System.out.println();

        // Test 2: List tables
        System.out.println("Test 2: Checking database tables...");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            int tableCount = 0;
            System.out.println("Tables found:");
            while (rs.next()) {
                tableCount++;
                System.out.println("  - " + rs.getString(1));
            }
            if (tableCount > 0) {
                System.out.println("✓ Found " + tableCount + " tables");
            } else {
                System.out.println("✗ No tables found! Run setup_database.bat");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error checking tables");
            e.printStackTrace();
        }

        System.out.println();

        // Test 3: Check users table
        System.out.println("Test 3: Checking users table...");
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next()) {
                int userCount = rs.getInt(1);
                System.out.println("✓ Users table accessible (" + userCount + " users)");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error accessing users table");
            e.printStackTrace();
        }

        System.out.println();

        // Test 4: Insert test data
        System.out.println("Test 4: Testing insert operation...");
        try {
            // Check if admin exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT username FROM users WHERE username = ?");
            checkStmt.setString(1, "test_user");
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                System.out.println("  Test user already exists, cleaning up...");
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM users WHERE username = ?");
                deleteStmt.setString(1, "test_user");
                deleteStmt.executeUpdate();
                deleteStmt.close();
            }
            rs.close();
            checkStmt.close();

            // Insert test user
            PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)");
            insertStmt.setString(1, "test_user");
            insertStmt.setString(2, "test_hash");
            insertStmt.setString(3, "test_salt");
            insertStmt.setString(4, "STAFF");
            int rows = insertStmt.executeUpdate();
            insertStmt.close();

            if (rows > 0) {
                System.out.println("✓ Insert successful");

                // Clean up test user
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM users WHERE username = ?");
                deleteStmt.setString(1, "test_user");
                deleteStmt.executeUpdate();
                deleteStmt.close();
                System.out.println("✓ Cleanup successful");
            }
        } catch (SQLException e) {
            System.out.println("✗ Error testing insert");
            e.printStackTrace();
        }

        System.out.println();

        // Close connection
        DBConnection.closeConnection(conn);

        System.out.println("=================================");
        System.out.println("All tests completed!");
        System.out.println("=================================");
        System.out.println("\nIf all tests passed:");
        System.out.println("1. Your database is set up correctly");
        System.out.println("2. You can start using database services");
        System.out.println("3. Review DATABASE_MIGRATION_GUIDE.md");
        System.out.println("\nIf tests failed:");
        System.out.println("1. Check MySQL is running");
        System.out.println("2. Run setup_database.bat");
        System.out.println("3. Check DBConnection.java settings");
    }
}
