package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Schedule 
{
    /*
    * There are two ObservableLists for appointments: allAppointments contains all appointments in the database.
    * userAppointments is populated only with appointments scheduled for the currently logged in user.
    *
    * All of these ObservableLists are used to populate ListViews and TableViews throughout the program.
    * The Schedule.java class contains both the static ObservableLists and static methods to manipulate the data
    * inside of the lists.
    */
    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> userAppointments = FXCollections.observableArrayList();
    private static User activeUser;
    
    
    /*
    * Methods for allCities ObservableList
    */
    public static void addCity(City city)
    {
        allCities.add(city);
    }
    
    public static ObservableList<City> getAllCities()
    {
        return allCities;
    }
    
    public static int getCityIndex(int id)
    {
        int index = -1;
        for (int i = 0; i < allCities.size(); i++)
        {
            if (id == allCities.get(i).getId())
            {
                index = i;
            }
        }
        return index;
    }
    /*
    * Methods for allCountries ObservableList
    */
    public static void addCountry(Country country)
    {
        allCountries.add(country);
    }
    
    public static ObservableList<Country> getAllCountries()
    {
        return allCountries;
    }
    
    public static int getCountryIndex(int id)
    {
        int index = -1;
        for (int i = 0; i < allCountries.size(); i++)
        {
            if (id == allCountries.get(i).getId())
            {
                index = i;
            }
        }
        return index;
    }
    
    /*
    * Methods for allCustomers ObservableList
    */
        
    public static void addCustomer(Customer customer)
    {
        allCustomers.add(customer);
    }
    
    public static ObservableList<Customer> getAllCustomers()
    {
        return allCustomers;
    }
    
    public static ObservableList<Customer> removeCustomer(Customer customer)
    {
        allCustomers.remove(customer);
        return allCustomers;
    }
    
    public static int getCustomerIndex(int id)
    {
        int index = -1;
        for (int i = 0; i < allCustomers.size(); i++)
        {
            if (id == allCustomers.get(i).getId())
            {
                index = i;
            }
        }
        return index;
    }
    
    public static void updateCustomer(Customer customer, int index) 
    {
        allCustomers.get(index).setCustomerName(customer.getCustomerName());
        allCustomers.get(index).setPhone(customer.getPhone());
        allCustomers.get(index).setAddress(customer.getAddress());
        allCustomers.get(index).setAddress2(customer.getAddress2());
        allCustomers.get(index).setZip(customer.getZip());
        allCustomers.get(index).setCity(customer.getCity());
        allCustomers.get(index).setCityId(customer.getCityId());
        allCustomers.get(index).setCountry(customer.getCountry());
        allCustomers.get(index).setCountryId(customer.getCountryId());
    }
    /*
    * Methods for allAppointments ObservableList
    */
    
    public static int getAppointmentIndex(ObservableList<Appointment> appointment, int id)
    {
        int index = -1;
        for (int i = 0; i < appointment.size(); i++)
        {
            if (id == appointment.get(i).getId())
            {
                index = i;
            }
        }
        return index;
    }
    
    public static void addUserAppointment(Appointment appointment)
    {
        userAppointments.add(appointment);
    }
    
    public static void addAppointment(Appointment appointment)
    {
        allAppointments.add(appointment);
    }
    
    public static ObservableList<Appointment> removeAppointment(Appointment appointment)
    {
        allAppointments.remove(appointment);
        return allAppointments;
    }
    
    public static ObservableList<Appointment> removeUserAppointment(Appointment appointment)
    {
        userAppointments.remove(appointment);
        return allAppointments;
    }
    
    public static ObservableList<Appointment> getAllAppointments()
    {
        return allAppointments;
    }
    
    public static ObservableList<Appointment> getUserAppointments()
    {
        return userAppointments;
    }
    
    public static void clearUserAppointments() 
    {
        userAppointments.clear();
    }
    
    /*
    * Methods for activeUser
    */
    public static void setActiveUser(User user)
    {
        activeUser = user;
    }
    
    public static User getActiveUser()
    {
        return activeUser;
    }
}
