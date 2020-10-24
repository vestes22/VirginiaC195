package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;

public class AppointmentDetailsController implements Initializable
{
    Stage stage;
    Parent scene;

    @FXML
    private Label titleText;

    @FXML
    private Label descriptionText;

    @FXML
    private Label contactText;

    @FXML
    private Label startText;

    @FXML
    private Hyperlink customerLink;

    @FXML
    private Label locationText;

    @FXML
    private Label typeText;

    @FXML
    private Label endText;

    @FXML
    private Label urlText;
    
    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> apptCustomerCol;

    @FXML
    private TableColumn<Appointment, String> apptTitleCol;
    
    @FXML
    private TableColumn<Appointment, LocalDate> apptDateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> apptStartCol;

    @FXML
    private TableColumn<Appointment, LocalTime> apptEndCol;
    
    @FXML 
    private Label notificationLabel;

    /*
    * Refreshes the details screen with the details of whichever appointment the user has selected.
    */
    @FXML
    void apptDetailsButtonClicked(ActionEvent event) 
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
            notificationLabel.setText("Please select an appointment to view.");
        }
    }
    
    /*
    * When customer name is clicked, redirects user to screen with that customer's details.
    */
    @FXML
    void customerLinkClicked(ActionEvent event) 
    {
        int customerIndex = -1;
        for(int i = 0; i < Schedule.getAllCustomers().size(); i++)
        {
            if (Schedule.getAllCustomers().get(i).getCustomerName().equals(customerLink.getText()))
            {
                customerIndex = i;
            }
        }
        
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/customer_details.fxml"));
            loader.load();
            CustomerDetailsController customerController = loader.getController();
            
            customerController.selectCustomerDetails(Schedule.getAllCustomers().get(customerIndex));
            stage = (Stage)((Hyperlink)event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    * When the edit button is clicked, redirects user to screen where they can edit and save 
    * appointment details for whichever appointment they currently have selected on the table.
    */
    @FXML
    void editButtonClicked(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/edit_appointment.fxml"));
            loader.load();
            EditAppointmentController editController = loader.getController();
            
            editController.editAppointmentDetails(appointmentTable.getSelectionModel().getSelectedItem());
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        
    }

    /*
    * Redirects user back to homepage.
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
    * Used by EditAppointmentController to pre-fill input fields with the current
    * appointment details.
    */
    public void selectAppointmentDetails(Appointment appointment)
    {
        try
        {      
            titleText.setText(appointment.getTitle());
            descriptionText.setText(appointment.getDescription());
            contactText.setText(appointment.getContact());
            startText.setText(String.valueOf(appointment.getStartTime()));
            locationText.setText(appointment.getLocation());
            typeText.setText(appointment.getType());
            endText.setText(String.valueOf(appointment.getEndTime()));
            urlText.setText(appointment.getUrl());
            customerLink.setText(appointment.getCustomerName());
            appointmentTable.getSelectionModel().select(appointment);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        appointmentTable.setItems(Schedule.getUserAppointments());
        apptCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        apptTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        apptDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        apptStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        apptEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

}
