package controller;

import dao.AppointmentDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;
import model.Schedule;
import util.DBConnection;
import util.InvalidAppointmentTimeException;
import util.OverlappingAppointmentException;

public class AddAppointmentController implements Initializable
{
    
    Stage stage;
    Parent scene;
    private final ObservableList<String> months = FXCollections.observableArrayList();
    private final String monthsArray[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @FXML
    private TextField titleText;

    @FXML
    private TextField locationText;

    @FXML
    private TextField typeText;

    @FXML
    private TextField contactText;

    @FXML
    private TextField URLText;
    
    @FXML
    private ListView<String> monthList;

    @FXML
    private TextField dayText;

    @FXML
    private TextField startHourText;

    @FXML
    private TextField endHourText;

    @FXML
    private TextField yearText;

    @FXML
    private TextField startMinText;

    @FXML
    private TextField endMinText;

    @FXML
    private ListView<Customer> customerList;

    @FXML
    private TextArea descriptionText;
    
    @FXML 
    private Label notificationLabel;

    
    /*
    * Creates a pop-up asking user if they want to exit without saving. If yes, returns user to homepage.
    * If no, stays on current page with no additional actions.
    */
    @FXML
    void cancelButtonClicked(ActionEvent event) 
    {
        //Pop-Up to ensure user wants to quit without saving changes.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();
        
        if(answer.isPresent() && answer.get() == ButtonType.OK)
        {
            //If user wants to exit without saving, navigates user back to homepage.
            try
            {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/homepage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    
    /*
    * Parses data from input fields, saves it into an Appointment object, and commits the new appointment to
    * the database.
    */
    @FXML
    void saveButtonClicked(ActionEvent event)
    {
        try
        {
            //Parses appointment details from input fields.
            Appointment newAppointment = new Appointment();
            String title = titleText.getText();
            int customerId = customerList.getSelectionModel().getSelectedItem().getId();
            String customerName = customerList.getSelectionModel().getSelectedItem().getCustomerName();
            String location = locationText.getText();
            String contact = contactText.getText();
            String type = typeText.getText();
            String url = URLText.getText();
            String description = descriptionText.getText();
     
            newAppointment.setCustomerId(customerId);
            newAppointment.setCustomerName(customerName);
            
            //Ensures all fields have content.
            if (title.equals("") || location.equals("") || contact.equals("") || type.equals("") || url.equals("") || description.equals(""))
            {
                notificationLabel.setText("*All fields require values.");
            }
            
            //Adds the appointment info to a new Appointment object.
            else
            {
                newAppointment.setTitle(title);
                newAppointment.setLocation(location);
                newAppointment.setContact(contact);
                newAppointment.setType(type);
                newAppointment.setUrl(url);
                newAppointment.setDescription(description);
            }
            
            if (dayText.getText().equals("") || yearText.getText().equals("") || startHourText.getText().equals("") || 
                    startMinText.getText().equals("") || endHourText.getText().equals("") || endMinText.getText().equals(""))
            {
                notificationLabel.setText("*All fields must be completed before saving.");
            }                
            else
            {
                int monthInt = monthList.getSelectionModel().getSelectedIndex() + 1;
                int dayOfMonth = Integer.parseInt(dayText.getText());
                int year = Integer.parseInt(yearText.getText());
                int startHour = Integer.parseInt(startHourText.getText());
                int startMinute = Integer.parseInt(startMinText.getText());
                int endHour = Integer.parseInt(endHourText.getText());
                int endMinute = Integer.parseInt(endMinText.getText());


                LocalDateTime startDateTime = LocalDateTime.of(year, monthInt, dayOfMonth, startHour, startMinute);
                LocalDateTime endDateTime = LocalDateTime.of(year, monthInt, dayOfMonth, endHour, endMinute);

                newAppointment.setStart(startDateTime);
                newAppointment.setEnd(endDateTime);
                
                for (Appointment appointment : Schedule.getUserAppointments())
                {
                    if (newAppointment.getStart().isAfter(appointment.getStart()) && newAppointment.getStart().isBefore(appointment.getEnd()))
                    {
                        throw new OverlappingAppointmentException();
                    }
                    if (newAppointment.getStart().isBefore(appointment.getStart()) && newAppointment.getEnd().isAfter(appointment.getStart()))
                    {
                        throw new OverlappingAppointmentException();
                    }
                    if (newAppointment.getStart().equals(appointment.getStart()) || newAppointment.getEnd().equals(appointment.getEnd()))
                    {
                        throw new OverlappingAppointmentException();
                    }
                }

                //Commits the new appointment to the database.
                AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.getConnection());
                appointmentDAO.create(newAppointment);
                Schedule.addAppointment(newAppointment);
                Schedule.addUserAppointment(newAppointment);

                //Navigates back to the homepage when finished.
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/homepage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            
        }
        catch (NumberFormatException e)
        {
            notificationLabel.setText("*Invalid date or times.");
        }
        catch (InvalidAppointmentTimeException e)
        {
            //InvalidAppointmentTimeException is thrown by Appointment object's setStart() method.
            notificationLabel.setText("*Cannot schedule outside of business hours.");
        }
        catch (OverlappingAppointmentException e)
        {
            notificationLabel.setText("Another appointment is already scheduled during this time.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            notificationLabel.setText("*All fields require values.");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        /*
        * Populates customer names into a ListView for user to select from when setting the appointment customer.
        */
        customerList.setItems(Schedule.getAllCustomers());
        customerList.setCellFactory(cell -> new ListCell<Customer>() 
        //Lambda expression allows us to define the function used to set the ListView's cell factory, 
        //at the same time as setting the cell factory.
        {
            @Override
            protected void updateItem(Customer item, boolean empty) 
            {
                super.updateItem(item, empty);

                if (empty || item == null || item.getCustomerName() == null) 
                {
                    setText(null);
                } 
                else 
                {
                    setText(item.getCustomerName());
                }

            }
        });
        
        customerList.getSelectionModel().select(0);
        
        /*
         * Populate months into a ListView for user to select from when setting the appointment date.
         * Selects January by default.
         */
        months.addAll(Arrays.asList(monthsArray));
        monthList.setItems(months);
        monthList.getSelectionModel().select("JAN");
    }

}
