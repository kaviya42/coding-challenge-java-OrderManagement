package Connect;

import java.sql.Connection;
import java.sql.DriverManager;

public class DataConnect {
    
    private static Connection con;
    
    private DataConnect()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/codchal", "root", "KaVi_111#");      
			System.out.println("Connected to the database");
        }
        catch(Exception e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static Connection getConnect() {
        if (con == null) {
            try {
                new DataConnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return con;
    }
    
}
