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
import util.InvalidCustomerInformationException;
import util.OverlappingAppointmentException;

public class EditAppointmentController implements Initializable
{
    Stage stage; 
    Parent scene;
    private final ObservableList<String> months = FXCollections.observableArrayList();
    private final String monthsArray[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @FXML
    private Label notificationLabel;

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
    private Label apptIdLabel;

    @FXML
    private ListView<Customer> customerList;

    @FXML
    private TextArea descriptionText;

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

    /*
    * Creates pop-up that asks user if they want to exit without saving. If yes, disregards changes and 
    * redirects user to homepage. If no, closes pop-up with no additional actions.
    */
    @FXML
    void cancelButtonClicked(ActionEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();
        
        if(answer.isPresent() && answer.get() == ButtonType.OK)
        {
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/appointment_details.fxml"));
                loader.load();
                int appointmentId = Integer.parseInt(apptIdLabel.getText());
                int appointmentIndex = Schedule.getAppointmentIndex(Schedule.getAllAppointments(), appointmentId);
                AppointmentDetailsController appointmentController = loader.getController();
                appointmentController.selectAppointmentDetails(Schedule.getAllAppointments().get(appointmentIndex));
               
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
    }

    /*
    * Parses the appointment details from the input fields and updates the corresponding, existing 
    * Appointment object in the static ListView. Commits changes to database.
    */
    @FXML
    void saveButtonClicked(ActionEvent event)
    {
        try
        {
            //Parses data from input fields.
            int appointmentId = Integer.parseInt(apptIdLabel.getText());
            String title = titleText.getText();
            int customerId = customerList.getSelectionModel().getSelectedItem().getId();
            String location = locationText.getText();
            String contact = contactText.getText();
            String type = typeText.getText();
            String url = URLText.getText();
            String description = descriptionText.getText();
            
            int appointmentIndex = Schedule.getAppointmentIndex(Schedule.getAllAppointments(), appointmentId);
            int userAppointmentIndex = Schedule.getAppointmentIndex(Schedule.getUserAppointments(), appointmentId);
            
            if (title.equals("") || location.equals("") || contact.equals("") || type.equals("") || url.equals("") || description.equals(""))
            {
                throw new InvalidCustomerInformationException();
            }
            
            else
            {
                /*
                * I have two static ObservableLists of appointments (also described in Schedule.java).
                * One contains all appointments in the database (mostly maintained for the Reportal).
                * The other only contains the appointments for the currently active user.
                * Both must be updated with changes.
                */
               
                Schedule.getAllAppointments().get(appointmentIndex).setTitle(title);
                Schedule.getAllAppointments().get(appointmentIndex).setLocation(location);
                Schedule.getAllAppointments().get(appointmentIndex).setContact(contact);
                Schedule.getAllAppointments().get(appointmentIndex).setType(type);
                Schedule.getAllAppointments().get(appointmentIndex).setUrl(url);
                Schedule.getAllAppointments().get(appointmentIndex).setDescription(description);
                
                Schedule.getUserAppointments().get(userAppointmentIndex).setTitle(title);
                Schedule.getUserAppointments().get(userAppointmentIndex).setLocation(location);
                Schedule.getUserAppointments().get(userAppointmentIndex).setContact(contact);
                Schedule.getUserAppointments().get(userAppointmentIndex).setType(type);
                Schedule.getUserAppointments().get(userAppointmentIndex).setUrl(url);
                Schedule.getUserAppointments().get(userAppointmentIndex).setDescription(description);
            }
                                
            int monthInt = monthList.getSelectionModel().getSelectedIndex() + 1;
            int dayOfMonth = Integer.parseInt(dayText.getText());
            int year = Integer.parseInt(yearText.getText());
            int startHour = Integer.parseInt(startHourText.getText());
            int startMinute = Integer.parseInt(startMinText.getText());
            int endHour = Integer.parseInt(endHourText.getText());
            int endMinute = Integer.parseInt(endMinText.getText());
           
            LocalDateTime startDateTime = LocalDateTime.of(year, monthInt, dayOfMonth, startHour, startMinute);
            LocalDateTime endDateTime = LocalDateTime.of(year, monthInt, dayOfMonth, endHour, endMinute);
            
            for (Appointment appointment : Schedule.getUserAppointments())
            {
                if (appointment.getId() != appointmentId)
                {
                    if (startDateTime.isAfter(appointment.getStart()) && startDateTime.isBefore(appointment.getEnd()))
                    {
                        throw new OverlappingAppointmentException();
                    }
                    if (startDateTime.isBefore(appointment.getStart()) && endDateTime.isAfter(appointment.getStart()))
                    {
                        throw new OverlappingAppointmentException();
                    }
                    if (startDateTime.equals(appointment.getStart()) || endDateTime.equals(appointment.getEnd()))
                    {
                        throw new OverlappingAppointmentException();
                    }
                }
            }
            
            Schedule.getAllAppointments().get(appointmentIndex).setStart(startDateTime);
            Schedule.getAllAppointments().get(appointmentIndex).setEnd(endDateTime);
            Schedule.getAllAppointments().get(appointmentIndex).setCustomerId(customerId);
            
            Schedule.getUserAppointments().get(userAppointmentIndex).setStart(startDateTime);
            Schedule.getUserAppointments().get(userAppointmentIndex).setEnd(endDateTime);
            Schedule.getUserAppointments().get(userAppointmentIndex).setCustomerId(customerId);

            //Commit appointment changes to the database.
            Appointment updatedAppointment = Schedule.getAllAppointments().get(appointmentIndex);
            AppointmentDAO appointmentDAO = new AppointmentDAO(DBConnection.getConnection());
            appointmentDAO.update(updatedAppointment);
            
            //Redirect back to the Appointment Details screen.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/appointment_details.fxml"));
            loader.load();
            AppointmentDetailsController appointmentController = loader.getController();
            appointmentController.selectAppointmentDetails(Schedule.getAllAppointments().get(appointmentIndex));
               
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch (NumberFormatException e)
        {
            notificationLabel.setText("*Invalid date or times.");
        }
        catch (InvalidCustomerInformationException e)
        {
            notificationLabel.setText("*All fields require values.");
        }
        catch (InvalidAppointmentTimeException e)
        {
            //This exception is thrown by the appointment object's setStart() method, if the appointment time is outside of business hours.
            notificationLabel.setText("*Appointment time outside of business hours.");
        }
        catch (OverlappingAppointmentException e)
        {
            notificationLabel.setText("*Another appointment is already scheduled during this time.");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /*
    * Used to pre-fill input fields with existing data for the appointment being edited.
    */
    public void editAppointmentDetails(Appointment appointment)
    {
        try
        {
            apptIdLabel.setText(String.valueOf(appointment.getId()));
            titleText.setText(appointment.getTitle());
            descriptionText.setText(appointment.getDescription());
            contactText.setText(appointment.getContact());
            locationText.setText(appointment.getLocation());
            typeText.setText(appointment.getType());
            int month = appointment.getAppointmentDate().getMonthValue();
            monthList.getSelectionModel().select(months.get(month - 1));
            monthList.scrollTo(month - 1);
            int index = -1;
            for (int i = 0; i < Schedule.getAllCustomers().size(); i++)
            {
                if (appointment.getCustomerName().equals(Schedule.getAllCustomers().get(i).getCustomerName()))
                {
                    index = i;
                }
            }
            customerList.scrollTo(index);
            customerList.getSelectionModel().select(index);
            dayText.setText(String.valueOf(appointment.getAppointmentDate().getDayOfMonth()));
            yearText.setText(String.valueOf(appointment.getAppointmentDate().getYear()));
            startHourText.setText(String.valueOf(appointment.getStartTime().getHour()));
            startMinText.setText(String.valueOf(appointment.getStartTime().getMinute()));
            endHourText.setText(String.valueOf(appointment.getEndTime().getHour()));
            endMinText.setText(String.valueOf(appointment.getEndTime().getMinute()));
            URLText.setText(appointment.getUrl());
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Populates the month ListView and the Customer ListView.
        months.addAll(Arrays.asList(monthsArray));
        monthList.setItems(months);
        
        customerList.setItems(Schedule.getAllCustomers());
        //Lambda expression allows us to define the function used to set the ListView's cell factory, 
        //at the same time as setting the cell factory.
        customerList.setCellFactory(cell -> new ListCell<Customer>() 
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
    }

}
