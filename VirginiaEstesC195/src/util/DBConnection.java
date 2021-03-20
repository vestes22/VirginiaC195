package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection 
{
     
   private static final String JDBCURL = "jdbc:mysql://3.227.166.251/U05O8C"; //The JDBC URL
   private static final String MYSQLJDBCDRIVER = "com.mysql.jdbc.Driver"; //A reference to the driver
   public static Connection connection = null;
   public static final String USERNAME = "U05O8C";
   public static String password = "53688558094";
    
   //Establishes a connection with the database.
   public static Connection startConnection() 
    {
        try
        {
            Class.forName(MYSQLJDBCDRIVER);
            connection = (Connection)DriverManager.getConnection(JDBCURL, USERNAME, password);
            System.out.println("Connection successful!");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Perhaps this Error: " + e.getMessage());
        }
        catch (SQLException e)
        {
            System.out.println("Is it this error Error: " + e.getMessage());
        }
        
        return connection;
    }
    
   //Closes the connection to the database.
    public static void closeConnection()
    {
        try
        {
            connection.close();
        }
        catch(SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public static Connection getConnection()
    {
        return connection;
    }
}
