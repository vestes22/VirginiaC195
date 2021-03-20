package controller;

import dao.AppointmentDAO;
import dao.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.Schedule;
import util.DBConnection;

public class HomepageController implements Initializable
{
    Stage stage;
    Parent scene;     

    @FXML
    private TableView<Appointment> appointmentTable;
    
    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Appointment, Integer> apptCustomerCol;

    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    
    @FXML
    private TableColumn<Appointment, LocalDate> apptDateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> apptStartCol;

    @FXML
    private TableColumn<Appointment, LocalTime> apptEndCol;

    @FXML
    private TableColumn<Customer, Integer> custIdCol;

    @FXML
    private TableColumn<Customer, String> custNameCol;
    
    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, String> custAddressCol;

    @FXML
    private Label notificationLabel;
    
    /*
    * Navigates user to the Add Appointment screen.
    */
    @FXML
    void apptAddButtonClicked(ActionEvent event)
    {
        try
        {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/add_appointment.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    * Creates a pop-up asking user if they are sure they want to delete the selected appointment.
    * If yes, deletes the associated Appointment object from the program and also deletes from database.
    */
    @FXML
    void apptDeleteButtonClicked(ActionEvent event)
    {
        if (appointmentTable.getSelectionModel().getSelectedItem() == null)
        {
            notificationLabel.setText("Please select an appointment to delete.");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this appointment?");
            Optional<ButtonType> answer = alert.showAndWait();
        
            if(answer.isPresent() && answer.get() == ButtonType.OK)
            {
                int appointmentId = appointmentTable.getSelectionModel().getSelectedItem().getId();
                AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.getConnection());
                appointmentDAO.delete(appointmentId);
                Schedule.removeAppointment(appointmentTable.getSelectionModel().getSelectedItem());
                Schedule.removeUserAppointment(appointmentTable.getSelectionModel().getSelectedItem());
            }
        }
    }

    /*
    * Navigates user to the Appointment Details page, where they can view the details for the selected appointment.
    */
    @FXML
    void apptDetailsButtonClicked(ActionEvent event)
    {
        if (appointmentTable.getSelectionModel().getSelectedItem() == null)
        {
            notificationLabel.setText("*Please select an appointment to view.");
        }
        else
        {   
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/appointment_details.fxml"));
                loader.load();
                AppointmentDetailsController appointmentController = loader.getController();
            
                appointmentController.selectAppointmentDetails(appointmentTable.getSelectionModel().getSelectedItem());
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*
    * Navigates user to view the Calendar by week and by month.
    */
    @FXML
    void calendarButtonClicked(ActionEvent event) 
    {
        try 
        {
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/calendar.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    * Navigates the user to a form where they can add a new customer.
    */
    @FXML
    void custAddButtonClicked(ActionEvent event)
    {
        try 
        {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/add_customer.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    * Creates a pop-up asking user if they are sure they want to delete the selected customer.
    * If yes, deletes both the associated Customer object from the program and also deletes customer
    * from the database.
    */
    @FXML
    void custDeleteButtonClicked(ActionEvent event) 
    {
        if (customerTable.getSelectionModel().getSelectedItem() == null)
        {
            notificationLabel.setText("Please select a customer to delete.");
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete this customer?");
            Optional<ButtonType> answer = alert.showAndWait();
        
            if(answer.isPresent() && answer.get() == ButtonType.OK)
            {
                int customerId = customerTable.getSelectionModel().getSelectedItem().getId();
                CustomerDAO customerDAO = new CustomerDAO(DBConnection.getConnection());
                customerDAO.delete(customerId);
                Schedule.removeCustomer(customerTable.getSelectionModel().getSelectedItem());
            }
        }
    }

    /*
    * Navigates user to a page where they can view additional details about the customer
    * they have selected from the table.
    */
    @FXML
    void custDetailsButtonClicked(ActionEvent event)
    {
        if (customerTable.getSelectionModel().getSelectedItem() == null)
        {
            notificationLabel.setText("*Please select a customer to view.");
        }
        else
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/customer_details.fxml"));
                loader.load();
                CustomerDetailsController customerController = loader.getController();
        
                customerController.selectCustomerDetails(customerTable.getSelectionModel().getSelectedItem());
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    /*
    * Logs user out of the session, and returns program to the Login page.
    */
    @FXML
    void exitButtonClicked(ActionEvent event) 
    {
        try
        {
            Schedule.clearUserAppointments();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /*
    * Navigates user to the reportal.
    */
    @FXML
    void reportalButtonClicked(ActionEvent event) 
    {
         try
        {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/reportal.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {  
        // Populates table with list of customers.
        customerTable.setItems(Schedule.getAllCustomers());
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        // Populates table with list of appointments for the currently logged in user only.
        appointmentTable.setItems(Schedule.getUserAppointments());
        apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

}
