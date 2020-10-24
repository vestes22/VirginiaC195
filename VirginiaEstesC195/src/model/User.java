package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class User 
{
    private int userId;
    private String username;
    private String password;
    private int active;
    private LocalDate createDate;
    private LocalTime createTime;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;
    
    public User
    (
        int userId,
        String username,
        String password,
        int active,
        LocalDate createDate,
        LocalTime createTime,
        String createdBy,
        LocalDateTime lastUpdate,
        String lastUpdateBy
    )
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createTime = createTime;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public void setUserId(int userId)
    {
        this.userId = userId;
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public void setActive(int active)
    {
        this.active = active;
    }
    
    public void setCreateDate(LocalDate createDate)
    {
        this.createDate = createDate;
    }
    
    public void setCreateTime(LocalTime createTime)
    {
        this.createTime = createTime;
    }
    
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }
    
    public void setLastUpdate(LocalDateTime lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }
    
    public void setLastUpdateBy(String lastUpdateBy)
    {
        this.lastUpdateBy = lastUpdateBy;
    }
    
    public int getUserId()
    {
        return userId;
    }
    
    public String getUsername()
    {
        return username;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public int getActive()
    {
        return active;
    }
    
    public LocalDate getCreateDate()
    {
        return createDate;
    }
    
    public LocalTime getCreateTime()
    {
        return createTime;
    }
    
    public String getCreatedBy()
    {
        return createdBy;
    }
    
    public LocalDateTime getLastUpdate()
    {
        return lastUpdate;
    }
    
    public String getLastUpdateBy()
    {
        return lastUpdateBy;
    }
}
