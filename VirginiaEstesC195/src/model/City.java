package model;

import dao.DataTransferObject;

public class City implements DataTransferObject
{
    private int id;
    private String city;
    private int countryId;
    
    public City()
    {
        
    }
    
    public City
    (
        int id,
        String city,
        int countryId
    )
    {
        this.id = id;
        this.city = city;
        this.countryId = countryId;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public void setCountryId(int countryId)
    {
        this.countryId = countryId;
    }
    
    @Override
    public int getId()
    {
        return id;
    }
    
    public String getCity()
    {
        return city;
    }
    
    public int getCountryId()
    {
        return countryId;
    }
}
    

    
