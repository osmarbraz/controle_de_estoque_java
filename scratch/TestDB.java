import java.sql.Connection;
import java.sql.DriverManager;

public class TestDB {
    public static void main(String[] args) {
        String[] passwords = {"", "root", "123456", "mysql", "admin", "1234", "password"};
        String user = "root";
        
        for (String pass : passwords) {
            String url = "jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, user, pass);
                System.out.println("SUCCESS_PASSWORD:" + pass);
                conn.close();
                return;
            } catch (Exception e) {
                System.out.println("FAILED_PASSWORD:" + pass + " -> " + e.getMessage());
            }
        }
    }
}
