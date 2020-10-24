package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import model.Schedule;
import util.TimeConversion;

public class CustomerDAO extends DataAccessObject<Customer>
{
    private static final String INSERT_CUSTOMER =
            "INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_ADDRESS = 
            "INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL = 
            "SELECT cu.customerId, cu.customerName, cu.addressId, a.address, a.address2, ci.cityId, ci.city, a.postalCode, " + 
            "co.countryId, co.country, a.phone, cu.active " +
            "FROM customer cu JOIN address a " +
            "ON cu.addressId = a.addressId " +
            "JOIN city ci " +
            "ON a.cityId = ci.cityId " +
            "JOIN country co " +
            "ON ci.countryId = co.countryId";
    
    private static final String SELECT_BY_ID = 
            "SELECT cu.customerId, cu.customerName, cu.addressId, a.address, a.address2, ci.cityId, ci.city, a.postalCode, " + 
            "co.countryId, co.country, a.phone, cu.active " +
            "FROM customer cu JOIN address a " +
            "ON cu.addressId = a.addressId " +
            "JOIN city ci " +
            "ON a.cityId = ci.cityId " +
            "JOIN country co " +
            "ON ci.countryId = co.countryId " +
            "WHERE cu.customerId = ?";
    
    private static final String UPDATE_CUSTOMER = 
            "UPDATE customer " +
            "SET customerName = ?, " + 
            "lastUpdate = ?, " + 
            "lastUpdateBy = ? " +
            "WHERE customerId = ?";
    
    private static final String UPDATE_ADDRESS = 
            "UPDATE address " + 
            "SET address = ?, " + 
            "address2 = ?, " +
            "cityId = ?, " +
            "postalCode = ?, " + 
            "phone = ?, " +
            "lastUpdate = ?, " +
            "lastUpdateBy = ? " +
            "WHERE addressId = ?";
    
    private static final String DELETE_CUSTOMER = 
            "DELETE FROM customer WHERE customerId = ?";
    
    private static final String SELECT_NEW_ID =
            "SELECT LAST_INSERT_ID();";
    
    public CustomerDAO(Connection connection)
    {
        super(connection);
    }
    
    /*
    * Used to query a specific customer from the database based on its ID. 
    * Adds the information into an Customer object that can be used by the program.
    */
    @Override
    public Customer findById(int id) 
    {
        Customer customer = new Customer();
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_BY_ID);)
        {
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next())
            {
                customer.setId(results.getInt(1));
                customer.setCustomerName(results.getString(2));
                customer.setAddressId(results.getInt(3));
                customer.setAddress(results.getString(4));
                customer.setAddress2(results.getString(5));
                customer.setCityId(results.getInt(6));
                customer.setCity(results.getString(7));
                customer.setZip(results.getString(8));
                customer.setCountryId(results.getInt(9));
                customer.setCountry(results.getString(10));
                customer.setPhone(results.getString(11));
                customer.setActive(results.getInt(12));
            }
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return customer;
    }

    /*
    * Used to select all customers in the database. 
    * Creates a Customer object for each one, and returns them in an ObservableList
    * that can be used by the program.
    */
    @Override
    public ObservableList<Customer> findAll()
    {
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try (PreparedStatement statement = this.connection.prepareStatement(SELECT_ALL);)
        {
            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                Customer customer = new Customer();
                customer.setId(results.getInt(1));
                customer.setCustomerName(results.getString(2));
                customer.setAddressId(results.getInt(3));
                customer.setAddress(results.getString(4));
                customer.setAddress2(results.getString(5));
                customer.setCityId(results.getInt(6));
                customer.setCity(results.getString(7));
                customer.setZip(results.getString(8));
                customer.setCountryId(results.getInt(9));
                customer.setCountry(results.getString(10));
                customer.setPhone(results.getString(11));
                customer.setActive(results.getInt(12));
                allCustomers.add(customer);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return allCustomers;
    }

    /*
    * Takes a Customer object and commits any changes to its associated customer in the database.
    */
    @Override
    public Customer update(Customer dto) 
    {
        try
        (
            PreparedStatement updateCustomer = this.connection.prepareStatement(UPDATE_CUSTOMER);   
            PreparedStatement updateAddress = this.connection.prepareStatement(UPDATE_ADDRESS);
        )
        {
            updateCustomer.setString(1, dto.getCustomerName());
            updateCustomer.setTimestamp(2, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            updateCustomer.setString(3, Schedule.getActiveUser().getUsername());
            updateCustomer.setInt(4, dto.getId());
            int affectedRows = updateCustomer.executeUpdate();
            System.out.println("The number of rows updated is: " + affectedRows);
            
            updateAddress.setString(1, dto.getAddress());
            updateAddress.setString(2, dto.getAddress2());
            updateAddress.setInt(3, dto.getCityId());
            updateAddress.setString(4, dto.getZip());
            updateAddress.setString(5, dto.getPhone());
            updateAddress.setTimestamp(6, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            updateAddress.setString(7, Schedule.getActiveUser().getUsername());
            updateAddress.setInt(8, dto.getAddressId());
            int affectedAddressRows = updateAddress.executeUpdate();
            System.out.println("The number of updated address rows is: " + affectedAddressRows);
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return dto;
    }

    /*
    * Takes a Customer object, and uses it to create and add a new customer to the database.
    */
    @Override
    public Customer create(Customer dto) 
    {
        try
        (
            PreparedStatement insertCustomer = this.connection.prepareStatement(INSERT_CUSTOMER);
            PreparedStatement insertAddress = this.connection.prepareStatement(INSERT_ADDRESS);
            PreparedStatement getNewAddressId = this.connection.prepareStatement(SELECT_NEW_ID);
            PreparedStatement getNewCustomerId = this.connection.prepareStatement(SELECT_NEW_ID);
        )
        {
            insertAddress.setString(1, dto.getAddress());
            insertAddress.setString(2, dto.getAddress2());
            insertAddress.setInt(3, dto.getCityId());
            insertAddress.setString(4, dto.getZip());
            insertAddress.setString(5, dto.getPhone());
            insertAddress.setTimestamp(6, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            insertAddress.setString(7, Schedule.getActiveUser().getUsername());
            insertAddress.setTimestamp(8, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            insertAddress.setString(9, Schedule.getActiveUser().getUsername());
            insertAddress.execute();
            ResultSet addressResults = getNewAddressId.executeQuery();
            addressResults.next();
            dto.setAddressId(addressResults.getInt(1));
            
            insertCustomer.setString(1, dto.getCustomerName());
            insertCustomer.setInt(2, dto.getAddressId());
            insertCustomer.setInt(3, 0);
            insertCustomer.setTimestamp(4, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            insertCustomer.setString(5, Schedule.getActiveUser().getUsername());
            insertCustomer.setTimestamp(6, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            insertCustomer.setString(7, Schedule.getActiveUser().getUsername());
            insertCustomer.execute();
            ResultSet customerResults = getNewCustomerId.executeQuery();
            customerResults.next();
            dto.setId(customerResults.getInt(1));
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
       
        return dto;
    }
    
    /*
    * Used to delete a customer from the database based on customer ID.
    */
    @Override
    public void delete(int id) 
    {
        try
        (
            PreparedStatement statement = this.connection.prepareStatement(DELETE_CUSTOMER);
        )
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
