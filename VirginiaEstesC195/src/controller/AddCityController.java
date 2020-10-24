package controller;

import dao.CityDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.City;
import model.Country;
import model.Schedule;
import util.DBConnection;


/*
* The Add City window is used when updating and creating addresses for customers. 
* If existing city is not available to select from, user can choose to add a new one.
*/
public class AddCityController implements Initializable
{
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField cityNameText;

    @FXML
    private ListView<Country> countryList;
    
    @FXML 
    private Label notificationLabel;
    
    //Closes the Add New City window without saving changes.
    @FXML
    void cancelButtonClicked(ActionEvent event) 
    {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.close();
    }

    /*
    * Ensures the new city has a name. Commits the new city to the database. Closes the Add City window.
    */
    @FXML
    void saveButtonClicked(ActionEvent event) 
    {
        if (cityNameText.getText() == null)
        {
            notificationLabel.setText("Please provide a city name.");
        }
        else
        {
            City city = new City();
            city.setCity(cityNameText.getText());
            city.setCountryId(countryList.getSelectionModel().getSelectedItem().getId());
            CityDAO cityDAO = new CityDAO(DBConnection.getConnection());
            city = cityDAO.create(city);
            Schedule.addCity(city);
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.close();
        }
    }
    
    @Override 
    public void initialize(URL url, ResourceBundle rb)
    {
        //Populates list of countries the user can select from, for the location of the new city.
        countryList.setItems(Schedule.getAllCountries());
        //Lambda expression allows us to define the function used to set the ListView's cell factory, 
        //at the same time as setting the cell factory.
        countryList.setCellFactory(cell -> new ListCell<Country>() 
        {
            @Override
            protected void updateItem(Country item, boolean empty) 
            {
                super.updateItem(item, empty);

                if (empty || item == null || item.getCountry() == null) 
                {
                    setText(null);
                } 
                else 
                {
                    setText(item.getCountry());
                }
            }
        });
        countryList.getSelectionModel().select(0);
    }
}
