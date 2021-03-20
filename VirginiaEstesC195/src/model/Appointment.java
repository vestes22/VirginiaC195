package model;

import dao.DataTransferObject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import util.InvalidAppointmentTimeException;

public class Appointment implements DataTransferObject
{
    private int id;
    private int customerId;
    private int userId;
    private String customerName;
    private String title;
    private String description;
    private String location;
    private String contact;
    private String type;
    private String url;
    private LocalDateTime start;
    private LocalTime startTime;
    private LocalDateTime end;
    private LocalTime endTime;
    private LocalDate appointmentDate;
    
    public Appointment()
    {
        
    }
    
    public Appointment 
    (
        int id,
        int customerId,
        int userId,
        String customerName,
        String title,
        String description,
        String location, 
        String contact,
        String type,
        String url,
        LocalDateTime start,
        LocalDateTime end      
    )
    {
        this.id = id;
        this.customerId = customerId;
        this.userId = userId;
        this.customerName = customerName;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
        
        int startHour = start.getHour();
        int startMinute = start.getMinute();
        this.startTime = LocalTime.of(startHour, startMinute);
        
        int endHour = end.getHour();
        int endMinute = end.getMinute();
        this.endTime = LocalTime.of(endHour, endMinute);
        
        int month = start.getMonthValue();
        int day = start.getDayOfMonth();
        int year = start.getYear();
        
        this.appointmentDate = LocalDate.of(year, month, day);
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
        
    public void setCustomerId(int customerId)
    {
        this.customerId = customerId;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public void setCustomerName(String customerName)
    {
        this.customerName = customerName;
    }
    
    public void setTitle(String title)
    {
        this.title = title;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public void setLocation(String location)
    {
        this.location = location;
    }
    
    public void setContact(String contact)
    {
        this.contact = contact;
    }
    
    public void setType(String type)
    {
        this.type = type;
    }
    
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    public void setStart(LocalDateTime start) throws InvalidAppointmentTimeException
    {
        this.start = start;
        Month month = start.getMonth();
        int day = start.getDayOfMonth();
        int year = start.getYear();
        int hour = start.getHour();
        int minute = start.getMinute();
        int second = start.getSecond();
        
        if (hour < 8 || hour >= 17)
        {
            throw new InvalidAppointmentTimeException();
        }
            
        this.appointmentDate = LocalDate.of(year, month, day);
        this.startTime = LocalTime.of(hour, minute, second);
    }
    
    public void setStartTime(LocalTime startTime)
    {
        this.startTime = startTime;
    }
    
    public void setEnd(LocalDateTime end) throws InvalidAppointmentTimeException
    {
        this.end = end;
        int hour = end.getHour();
        int minute = end.getMinute();
        int second = end.getSecond();
        
        if (hour < 8 || hour > 17)
        {
            throw new InvalidAppointmentTimeException();
        }
        
        this.endTime = LocalTime.of(hour, minute, second);
    }
    
    public void setEndTime(LocalTime endTime)
    {
        this.endTime = endTime;
    }
 
    @Override
    public int getId()
    {
        return id;
    }
    
    public int getCustomerId()
    {
        return customerId;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }
    
    public String getTitle()
    {
        return title;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public String getLocation()
    {
        return location;
    }
    
    public String getContact()
    {
        return contact;
    }
    
    public String getType()
    {
        return type;
    }
    
    public String getUrl()
    {
        return url;
    }
    
    public LocalDateTime getStart()
    {
        return start;
    }
    
    public LocalTime getStartTime()
    {
        return startTime;
    }
    
    public LocalDateTime getEnd()
    {
        return end;
    }
    
    public LocalTime getEndTime()
    {
        return endTime;
    }
    
    public LocalDate getAppointmentDate()
    {
        return appointmentDate;
    }
}