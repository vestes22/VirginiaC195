package model;

import dao.DataTransferObject;

public class Country implements DataTransferObject
{
    private int id;
    private String country;

    public Country()
    {
        
    }
    
    public Country
    (
        int id,
        String country
    )
    {
        this.id = id;
        this.country = country;
    }
    
    public void setId(int countryId)
    {
        this.id = countryId;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    @Override
    public int getId()
    {
        return id;
    }
    
    public String getCountry()
    {
        return country;
    }
}
