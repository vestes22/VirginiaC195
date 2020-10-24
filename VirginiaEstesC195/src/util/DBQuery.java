package util;

import java.sql.Connection;
import java.sql.Statement;

public class DBQuery 
{
    
    private static Statement statement;
    
    public static void setStatement(Connection connection) throws Exception
    {
        statement = connection.createStatement();
    }
    
    public static Statement getStatement()
    {
        return statement;
    }
    
}
