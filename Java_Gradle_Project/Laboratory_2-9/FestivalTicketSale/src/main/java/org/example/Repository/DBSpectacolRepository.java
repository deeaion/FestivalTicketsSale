package org.example.Repository;

import org.example.Model.Spectacol;
import org.example.Model.Validators.ModelValidators.SpectacolValidator;
import org.example.Repository.Exceptions.RepositoryException;
import org.example.Repository.Interfaces.SpectacolRepository;
import org.example.Repository.Utils.DBUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class DBSpectacolRepository implements SpectacolRepository {

    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private SpectacolValidator spectacolValidator;
    public DBSpectacolRepository(Properties properties, SpectacolValidator spectacolValidator)
    {
        logger.info("Initializing SpectacolRepository Database with properties");
        dbUtils=new DBUtils(properties);
        this.spectacolValidator=spectacolValidator;
    }
    @Override
    public Spectacol findOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        if(id==null)
        {
            throw new RepositoryException("Cannot find a Spectacol if id is null!");
        }
        Spectacol spectacol=null;

        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Spectacol where id_spectacol=?"))
        {
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    spectacol=getSpectacolFromResultSet(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(spectacol);
        return spectacol;
    }

    private Spectacol getSpectacolFromResultSet(ResultSet resultSet) throws SQLException {
        Long id_spectacol=resultSet.getLong("id_spectacol");
        String locatie=resultSet.getString("locatie_spectacol");
        LocalDateTime data= resultSet.getTimestamp("data_spectacol").toLocalDateTime();
        int nr_locuri_disp=resultSet.getInt("nr_locuri_disponibile_spectacol");
        int nr_locuri_vandute=resultSet.getInt("nr_locuri_vandute_spectacol");
        String artist=resultSet.getString("artist");
        Spectacol spectacol=new Spectacol(locatie,data,nr_locuri_disp,nr_locuri_vandute,artist);
        spectacol.setId(id_spectacol);
        return spectacol;
    }

    @Override
    public Iterable<Spectacol> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Spectacol"))
        {
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Spectacol spectacol=getSpectacolFromResultSet(resultSet);
                    spectacols.add(spectacol);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(spectacols);
        return spectacols;
    }

    @Override
    public Spectacol save(Spectacol entity) {
        logger.traceEntry("Saving task {} ",entity);
        if(entity==null)
        {
            String m="Cannot save spectacol if entity is null!\n";
            logger.traceExit("Sent error from repo {}",m);
            throw new RepositoryException(m);

        }
        int result=0;
        Connection con=dbUtils.getConnection();
        spectacolValidator.validate(entity);
        try (PreparedStatement preparedStatement=con.prepareStatement("insert into Spectacol (locatie_spectacol,data_spectacol,nr_locuri_disponibile_spectacol,nr_locuri_vandute_spectacol,artist) values (?,?,?,?,?)"))
        {
            preparedStatement.setString(1,entity.getLocatie());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getData()));
            preparedStatement.setInt(3,entity.getNumar_locuri_disponibile());
            preparedStatement.setInt(4, entity.getNumar_locuri_vandute());
            preparedStatement.setString(5,entity.getArtist());
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
    public Spectacol delete(Long id) {
        logger.traceEntry("Deleting spectacol with id {}",id);
        Connection con=dbUtils.getConnection();
        if(id==null)
        {
            String message="Cannot find a Spectacol if id is null!";
            logger.traceExit(message);
            throw new RepositoryException(message);
        }
        Spectacol spectacol=findOne(id);
        int result=0;
        if(spectacol==null)
        {
            String message="Cannot find Spectacol with id !";
            logger.traceExit(message);
            throw new RepositoryException("Spectacol with id "+id+" does not exist!");
        }
        try (PreparedStatement preparedStatement=con.prepareStatement("DELETE from Spectacol where id_spectacol=?"))
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
            logger.traceExit("Failed deletion for Spectacol with id {}",id);
            return null;
        }
        logger.traceExit(spectacol);
        return spectacol;
    }

    @Override
    public Spectacol update(Long id,Spectacol entity) {
        logger.traceEntry("Update task {} ",entity);
        if(entity==null || id==null)
        {
            String m="Cannot update spectacol if entity is null!\n";
            logger.traceExit("Sent error from repo {}",m);
            throw new RepositoryException(m);

        }
        int result=0;
        Connection con=dbUtils.getConnection();
        spectacolValidator.validate(entity);
        try (PreparedStatement preparedStatement=con.prepareStatement("update Spectacol set locatie_spectacol=?, data_spectacol=?, nr_locuri_disponibile_spectacol=?, nr_locuri_vandute_spectacol=?,artist=? where id_spectacol=?"))
        {
            preparedStatement.setString(1,entity.getLocatie());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(entity.getData()));
            preparedStatement.setInt(3,entity.getNumar_locuri_disponibile());
            preparedStatement.setInt(4, entity.getNumar_locuri_vandute());
            preparedStatement.setString(5,entity.getArtist());
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

    @Override
    public Iterable<Spectacol> findByArtist(String artist) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Spectacol where artist=?"))
        {
            preparedStatement.setString(1,artist);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Spectacol spectacol=getSpectacolFromResultSet(resultSet);
                    spectacols.add(spectacol);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(spectacols);
        return spectacols;
    }

    @Override
    public Iterable<Spectacol> findByDate(LocalDateTime data) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Spectacol where data_spectacol >= ? AND data_spectacol < ?"))
        {
            // Convert LocalDateTime to a long integer timestamp
            long startOfDayTimestamp = data.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long endOfDayTimestamp = data.plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            // Set the timestamps in the prepared statement
            preparedStatement.setLong(1, startOfDayTimestamp);
            preparedStatement.setLong(2, endOfDayTimestamp);

            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Spectacol spectacol=getSpectacolFromResultSet(resultSet);
                    spectacols.add(spectacol);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(spectacols);
        return spectacols;
    }

    @Override
    public Iterable<Spectacol> findByDateandArtist(String artist, LocalDateTime date) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Spectacol> spectacols=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from Spectacol where strftime('%Y-%m-%d', data_spectacol)=? and artist=?"))
        {
            // Convert LocalDateTime to LocalDate
            LocalDate localDate = date.toLocalDate();

            // Set the date in the prepared statement
            preparedStatement.setString(1, localDate.toString());
            preparedStatement.setString(2,artist);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Spectacol spectacol=getSpectacolFromResultSet(resultSet);
                    spectacols.add(spectacol);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(spectacols);
        return spectacols;
    }

    @Override
    public Iterable<String> getArtisti() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<String> artist=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT  DISTINCT  Artist from Spectacol"))
        {
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    artist.add(resultSet.getString("artist"));
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(artist);
        return artist;
    }
}
