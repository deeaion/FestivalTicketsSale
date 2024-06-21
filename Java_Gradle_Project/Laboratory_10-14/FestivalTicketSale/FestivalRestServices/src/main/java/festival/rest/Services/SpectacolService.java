package festival.rest.Services;

import festival.model.Spectacol;
import festival.persistance.DBImplementation.DBBiletRepository;
import festival.persistance.DBImplementation.DBSpectacolRepository;
import festival.persistance.Interfaces.SpectacolRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class SpectacolService implements ISpectacolService{
    @Autowired
    private DBSpectacolRepository repositorySpectacol;
    @Autowired
    private DBBiletRepository repositoryBilet;
    private Logger logger = LogManager.getLogger();

    @Override
    public Spectacol addSpectacol( String data, String locatie, int nrLocuriDisponibile, int nrLocuriVandute, String artist) {
        logger.traceEntry();
        try
        {
            Spectacol spectacol=new Spectacol(locatie, LocalDateTime.parse(data),nrLocuriDisponibile,nrLocuriVandute,artist);
            return repositorySpectacol.save(spectacol);}
        catch (Exception e)
        {
            logger.error(e);
            System.out.println("Error adding spectacol "+e);
        }

        return null;

    }

    @Override
    public Spectacol updateSpectacol(Long id, String data, String locatie, int nrLocuriDisponibile, int nrLocuriVandute, String artist) {
        //caut spectacolul
        logger.traceEntry();
        Spectacol spectacol=repositorySpectacol.findOne(id);
        if (spectacol!=null)
        {
            try
            {
                spectacol.setData(LocalDateTime.parse(data));
                spectacol.setLocatie(locatie);
                spectacol.setNumar_locuri_disponibile(nrLocuriDisponibile);
                spectacol.setNumar_locuri_vandute(nrLocuriVandute);
                spectacol.setArtist(artist);
                repositorySpectacol.update(id,spectacol);
                return spectacol;}
            catch (Exception e)
            {
                logger.error(e);
                System.out.println("Error updating spectacol "+e);
            }
        }
        else
        {
            logger.error("Spectacolul nu exista");
            System.out.println("Spectacolul nu exista");
        }
        return null;
    }

    @Override
    public Spectacol deleteSpectacol(Long id) {
        logger.traceEntry();
        Spectacol spectacol=repositorySpectacol.findOne(id);
        if (spectacol!=null)
        {
            try
            {
                repositorySpectacol.delete(id);
                return spectacol;}
            catch (Exception e)
            {
                logger.error(e);
                System.out.println("Error deleting spectacol "+e);
            }
        }
        else
        {
            logger.error("Spectacolul nu exista");
            System.out.println("Spectacolul nu exista");
        }
        return null;
    }

    @Override
    public List<Spectacol> getAllSpectacole() {
        logger.traceEntry();
        return (List<Spectacol>) repositorySpectacol.findAll();
    }

    @Override
    public Spectacol getSpectacol(Long id) {
        logger.traceEntry();
        return repositorySpectacol.findOne(id);
    }
}
