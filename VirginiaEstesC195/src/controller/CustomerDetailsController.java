package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import model.Schedule;
import util.DBConnection;
import util.DBQuery;

public class CustomerDetailsController implements Initializable
{

    Stage stage;
    Parent scene;
    
    @FXML
    private TableView<Customer> customerTable;
    
    @FXML 
    private TableColumn<Customer, Integer> custIdCol;
    
    @FXML 
    private TableColumn<Customer, String> custNameCol;
    
    @FXML
    private TableColumn<Customer, String> custPhoneCol;

    @FXML
    private TableColumn<Customer, String> custAddressCol;

    @FXML
    private Label customerIdLabel;
    
    @FXML
    private Label nameText;

    @FXML
    private Label phoneText;

    @FXML
    private Label activeText;

    @FXML
    private Label streetText;

    @FXML
    private Label street2Text;

    @FXML
    private Label cityText;

    @FXML
    private Label zipText;

    @FXML
    private Label countryText;
    
    /*
    * Refreshes screen with details about a customer selected from the tableview on the left.
    */
    @FXML
    void custDetailsButtonClicked(ActionEvent event)
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

    /*
    * Redirects user to a screen where they can edit the details for the currently selected customer.
    */
    @FXML
    void editButtonClicked(ActionEvent event) 
    {
        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/edit_customer.fxml"));
            loader.load();
            EditCustomerController editController = loader.getController();
            
            editController.editCustomerDetails(customerTable.getSelectionModel().getSelectedItem());
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
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    /*
    * Used by EditCustomerController to pre-fill customer fields with existing data for the customer 
    * when the Edit Customer page loads.
    */
    public void selectCustomerDetails(Customer customer)
    {
        try
        {
            String customerName;
            String phone;
            int active;
            String address;
            String address2;
            String city;
            String zip;
            String country;
            
            int customerId = customer.getId();
            Connection connection = DBConnection.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();
            
            String selectStatement = "SELECT cu.customerName, a.phone, cu.active, a.address, a.address2, ci.city, a.postalCode, co.country "
                    + "               FROM customer cu JOIN address a"
                    + "               ON cu.addressId = a.addressId"
                    + "               JOIN city ci"
                    + "               on a.cityId = ci.cityId"
                    + "               JOIN country co"
                    + "               ON ci.countryId = co.countryId"
                    + "               WHERE customerId = " + customerId + ";";
           
            
            statement.execute(selectStatement);
            ResultSet results = statement.getResultSet();
            
            results.next();
            customerName = results.getString("customerName");
            phone = results.getString("phone");
            active = results.getInt("active");
            address = results.getString("address");
            address2 = results.getString("address2");
            city = results.getString("city");
            zip = results.getString("postalCode");
            country = results.getString("country");
            
            customerIdLabel.setText(String.valueOf(customerId));
            nameText.setText(customerName);
            phoneText.setText(phone);
            activeText.setText(String.valueOf(active));
            streetText.setText(address);
            street2Text.setText(address2);
            cityText.setText(city);
            zipText.setText(zip);
            countryText.setText(country);   
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        customerTable.getSelectionModel().select(customer);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        customerTable.setItems(Schedule.getAllCustomers());
        custIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        custNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        custAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        custPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

}



