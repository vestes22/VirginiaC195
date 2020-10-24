package model;

import dao.DataTransferObject;

public class Customer implements DataTransferObject 
{
    private int id;
    private String customerName;
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String city;
    private String zip;
    private int countryId;
    private String country;
    private String phone;
    private int active;
    
    public Customer()
    {
        
    }
    
    public Customer
    (
        int id,
        String customerName,
        int addressId,
        String address,
        String address2,
        int cityId,
        String city,
        String zip,
        int countryId,
        String country,
        String phone,
        int active
    )
    {
        this.id = id;
        this.customerName = customerName;
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.city = city;
        this.zip = zip;
        this.countryId = countryId;
        this.country = country;
        this.phone = phone;
        this.active = active;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }

    
    public void setCustomerName(String customerName) 
    {
        this.customerName = customerName;
    }
    
    public void setAddressId(int addressId)
    {
        this.addressId = addressId;
    }
    
    public void setAddress(String address) 
    {
        this.address = address;
    }
    
    public void setAddress2(String address2)
    {
        this.address2 = address2;
    }
    
    public void setCityId(int cityId)
    {
        this.cityId = cityId;
    }
    
    public void setCity(String city)
    {
        this.city = city;
    }
    
    public void setZip(String zip) 
    {
        this.zip = zip;
    }
    
    public void setCountryId(int countryId)
    {
        this.countryId = countryId;
    }
    
    public void setCountry(String country)
    {
        this.country = country;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public void setActive(int active)
    {
        this.active = active;
    }
    
    @Override
    public int getId()
    {
        return id;
    }
    
    public String getCustomerName()
    {
        return customerName;
    }
    
    public int getAddressId()
    {
        return addressId;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public String getAddress2()
    {
        return address2;
    }
    
    public int getCityId()
    {
        return cityId;
    }
    
    public String getCity()
    {
        return city;
    }
        
    public String getZip()
    {
        return zip;
    }
    
    public int getCountryId()
    {
        return countryId;
    }
    
    public String getCountry()
    {
        return country;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public int getActive()
    {
        return active;
    }
           
}

