package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Schedule;

public class CalendarController implements Initializable {
    
    Stage stage;
    Parent scene;
    private final ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private final ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    private final ObservableList<String> months = FXCollections.observableArrayList();
    private final String monthsArray[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};

    @FXML
    private TableView<Appointment> weeklyAppointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> weeklyCustomerCol;

    @FXML
    private TableColumn<Appointment, String> weeklyTitleCol;

    @FXML
    private TableColumn<Appointment, LocalDate> weeklyDateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> weeklyStartCol;

    @FXML
    private TableColumn<Appointment, LocalTime> weeklyEndCol;

    @FXML
    private TableView<Appointment> monthlyAppointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> monthlyCustomerCol;

    @FXML
    private TableColumn<Appointment, String> monthlyTitleCol;

    @FXML
    private TableColumn<Appointment, LocalDate> monthlyDateCol;

    @FXML
    private TableColumn<Appointment, LocalTime> monthlyStartCol;

    @FXML
    private TableColumn<Appointment, LocalTime> monthlyEndCol;

    @FXML
    private ListView<String> monthList;

    @FXML
    private TextField yearText;
    
    @FXML
    private Label warningLabel;
    
    /*
    * Directs user back to homepage.
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
    * Redirects user to Appointment Details screen with details for the appointment they have selected 
    * on the table.
    */
    @FXML
    void monthlyDetailsButtonClicked(ActionEvent event) 
    {
        
        if (monthlyAppointmentsTable.getSelectionModel().getSelectedItem() == null)
        {
            warningLabel.setText("*Please select an appointment to view.");
        }
        else
        {   
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/appointment_details.fxml"));
                loader.load();
                AppointmentDetailsController appointmentController = loader.getController();
            
                appointmentController.selectAppointmentDetails(monthlyAppointmentsTable.getSelectionModel().getSelectedItem());
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
    * Refreshes the monthly appointments calendar table with appointments from the month and 
    * year that the user indicates.
    */
    @FXML
    void viewButtonClicked(ActionEvent event) 
    {
        try
        {
            warningLabel.setText("");
            monthlyAppointments.clear();
            Schedule.getUserAppointments().forEach((appointment) -> {
                LocalDate date = appointment.getAppointmentDate();
                int year = Integer.parseInt(yearText.getText());
                if (year < 2020)
                {
                    warningLabel.setText("*Please select a valid year: cannot view past appointments.");
                }
                if (year > 9999)
                {
                    warningLabel.setText("*Please select a valid year.");
                }
                if ((date.getMonthValue() == monthList.getSelectionModel().getSelectedIndex() + 1) 
                        && (date.getYear() == Integer.parseInt(yearText.getText()))) {
                    monthlyAppointments.add(appointment);
                }
            });
        
            monthlyAppointmentsTable.setItems(monthlyAppointments);
            monthlyCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            monthlyTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            monthlyDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
            monthlyStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            monthlyEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        }
        catch (NumberFormatException e)
        {
            warningLabel.setText("*Please enter a valid year.");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /*
    * Redirects user to the details page of the selected appointment.
    */
    @FXML
    void weeklyDetailsButtonClicked(ActionEvent event) 
    {
        
        if (weeklyAppointmentsTable.getSelectionModel().getSelectedItem() == null)
        {
            warningLabel.setText("*Please select an appointment to view.");
        }
        else
        {   
            try
            {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/appointment_details.fxml"));
                loader.load();
                AppointmentDetailsController appointmentController = loader.getController();
            
                appointmentController.selectAppointmentDetails(weeklyAppointmentsTable.getSelectionModel().getSelectedItem());
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
        /*
        * Populates months into a ListView for user to select from when selecting a month to view from calendar.
        * Selects the system's current month by default.
        * Populates the Month calendar table with appointment data for the current month.
        */
        
        months.addAll(Arrays.asList(monthsArray));
        monthList.setItems(months);
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        monthList.getSelectionModel().select(month - 1);
        monthList.scrollTo(month-1);
        
        yearText.setText(String.valueOf(today.getYear()));
        
        Schedule.getUserAppointments().stream().filter((appointment) -> ((appointment.getAppointmentDate().getMonthValue() == monthList.getSelectionModel().getSelectedIndex() + 1) 
                && (appointment.getAppointmentDate().getYear() == Integer.parseInt(yearText.getText())))).forEachOrdered((appointment) -> {
                    monthlyAppointments.add(appointment);
        });
        
        monthlyAppointmentsTable.setItems(monthlyAppointments);
        monthlyCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        monthlyTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        monthlyDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        monthlyStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        monthlyEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        
        
        /*
         * Populates the weeklyAppointments observable list with appointments occuring within the next 7 days.
         */
        
        //Lambda expression minimizes lines of code to be written, allowing for more efficient programming.
        Schedule.getUserAppointments().stream().filter((appointment) -> (appointment.getAppointmentDate().minusDays(7).compareTo(today) <= 0)).forEachOrdered((appointment) -> 
        {
            weeklyAppointments.add(appointment);
        });
        
        weeklyAppointmentsTable.setItems(weeklyAppointments);
        weeklyCustomerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        weeklyTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        weeklyDateCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        weeklyStartCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        weeklyEndCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }

}
