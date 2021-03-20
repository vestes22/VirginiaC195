package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Schedule;
import model.User;
import util.DBConnection;
import util.DBQuery;
import util.InvalidLoginException;
import util.Logger;

public class LoginController implements Initializable
{
    Stage stage;
    Parent scene;
    
    @FXML
    private Label notificationLabel;

    @FXML
    private Label loginTitleLabel;
    
    @FXML 
    private Label usernameLabel;
    
    @FXML
    private Label passwordLabel;
    
    @FXML
    private Button submitButton;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField usernameText;
    
    /*
    * Validates login credentials and logs user into session.
    */
    @FXML
    void sumbitButtonClicked(ActionEvent event)
    {
        try
        {
            //Establishes database connection and gets username and password from user.
            Connection connection = DBConnection.getConnection();
            DBQuery.setStatement(connection);
            Statement statement = DBQuery.getStatement();
            String usernameLogin = usernameText.getText();
            String passwordLogin = passwordText.getText();
            
            //Checks database to see if matching results are returned.
            String selectLoginInfo = "SELECT * FROM user WHERE username = '" + usernameLogin + "' AND password = '" + passwordLogin + "';";
            statement.execute(selectLoginInfo);
            ResultSet loginResults = statement.getResultSet();
            
            //If no matching results, notifies user that username or password is incorrect, in either English or Spanish
            //Depending on user's default language on their system.
            if (loginResults.next() == false)
            {
                throw new InvalidLoginException();
            }
            
            //If matching results are returned, logs user in to application and sets them as the currently active user.
            else
            {
                User user;
     
                int userId = loginResults.getInt("userId");
                String username = loginResults.getString("username");
                String password = loginResults.getString("password");
                int active = loginResults.getInt("active");
                LocalDate createDate = loginResults.getDate("createDate").toLocalDate();
                LocalTime createTime = loginResults.getTime("createDate").toLocalTime();
                String createdBy = loginResults.getString("createdBy");
                LocalDateTime lastUpdate = loginResults.getTimestamp("lastUpdate").toLocalDateTime();
                String lastUpdateBy = loginResults.getString("lastUpdateBy");
                    
                user =  new User(userId, username, password, active, createDate, createTime, createdBy, lastUpdate, lastUpdateBy);
                Schedule.setActiveUser(user);
                
                LocalDate loginDate = LocalDate.now();
                LocalTime loginTime = LocalTime.now();
                System.out.println(loginTime);
            
                Schedule.getAllAppointments().stream().filter((appointment) -> (loginDate.getMonthValue() == appointment.getAppointmentDate().getMonthValue() && loginDate.getDayOfMonth() == appointment.getAppointmentDate().getDayOfMonth())).forEachOrdered((appointment) -> {
                    long timeDifference = ChronoUnit.MINUTES.between(loginTime, appointment.getStartTime());
                    if (timeDifference > 0 && timeDifference <= 15) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("You have an appointment with " + appointment.getCustomerName() + " at " + appointment.getStartTime());
                        Optional<ButtonType> answer = alert.showAndWait();
                        System.out.println(timeDifference);
                    }
                });
                
                //Populates an ObservableList with appointments for the active user only. User is unable to edit or manipulate appointments 
                //For another user.
                for (int i = 0; i < Schedule.getAllAppointments().size(); i++)
                {
                    if (Schedule.getAllAppointments().get(i).getUserId() == Schedule.getActiveUser().getUserId())
                    {
                        Schedule.addUserAppointment(Schedule.getAllAppointments().get(i));
                    }    
                }
    
                //Logs the user's username and login timestame into an external .txt file.
                Logger.logUser("User: " + Schedule.getActiveUser().getUsername() + " Login Date: " + loginDate + " Login Time: " + loginTime);
                
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/homepage.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (InvalidLoginException e)
        {
            ResourceBundle resource = ResourceBundle.getBundle("util/language", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("es"))
            {
                String loginError = resource.getString("error");
                notificationLabel.setText(loginError);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        //Uses the user's computer language settings to populate the login screen text with the correct language (either English or Spanish).
        ResourceBundle resource = ResourceBundle.getBundle("util/language", Locale.getDefault());
        
        if (Locale.getDefault().getLanguage().equals("en") || Locale.getDefault().getLanguage().equals("es"))
        {
            String loginTitle = resource.getString("loginTitle");
            String username = resource.getString("username");
            String password = resource.getString("password");
            String submit = resource.getString("submitButton");
            loginTitleLabel.setText(loginTitle);
            usernameLabel.setText(username + ": ");
            passwordLabel.setText(password + ": ");
            submitButton.setText(submit);
        }
    }
}
