package org.example.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.Model.Angajat;
import org.example.Model.Validators.ModelValidators.AngajatValidator;
import org.example.Repository.Exceptions.RepositoryException;
import org.example.Repository.Interfaces.AngajatRepository;
import org.example.Repository.Utils.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBAngajatRepository implements AngajatRepository {

    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();

    public DBAngajatRepository(Properties properties, AngajatValidator angajatValidator) {
        logger.info("Initializing AngajatRepositoryDatabase with the properties given");
        dbUtils=new DBUtils(properties);
    }

    @Override
    public Angajat findByEmail(String email) {

        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        if(email==null)
        {
            throw new RepositoryException("Cannot find an Angajat if id is null!");
        }
        Angajat angajat=null;
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Angajat  where email_angajat=?"))
        {
            preparedStatement.setString(1,email);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    angajat=getAngajatFromResultSet(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(angajat);
        return angajat;
    }

    @Override
    public Angajat findByUsername(String username) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        if(username==null)
        {
            throw new RepositoryException("Cannot find an Angajat if id is null!");
        }
        Angajat angajat=null;
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Angajat  where username=?"))
        {
            preparedStatement.setString(1,username);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    angajat=getAngajatFromResultSet(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(angajat);
        return angajat;
    }

    @Override
    public Angajat findOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        if(id==null)
        {
            throw new RepositoryException("Cannot find an Angajat if id is null!");
        }
        Angajat angajat=null;

        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Angajat where id_angajat=?"))
        {
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                   angajat=getAngajatFromResultSet(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(angajat);
        return angajat;
    }
    public Angajat getAngajatFromResultSet(ResultSet resultSet) throws SQLException
    {
        Long id_angajat=resultSet.getLong("id_angajat");
        String nume_angajat=resultSet.getString("nume_angajat");
        String prenume_angajat=resultSet.getString("prenume_angajat");
        String username_angajat=resultSet.getString("username");
        String email_angajat=resultSet.getString("email_angajat");
        String password=resultSet.getString("password");
        Angajat angajat=new Angajat(nume_angajat,prenume_angajat,email_angajat,username_angajat,password);
        angajat.setId(id_angajat);
        return angajat;
    }
    @Override
    public Iterable<Angajat> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Angajat> angajatList=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Angajat"))
        {
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Angajat angajat=getAngajatFromResultSet(resultSet);
                    angajatList.add(angajat);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(angajatList);
        return angajatList;
    }

    @Override
    public Angajat save(Angajat entity) {
        logger.traceEntry("Saving task {} ",entity);
        if(entity==null)
        {
            String m="Cannot save angajat if entity is null!\n";
            logger.traceExit("Sent error from repo {}",m);
            throw new RepositoryException(m);

        }
        int result=0;
        Connection con=dbUtils.getConnection();
        try (PreparedStatement preparedStatement=con.prepareStatement("insert into Angajat (prenume_angajat,nume_angajat,email_angajat,username,password) VALUES (?,?,?,?,?)"))
        {
           preparedStatement.setString(1,entity.getPrenume());
           preparedStatement.setString(2,entity.getNume());
           preparedStatement.setString(3,entity.getEmail());
           preparedStatement.setString(4, entity.getUsername());
           preparedStatement.setString(5,entity.getPassword());
           result=preparedStatement.executeUpdate();
           logger.trace("Saved {} instances ",result);
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error saving element in  DB"+ e);
        }
        logger.traceExit();
        if(result==0)
        {
            return null;
        }
        return entity;
    }

    @Override
    public Angajat delete(Long id) {
        logger.traceEntry("Deleting angajat with id {}",id);
        Connection con=dbUtils.getConnection();
        if(id==null)
        {
            String message="Cannot find an Angajat if id is null!";
            logger.traceExit(message);
            throw new RepositoryException(message);
        }
        Angajat angajat=findOne(id);
        int result=0;
        if(angajat==null)
        {
            String message="Cannot find Angajat with id !";
            logger.traceExit(message);
            throw new RepositoryException("Angajat with id "+id+" does not exist!");
        }
        try (PreparedStatement preparedStatement=con.prepareStatement("DELETE from Angajat where id_angajat=?"))
        {
            preparedStatement.setLong(1,id);
            result=preparedStatement.executeUpdate();

        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }


        if(result==0)
        {
            logger.traceExit("Failed deletion for Angajat with id {}",id);
            return null;
        }
        logger.traceExit(angajat);
        return angajat;
    }

    @Override
    public Angajat update(Long id,Angajat entity) {

        logger.traceEntry("Update task {} ",entity);
        if(entity==null || id==null)
        {
            String m="Cannot update angajat if entity is null!\n";
            logger.traceExit("Sent error from repo {}",m);
            throw new RepositoryException(m);

        }
        int result=0;
        Connection con=dbUtils.getConnection();
        try (PreparedStatement preparedStatement=con.prepareStatement("update Angajat set prenume_angajat=?,nume_angajat=?,email_angajat=?,username=?,password=? where id_angajat=?"))
        {
            preparedStatement.setString(1,entity.getPrenume());
            preparedStatement.setString(2,entity.getNume());
            preparedStatement.setString(3,entity.getEmail());
            preparedStatement.setString(4, entity.getUsername());
            preparedStatement.setString(5,entity.getPassword());
            preparedStatement.setLong(6,id);
            result=preparedStatement.executeUpdate();
            logger.trace("Update {} instances ",result);
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error updating element in  DB"+ e);
        }

        if(result==0)
        {
            logger.traceExit("Update did not go well! {}",entity);
            return null;
        }
        logger.traceExit();
        return entity;
    }
}
