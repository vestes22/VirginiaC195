package model;

import java.time.LocalDate;
import java.time.ZonedDateTime;


public class Address 
{
    private int addressId;
    private String address;
    private String address2;
    private int cityId;
    private String postalCode;
    private String phone;
    private LocalDate createDate;
    private String createdBy;
    private ZonedDateTime lastUpdate;
    private String lastUpdateBy;
    
    public Address
    (
        int addressId,
        String address,
        String address2,
        int cityId,
        String postalCode,
        String phone,
        LocalDate createDate,
        String createdBy,
        ZonedDateTime lastUpdate,
        String lastUpdateBy
    )
    {
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.postalCode = postalCode;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
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
    
    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    public void setCreateDate(LocalDate createDate)
    {
        this.createDate = createDate;
    }
    
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }
    
    public void setLastUpdate(ZonedDateTime lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }
    
    public void setLastUpdateBy(String lastUpdateBy)
    {
        this.lastUpdateBy = lastUpdateBy;
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
    
    public String getPostalCode()
    {
        return postalCode;
    }
    
    public String getPhone()
    {
        return phone;
    }
    
    public LocalDate getCreateDate()
    {
        return createDate;
    }
    
    public String getCreatedBy()
    {
        return createdBy;
    }
    
    public ZonedDateTime getLastUpdate()
    {
        return lastUpdate;
    }
    
    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }
    
}
