package dao;

import java.sql.Connection;
import javafx.collections.ObservableList;

public abstract class DataAccessObject <T extends DataTransferObject>
{
    protected final Connection connection;
    
    public DataAccessObject(Connection connection)
    {
        super();
        this.connection = connection;
    }
    
    public abstract T findById(int id);
    public abstract ObservableList<T> findAll();
    public abstract T update(T dto);
    public abstract T create(T dto);
    public abstract void delete(int id);
}
