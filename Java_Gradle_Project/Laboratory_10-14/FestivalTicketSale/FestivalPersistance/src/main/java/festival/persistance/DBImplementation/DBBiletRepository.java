package festival.persistance.DBImplementation;

import festival.model.Bilet;
import festival.model.Spectacol;
import festival.persistance.Exceptions.RepositoryException;
import festival.persistance.Interfaces.BiletRepository;
import festival.persistance.Utils.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBBiletRepository implements BiletRepository {
    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    public DBBiletRepository(Properties properties)
    {
        logger.info("Initializing BiletRepository Database with properties");
        dbUtils=new DBUtils(properties);
    }
    @Override
    public Iterable<Bilet> findBySpectacol(Spectacol spectacol) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Bilet> bilets=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from BiletWithSpectacol where id_spectacol=?"))
        {
            preparedStatement.setLong(1,spectacol.getId());
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Bilet bilet=getBiletFromResultSet(resultSet);
                    bilets.add(bilet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }

        logger.traceExit(bilets);
        return bilets;
    }

    @Override
    public Iterable<Bilet> findByName(String name) {
        return null;
    }

    @Override
    public Bilet findBySeries(String series) {
        return null;
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
    private Bilet getBiletFromResultSet(ResultSet resultSet) throws SQLException {
        Long id_bilet=resultSet.getLong("id_bilet");
        String nume_cump=resultSet.getString("nume_cumparator_bilet");
        Spectacol spectacol=getSpectacolFromResultSet(resultSet);
        int nr_locuri_bilet=resultSet.getInt("nr_locuri_bilet");
        String serie=resultSet.getString("serie_bilet");
        Bilet bilet=new Bilet(nume_cump,serie,spectacol,nr_locuri_bilet);
        bilet.setId(id_bilet);
        return bilet;
    }
    @Override
    public Bilet findOne(Long id) {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        if(id==null)
        {
            throw new RepositoryException("Cannot find Bilet if id is null!");
        }
        Bilet bilet=null;

        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from BiletWithSpectacol where id_bilet=?"))
        {
            preparedStatement.setLong(1,id);
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                if(resultSet.next())
                {
                    bilet=getBiletFromResultSet(resultSet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(bilet);
        return bilet;
    }

    @Override
    public Iterable<Bilet> findAll() {
        logger.traceEntry();
        Connection con=dbUtils.getConnection();
        List<Bilet> bilets=new ArrayList<>();
        try (PreparedStatement preparedStatement=con.prepareStatement("SELECT * from BiletWithSpectacol"))
        {
            try(ResultSet resultSet=preparedStatement.executeQuery())
            {
                while(resultSet.next())
                {
                    Bilet bilet=getBiletFromResultSet(resultSet);
                    bilets.add(bilet);
                }
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        logger.traceExit(bilets);
        return bilets;
    }

    @Override
    public Bilet save(Bilet entity) {
        logger.traceEntry("Saving task {} ",entity);
        if(entity==null)
        {
            String m="Cannot save bilet if entity is null!\n";
            logger.traceExit("Sent error from repo {}",m);
            throw new RepositoryException(m);

        }
        int result=0;
        Connection con=dbUtils.getConnection();
        try (PreparedStatement preparedStatement=con.prepareStatement("insert into Bilet (nume_cumparator_bilet,id_spectacol,nr_locuri_bilet,serie_bilet) values (?,?,?,?)"))
        {
            preparedStatement.setString(1,entity.getNume_cumparator());
            preparedStatement.setLong(2, entity.getSpectacol().getId());
            preparedStatement.setInt(3,entity.getNr_locuri());
            preparedStatement.setString(4, entity.getSerie());
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
        con=dbUtils.getConnection();
        long id=0;
        try(PreparedStatement preparedStatement= con.prepareStatement("select max(id_bilet) as max from Bilet"))
        {
            try (ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next())
                {
                    id=resultSet.getInt("max");
                }

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        entity.setId(id);
        return entity;
    }

    @Override
    public Bilet delete(Long aLong) {
        return null;
    }

    @Override
    public Bilet update(Long id,Bilet entity) {

        logger.traceEntry("Update task {} ",entity);
        if(entity==null || id==null)
        {
            String m="Cannot update bilet if entity is null!\n";
            logger.traceExit("Sent error from repo {}",m);
            throw new RepositoryException(m);

        }
        int result=0;
        Connection con=dbUtils.getConnection();
        try (PreparedStatement preparedStatement=con.prepareStatement("update Bilet set serie_bilet=? where id_bilet=?"))
        {
            preparedStatement.setString(1,entity.getSerie());
            preparedStatement.setLong(2,id);
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
