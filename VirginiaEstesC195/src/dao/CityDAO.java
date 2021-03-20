package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;
import model.Schedule;
import util.TimeConversion;

public class CityDAO extends DataAccessObject<City>
{
    private static final String INSERT = 
            "INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
            "SELECT city, countryId FROM city WHERE cityId = ?";
    
    private static final String SELECT_ALL = 
            "SELECT cityId, city, countryId FROM city";
    
    private static final String UPDATE = 
            "UPDATE city " + 
            "SET city = ?, " +
            "countryId = ?, " +
            "lastUpdate = ?, " +
            "lastUpdateBy = ? " +
            "WHERE cityId = ?";
    
    private static final String DELETE = 
            "DELETE FROM city WHERE cityId = ?";
    
    private static final String SELECT_NEW_ID =
            "SELECT LAST_INSERT_ID();";
    
    
    public CityDAO(Connection connection)
    {
        super(connection);
    }

    /*
    * Used to query a specific city from the database based on its ID. 
    * Adds the information into a City object that can be used by the program.
    */
    @Override
    public City findById(int id) 
    {
        City city = new City();
        
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_BY_ID);)
        {
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                city.setId(id);
                city.setCity(results.getString(1));
                city.setCountryId(results.getInt(2));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return city;
    }

    /*
    * Used to select all cities in the database. 
    * Creates a City object for each one, and returns them in an ObservableList
    * that can be used by the program.
    */
    @Override
    public ObservableList<City> findAll() 
    {
        ObservableList<City> allCities = FXCollections.observableArrayList();
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_ALL);)
        {
            ResultSet results = statement.executeQuery();
            while(results.next())
            {
                City city = new City();
                city.setId(results.getInt(1));
                city.setCity(results.getString(2));
                city.setCountryId(results.getInt(3));
                allCities.add(city);
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return allCities;
    }

    /*
    * Takes a City object and commits any changes to its associated city in the database.
    */
    @Override
    public City update(City dto) 
    {
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);)
        {
            statement.setString(1, dto.getCity());
            statement.setInt(2, dto.getCountryId());
            statement.setTimestamp(3, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(4, Schedule.getActiveUser().getUsername());
            statement.setInt(5, dto.getId());
            int affectedRows = statement.executeUpdate();
            System.out.println("The number of updated rows is: " + affectedRows);
        }   
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return dto;
    }

    /*
    * Takes a City object, and uses it to create and add a new city to the database.
    */
    @Override
    public City create(City dto) 
    {
        try
        (
            PreparedStatement statement = this.connection.prepareStatement(INSERT);
            PreparedStatement getCityId = this.connection.prepareStatement(SELECT_NEW_ID);
        )
        {
            statement.setString(1, dto.getCity());
            statement.setInt(2, dto.getCountryId());
            statement.setTimestamp(3, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(4, Schedule.getActiveUser().getUsername());
            statement.setTimestamp(5, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(6, Schedule.getActiveUser().getUsername());
            statement.execute();
            ResultSet results = getCityId.executeQuery();
            results.next();
            dto.setId(results.getInt(1));
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return dto;
    }

    /*
    * Used to delete a city from the database based on the city ID.
    */
    @Override
    public void delete(int id) 
    {
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);)
        {
            statement.setInt(1, id);
            statement.execute();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}
