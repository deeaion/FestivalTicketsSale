package festival.persistance.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils {
    private Properties jdbcProps;
    private static final Logger logger= LogManager.getLogger();

    public DBUtils(Properties properties)
    {
        jdbcProps=properties;
    }
    private Connection instance=null;
    private Connection getNewConnection()
    {

        logger.traceEntry();
        String driver=jdbcProps.getProperty("festival.jdbc.driver");
        String url=jdbcProps.getProperty("festival.jdbc.url");
        String user=jdbcProps.getProperty("festival.jdbc.user");
        String pass=jdbcProps.getProperty("festival.jdbc.password");

            Path basePath = Paths.get("../ticket_sales.db"); // baza poate fi configurabilă

        try
        {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e)
        {
            logger.error(e);
            System.out.println("Error loading driver "+e);
        }
        logger.info("trying to connect to the database {}",url);
        logger.info("user {}",user);
        logger.info("password {}",pass);
        Connection connection=null;
        try
        {
            if (user!=null & pass!=null)
            {
                connection= DriverManager.getConnection(url,user,pass);
            }
            else
            {
                connection=DriverManager.getConnection(url);
            }
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error: "+e);
        }
        return connection;
    }
    public Connection getConnection()
    {
        logger.traceEntry();
        try
        {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();
        }
        catch (SQLException e)
        {
            logger.error(e);
            System.out.println("Error getting connection "+e);

        }
        logger.traceExit(instance);
        return instance;
    }
}
