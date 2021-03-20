package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Schedule;
import util.InvalidAppointmentTimeException;
import util.TimeConversion;

public class AppointmentDAO  extends DataAccessObject<Appointment>
{
    private static final String INSERT = 
            "INSERT INTO appointment (customerId, userId, title, description, location, contact, " + 
            "type, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) " + 
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SELECT_BY_ID = 
            "SELECT a.appointmentId, a.customerId, c.customerName, a.title, a.description, a.location, " +
            "a.contact, a.type, a.url, a.start, a.end , a.userId " +
            "FROM appointment a JOIN customer c " +
            "ON a.customerId = c.customerId " +
            "WHERE appointmentId = ?";
    
    private static final String SELECT_ALL = 
            "SELECT a.appointmentId, a.customerId, c.customerName, a.title, a.description, a.location, " +
            "a.contact, a.type, a.url, a.start, a.end, a.userId " +
            "FROM appointment a JOIN customer c " +
            "ON a.customerId = c.customerId";
    
    private static final String UPDATE = 
            "UPDATE appointment " + 
            "SET customerId = ?, " +
            "userId = ?, " +
            "title = ?, " + 
            "description = ?, " + 
            "location = ?, " + 
            "contact = ?, " +
            "type = ?, " +
            "url = ?, " +
            "start = ?, " +
            "end = ?, " +
            "lastUpdate = ?, " +
            "lastUpdateBy = ? " + 
            "WHERE appointmentId = ?";
    
    private static final String DELETE = 
            "DELETE FROM appointment WHERE appointmentId = ?";
    
    private static final String SELECT_NEW_ID =
            "SELECT LAST_INSERT_ID();";
            
    public AppointmentDAO(Connection connection)
    {
        super(connection);
    }

    /*
    * Used to query a specific appointment from the database based on its ID. 
    * Adds the information into an Appointment object that can be used by the program.
    */
    @Override
    public Appointment findById(int id) 
    {
        Appointment appointment = new Appointment();
        try (PreparedStatement statement = this.connection.prepareStatement(SELECT_BY_ID);)
        {
            statement.setInt(1, id);
            ResultSet results = statement.executeQuery();
            while(results.next())
            {
                appointment.setId(results.getInt(1));
                appointment.setCustomerId(results.getInt(2));
                appointment.setCustomerName(results.getString(3));
                appointment.setTitle(results.getString(4));
                appointment.setDescription(results.getString(5));
                appointment.setLocation(results.getString(6));
                appointment.setContact(results.getString(7));
                appointment.setType(results.getString(8));
                appointment.setUrl(results.getString(9));
                LocalDateTime localStartTime = TimeConversion.utcToLocal(LocalDateTime.of(results.getDate(10).toLocalDate(), results.getTime(10).toLocalTime()));
                appointment.setStart(localStartTime);
                LocalDateTime localEndTime = TimeConversion.utcToLocal(LocalDateTime.of(results.getDate(11).toLocalDate(), results.getTime(11).toLocalTime()));
                appointment.setEnd(localEndTime);
                appointment.setUserId(results.getInt(12));
            }
        }
        catch(SQLException | InvalidAppointmentTimeException e)
        {
            System.out.println(e.getMessage());
        }
        return appointment;
    }

    /*
    * Used to select all appointments in the database. 
    * Creates an Appointment object for each one, and returns them in an ObservableList
    * that can be used by the program.
    */
    @Override
    public ObservableList<Appointment> findAll()                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
    {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_ALL);)
        {
            ResultSet results = statement.executeQuery();
            while (results.next())
            {
                Appointment appointment = new Appointment();
                appointment.setId(results.getInt(1));
                appointment.setCustomerId(results.getInt(2));
                appointment.setCustomerName(results.getString(3));
                appointment.setTitle(results.getString(4));
                appointment.setDescription(results.getString(5));
                appointment.setLocation(results.getString(6));
                appointment.setContact(results.getString(7));
                appointment.setType(results.getString(8));
                appointment.setUrl(results.getString(9));
                LocalDateTime localStartTime = TimeConversion.utcToLocal(LocalDateTime.of(results.getDate(10).toLocalDate(), results.getTime(10).toLocalTime()));
                appointment.setStart(localStartTime);
                LocalDateTime localEndTime = TimeConversion.utcToLocal(LocalDateTime.of(results.getDate(11).toLocalDate(), results.getTime(11).toLocalTime()));
                appointment.setEnd(localEndTime);
                appointment.setUserId(results.getInt(12));
                appointments.add(appointment);
            }
        }
        catch(SQLException | InvalidAppointmentTimeException e)
        {
            System.out.println(e.getMessage());
        }
        return appointments;
    }

    /*
    * Takes an Appointment object and commits any changes to its associated appointment in the database.
    */
    @Override
    public Appointment update(Appointment dto) 
    {
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);)
        {
            statement.setInt(1, dto.getCustomerId());
            statement.setInt(2, dto.getUserId());
            statement.setString (3, dto.getTitle());
            statement.setString(4, dto.getDescription());
            statement.setString(5, dto.getLocation());
            statement.setString(6, dto.getContact());
            statement.setString(7, dto.getType());
            statement.setString(8, dto.getUrl());
            statement.setTimestamp(9, Timestamp.valueOf(TimeConversion.localToUTC(dto.getStart())));
            statement.setTimestamp(10, Timestamp.valueOf(TimeConversion.localToUTC(dto.getEnd())));
            statement.setTimestamp(11, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(12, Schedule.getActiveUser().getUsername());
            statement.setInt(13, dto.getId());
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
    * Takes an Appointment object, and uses it to create and add a new appointment to the database.
    */
    @Override
    public Appointment create(Appointment dto)
    {
        try
        (
            PreparedStatement statement = this.connection.prepareStatement(INSERT);
            PreparedStatement getApptId = this.connection.prepareStatement(SELECT_NEW_ID);
        )
        {
            statement.setInt(1, dto.getCustomerId());
            statement.setInt(2, Schedule.getActiveUser().getUserId());
            statement.setString(3, dto.getTitle());
            statement.setString(4, dto.getDescription());
            statement.setString(5, dto.getLocation());
            statement.setString(6, dto.getContact());
            statement.setString(7, dto.getType());
            statement.setString(8, dto.getUrl());
            statement.setTimestamp(9, Timestamp.valueOf(TimeConversion.localToUTC(dto.getStart())));
            statement.setTimestamp(10, Timestamp.valueOf(TimeConversion.localToUTC(dto.getEnd())));
            statement.setTimestamp(11, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(12, Schedule.getActiveUser().getUsername());
            statement.setTimestamp(13, Timestamp.valueOf(TimeConversion.localToUTC(LocalDateTime.now())));
            statement.setString(14, Schedule.getActiveUser().getUsername());
            Boolean bool = statement.execute();
            System.out.println(bool);
            ResultSet results = getApptId.executeQuery();
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
    * A testing method used to verify that new appointments are being added to the database correctly, and
    * that appointment IDs are being auto-incremented correctly. Returns the ID of the most recently inserted
    * appointment.
    */
    
    public int testId()
    {
        int id = -1;
        try(PreparedStatement statement = this.connection.prepareStatement(SELECT_NEW_ID);)
        {
            ResultSet results = statement.executeQuery();
            results.next();
            id = results.getInt(1);
            
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return id;
    }
    
    /*
    * Used to delete an appointment from the database based on appointment ID.
    */
    @Override
    public void delete(int id) 
    {
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);)
        {
            statement.setLong(1, id);
            statement.execute();
        }
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    
}
