package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import util.DBConnection;

public class ReportalController implements Initializable 
{
    Stage stage;
    Parent scene;
    ToggleGroup group = new ToggleGroup();
    
    @FXML
    private RadioButton apptTypeRadio;

    @FXML
    private RadioButton consultantScheduleRadio;

    @FXML
    private RadioButton custScheduleRadio;
    
    @FXML
    private Label reportTitle;

    @FXML
    private Label reportDescription;

    @FXML
    private Label report;

    /*
    * Redirects user to the homepage.
    */
    @FXML
    void homeButtonClicked(ActionEvent event) 
    {
        try
        {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/homepage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /*
    * Generates a report on the screen. The report generated depends on which report
    * the user selects from the radio buttons.
    */
    @FXML
    void viewReportButtonClicked(ActionEvent event) 
    {
        if (group.getSelectedToggle().equals(apptTypeRadio))
        {
            reportTitle.setText("Appointment Types by Month");
            reportDescription.setText("Lists how many appointments of each type occur within each month.");
            apptTypePerMonth();
        }
        
        if (group.getSelectedToggle().equals(consultantScheduleRadio))
        {
            reportTitle.setText("Schedules By Consultant");
            reportDescription.setText("Displays upcoming appointments for each consultant.");
            scheduleByConsultant();
        }
        
        if (group.getSelectedToggle().equals(custScheduleRadio))
        {
            reportTitle.setText("Schedule By Consultant");
            reportDescription.setText("Displays upcoming appointments for each customer.");
            scheduleByCustomer();
        }
    }
    
    /*
    * Generates a report counting the number of each type of appointment per month.
    * Queries the database for the information, then formats and presents it to the user.
    */
     public void apptTypePerMonth() 
     {
        try 
        {
            StringBuilder reportText;
            try (Statement statement = DBConnection.getConnection().createStatement()) { 
                String reportQuery =
                        "SELECT MONTHNAME(start) as 'Month', type, COUNT(*) as 'Count' " +
                        "FROM appointment GROUP BY 1, 2 " +
                        " ORDER BY 1;";
                ResultSet results = statement.executeQuery(reportQuery);
                reportText = new StringBuilder();
                String currentMonth = "";
                while(results.next())
                {
                    if (!currentMonth.equals(results.getString("Month")))
                    {
                        currentMonth = results.getString("Month");
                        reportText.append("Month: ");
                        reportText.append(currentMonth);
                        reportText.append("\n");
                        reportText.append("-----------------------");
                        reportText.append("\n");
                    }
                    
                    reportText.append("Type: ");
                    reportText.append(results.getString("type"));
                    reportText.append("\n");
                    reportText.append("Total: ");
                    reportText.append(results.getInt("Count"));
                    reportText.append("\n");
                    reportText.append("\n");
                }
            }
            report.setText(reportText.toString());
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    } 
     
    /*
     * Generates a report detailing the the schedule for each constultant, or user.
     * Queries the database for the information, then formats and presents it to the user.
     */
    public void scheduleByConsultant() 
    {
        try 
        {
            StringBuilder reportText;
            try (Statement statement = DBConnection.getConnection().createStatement()) { 
                String reportQuery =
                        "SELECT u.userName, c.customerName, a.title, a.start " +
                        "FROM user u JOIN appointment a " +
                        "ON u.userId = a.userId " +
                        "JOIN customer c " +
                        "on a.customerId = c.customerId " +
                        "ORDER BY u.userName, c.customerName";
                ResultSet results = statement.executeQuery(reportQuery);
                reportText = new StringBuilder();
                String currentUser = "";
                while(results.next())
                {
                    if (!currentUser.equals(results.getString("userName")))
                    {
                        currentUser = results.getString("userName");
                        reportText.append("User: ");
                        reportText.append(currentUser);
                        reportText.append("\n");
                        reportText.append("-----------------------");
                        reportText.append("\n");
                    }
                    reportText.append("Customer: ");
                    reportText.append(results.getString("customerName"));
                    reportText.append(" | ");
                    reportText.append("Appointment Title: ");
                    reportText.append(results.getString("title"));
                    reportText.append(" | ");
                    reportText.append("Date: ");
                    reportText.append(results.getDate("start"));
                    reportText.append(" | ");
                    reportText.append("Start Time: ");
                    reportText.append(results.getTime("start"));
                    reportText.append("\n");
                    reportText.append("\n");
                }
            }
            report.setText(reportText.toString());
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    /*
    * Generates a report detailing appointments for each customer.
    * Queries the database for the information, then formats and presents it to the user.
    */
    public void scheduleByCustomer()
    {
         try 
        {
            StringBuilder reportText;
             try (Statement statement = DBConnection.getConnection().createStatement()) {
                 String reportQuery =
                         "SELECT u.userName, c.customerName, a.title, a.start " +
                         "FROM user u JOIN appointment a " +
                         "ON u.userId = a.userId " +
                         "JOIN customer c " +
                         "on a.customerId = c.customerId " +
                         "ORDER BY c.customerName, u.userName";
                 ResultSet results = statement.executeQuery(reportQuery);
                 reportText = new StringBuilder();
                 String currentCustomer = "";
                 while(results.next())
                 {
                     if (!currentCustomer.equals(results.getString("customerName")))
                     {
                         currentCustomer = results.getString("customerName");
                         reportText.append("Customer: ");
                         reportText.append(currentCustomer);
                         reportText.append("\n");
                         reportText.append("-----------------------");
                         reportText.append("\n");
                     }
                     reportText.append("User: ");
                     reportText.append(results.getString("userName"));
                     reportText.append(" | ");
                     reportText.append("Appointment Title: ");
                     reportText.append(results.getString("title"));
                     reportText.append(" | ");
                     reportText.append("Date: ");
                     reportText.append(results.getDate("start"));
                     reportText.append(" | ");
                     reportText.append("Start Time: ");
                     reportText.append(results.getTime("start"));
                     reportText.append("\n");
                     reportText.append("\n");
                 }}
            report.setText(reportText.toString());
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        apptTypeRadio.setToggleGroup(group);
        consultantScheduleRadio.setToggleGroup(group);
        custScheduleRadio.setToggleGroup(group);
        apptTypeRadio.setSelected(true);
    }    
    
}
