package virginiaestesc195;

import dao.AppointmentDAO;
import dao.CityDAO;
import dao.CountryDAO;
import dao.CustomerDAO;
import java.sql.Connection;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Appointment;
import model.City;
import model.Country;
import model.Customer;
import model.Schedule;
import util.DBConnection;
import util.Logger;

public class VirginiaEstesC195 extends Application 
{
    
    @Override
    public void start(Stage stage) throws Exception 
    {
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

   
    public static void main(String[] args) 
    {
        DBConnection.startConnection();
        Logger.createPrintWriter();
        try
        {
            Connection connection = DBConnection.getConnection();
            CustomerDAO custDAO = new CustomerDAO(connection);
            AppointmentDAO apptDAO = new AppointmentDAO(connection);
            CityDAO cityDAO = new CityDAO(connection);
            CountryDAO countryDAO = new CountryDAO(connection);
            
            ObservableList<Customer> customers = custDAO.findAll();
            //Lambda expression reduces the number of lines, improving readability and efficiency.
            customers.forEach((customer) -> Schedule.addCustomer(customer));
           
            
            ObservableList<Appointment> appointments = apptDAO.findAll();
            appointments.forEach(Schedule::addAppointment);
            
            ObservableList<City> cities = cityDAO.findAll();
            //Lambda expression reduces the number of lines, improving readability and efficiency.
            cities.forEach((city) -> Schedule.addCity(city));
            
            ObservableList<Country> countries = countryDAO.findAll();
            //Lambda expression reduces the number of lines, improving readability and efficiency.
            countries.forEach((country) -> Schedule.addCountry(country));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        launch(args);
        DBConnection.closeConnection();
        Logger.closeWriter();
    }
    
}
