package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Schedule;
import util.TimeConversion;

public class CountryDAO extends DataAccessObject<Country>
{
    private static final String INSERT_COUNTRY = 
            "INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) " +
            "VALUES (?, ?, ?, ?, ?)";
    
    private static final String SELECT_ALL = 
            "SELECT countryId, country FROM country";
   
    private static final String SELECT_BY_ID = 
            "SELECT countryId, country FROM country WHERE countryId = ?";

    private static final String UPDATE_COUNTRY = 
            "UPDATE country " +
            "SET country = ?, " +
            "lastUpdate = ?, " +
            "lastUpdateBy = ? " +
            "WHERE countryId = ?";
    
    private static final String DELETE = 
            "DELETE FROM country WHERE countryId = ?";
 
    private static final String SELECT_NEW_ID =
            "SELECT LAST_INSERT_ID();";

    public CountryDAO (Connection connection)
    {
       super(connection); 
    }
    
    /*
    * Used to query a specific country from the database based on its ID. 
    * Adds the information into a Country object that can be used by the program.
    */
    @Override
    public Country findById(int id) 
    {
        Country country = new Country();
        try
        (PreparedStatement statement = this.connection.prepareStatement(SELECT_BY_ID);)
        {
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next())
            {
                country.setId(results.getInt(1));
                country.setCountry(results.getString(2));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return country;
    }
    
    /*
    * Used to select all countries in the database. 
    * Creates a Country object for each one, and returns them in an ObservableList
    * that can be used by the program.
    */
    @Override
    public ObservableList<Country> findAll() 
    {
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_ALL);)
        {
            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                Country country = new Country();
                country.setId(results.getInt(1));
                country.setCountry(results.getString(2));
                allCountries.add(country);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return allCountries;
    }

    /*
    * Takes a Country object and commits any changes to its associated country in the database.
    */
    @Override
    public Country update(Country dto) 
    {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_COUNTRY);)
        {
            statement.setString(1, dto.getCountry());
            statement.setTimestamp(2, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(3, Schedule.getActiveUser().getUsername());
            statement.setInt(4, dto.getId());
            int affectedRows = statement.executeUpdate();
            System.out.println("The number of affected rows is: " + affectedRows);
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return dto;
    }

    /*
    * Takes a Country object, and uses it to create and add a new country to the database.
    */
    @Override
    public Country create(Country dto) 
    {
        try
        (
            PreparedStatement statement = this.connection.prepareStatement(INSERT_COUNTRY);
            PreparedStatement getNewId = this.connection.prepareStatement(SELECT_NEW_ID);
        )
        {
            statement.setString(1, dto.getCountry());
            statement.setTimestamp(2, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(3, Schedule.getActiveUser().getUsername());
            statement.setTimestamp(4, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(5, Schedule.getActiveUser().getUsername());
            statement.execute();
            ResultSet results = getNewId.executeQuery();
            results.next();
            dto.setId(results.getInt(1));
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return dto;
    }

    /*
    * Used to delete a country from the database based on country ID.
    */
    @Override
    public void delete(int id) 
    {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE);)
        {
            statement.setInt(1, id);
            statement.execute();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}
