package util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger 
{
    private static FileWriter fileWriter;
    private static PrintWriter printWriter;
    
    //Creates PrintWriter used to log timestamps for user logins.
    public static PrintWriter createPrintWriter()
    {
        try
        {
          fileWriter = new FileWriter("usernameLogger", true);
          printWriter = new PrintWriter(fileWriter);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
        }
        return printWriter;
    }
    
    public static PrintWriter getPrintWriter()
    {
        return printWriter;
    }
    
    //Logs the user's login details to external .txt file.
    public static void logUser(String loginDetails)
    {
        printWriter.println(loginDetails);
    }
    
    public static void closeWriter()
    {
        printWriter.close();
    }
}
