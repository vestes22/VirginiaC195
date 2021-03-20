package controller;

import dao.CustomerDAO;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.City;
import model.Customer;
import model.Schedule;
import util.DBConnection;
import util.InvalidCustomerInformationException;

public class AddCustomerController implements Initializable
{
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField nameText;

    @FXML
    private TextField phoneText;

    @FXML
    private TextField addressText;

    @FXML
    private TextField address2Text;

    @FXML
    private ListView<City> cityList;

    @FXML
    private TextField zipText;

    @FXML
    private Label countryNameLabel;
   
    @FXML 
    private Label notificationLabel;

    /*
    * Pulls up a new window so user can add a new city, if the new customer's city does not already 
    * exist in the database.
    */
    @FXML
    void addNewCityButtonClicked(ActionEvent event)
    {
        try
        {
            Stage newCityStage = new Stage();
            Parent newCityScene;
            newCityScene = FXMLLoader.load(getClass().getResource("/view/add_city.fxml"));
            newCityStage.setScene(new Scene(newCityScene));
            newCityStage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /*
    * Creates a pop-up to ask users if they want to exit without saving. If yes, discards changes and 
    * directs user to homepage. If no, keeps user on current page with no additional actions taken.
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
    * Parses customer information from input fields, creates a new Customer object, 
    * and commits new customer to database.
    */
    @FXML
    void saveButtonClicked(ActionEvent event) 
    {
        try
        {
            /*
            * Parses customer information, and ensures all required fields have been filled out. 
            * If required field is missing data, throws InvalidCustomerInformationException.
            */
            Customer newCustomer = new Customer();
            String customerName = nameText.getText();
            String phone = phoneText.getText();
            String address = addressText.getText();
            String address2 = address2Text.getText();
            if (cityList.getSelectionModel().getSelectedItem() == null)
            {
                throw new InvalidCustomerInformationException();
            }
            String cityName = cityList.getSelectionModel().getSelectedItem().getCity();
            int cityId = cityList.getSelectionModel().getSelectedItem().getId();
            String zip = zipText.getText();
            int countryId = cityList.getSelectionModel().getSelectedItem().getCountryId();
            String countryName = countryNameLabel.getText();
            if (customerName.equals("") || phone.equals("") || address.equals("") || zip.equals(""))
            {
                throw new InvalidCustomerInformationException();
            }
            
            //Populates Customer object with parsed information.
            newCustomer.setCustomerName(customerName);
            newCustomer.setPhone(phone);
            newCustomer.setAddress(address);
            newCustomer.setAddress2(address2);
            newCustomer.setCity(cityName);
            newCustomer.setCityId(cityId);
            newCustomer.setZip(zip);
            newCustomer.setCountry(countryName);
            newCustomer.setCountryId(countryId);
            
            //Commits new Customer to the database.
            CustomerDAO customerDAO = new CustomerDAO(DBConnection.getConnection());
            customerDAO.create(newCustomer);
            Schedule.addCustomer(newCustomer);
            
            //Automatically redirects user to homepage.
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/homepage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(InvalidCustomerInformationException e)
        {
            notificationLabel.setText("*All fields must be completed before saving.");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Populates a list of cities for the user to select from when entering the customer address. 
        cityList.setItems(Schedule.getAllCities());
        //Lambda expression allows us to define the function used to set the ListView's cell factory, 
        //at the same time as setting the cell factory.
        cityList.setCellFactory(cell -> new ListCell<City>()
        {
            @Override
            protected void updateItem(City item, boolean empty)
            {
                super.updateItem(item, empty);
                
                if(empty || item == null || item.getCity() == null)
                {
                    setText(null);
                }
                else
                {
                    setText(item.getCity());
                }
            }
        });
        
        //Automatically chooses the country associated with the city the user selects.
        cityList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends City> ov, City old_val, City new_val) -> 
        {
            int countryId = cityList.getSelectionModel().getSelectedItem().getCountryId();
            int index = Schedule.getCountryIndex(countryId);
            countryNameLabel.setText((Schedule.getAllCountries().get(index).getCountry()));
        });
    }
}
