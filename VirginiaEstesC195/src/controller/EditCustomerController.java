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

public class EditCustomerController implements Initializable
{
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField custIdText;
    
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
    * Allows user to add a new city, if the needed city does not already exist on the list.
    * New city is added via a new window.
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
            System.out.println(e.getMessage());
        }
    }

    /*
    * Creates a pop-up asking user if they want to exit without saving changes. 
    * If yes, disregards changes and redirects user back to homepage. 
    * If no, closes the pop-up with no additional action.
    */
    @FXML
    void cancelButtonClicked(ActionEvent event) throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to exit without saving?");
        Optional<ButtonType> answer = alert.showAndWait();
        
        if (answer.isPresent() && answer.get() == ButtonType.OK)
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
    }
    
    /*
    * Parses the customer data from the input fields and updates its existing Customer object.
    * Commits changes to the database.
    */

    @FXML
    void saveButtonClicked(ActionEvent event)
    {
        try
        {
            //Parses data from the input fields.
            int customerId = Integer.parseInt(custIdText.getText());
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
         
            //If any data is missing, throws an InvalidCustomerInformationException.
            if (customerName.equals("") || phone.equals("") || address.equals("") || zip.equals(""))
            {
                throw new InvalidCustomerInformationException();
            }
            
            // Finds the existing customer object. Replaces it with new, updated customer object.
            int customerIndex = Schedule.getCustomerIndex(customerId);
            Customer customer = new Customer();
            customer.setCustomerName(customerName);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setAddress2(address2);
            customer.setZip(zip);
            customer.setCity(cityName);
            customer.setCityId(cityId);
            customer.setCountry(countryName);
            customer.setCountryId(countryId);

            // Commits customer changes to the database.
            Schedule.updateCustomer(customer, customerIndex);
            CustomerDAO customerDAO = new CustomerDAO(DBConnection.getConnection());
            customerDAO.update(Schedule.getAllCustomers().get(customerIndex));

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
    
    /*
    * Used to pre-fill input fields with existing customer data for the customer being edited.
    */
    public void editCustomerDetails(Customer customer)
    {
        try
        {
            custIdText.setText(String.valueOf(customer.getId()));
            nameText.setText(customer.getCustomerName());
            phoneText.setText(customer.getPhone());
            addressText.setText(customer.getAddress());
            address2Text.setText(customer.getAddress2());
            zipText.setText(customer.getZip());
            countryNameLabel.setText(customer.getCountry());
            cityList.getSelectionModel().select(Schedule.getCityIndex(customer.getCityId()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
       
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Don't let the user modify the ID.
        custIdText.setDisable(true);
        
        // Populate the list of cities for user to choose from.
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
        
        //Automatically selects the country associated with the city the user selects.
        cityList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends City> ov, City old_val, City new_val) -> 
        {
            int countryId = cityList.getSelectionModel().getSelectedItem().getCountryId();
            int index = Schedule.getCountryIndex(countryId);
            countryNameLabel.setText((Schedule.getAllCountries().get(index).getCountry()));
        });
    }

}
