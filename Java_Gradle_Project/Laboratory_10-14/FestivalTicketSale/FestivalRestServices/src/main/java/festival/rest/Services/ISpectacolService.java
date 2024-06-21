package festival.rest.Services;

import festival.model.Spectacol;

import java.util.List;

public interface ISpectacolService {
     Spectacol addSpectacol(String data, String locatie, int nrLocuriDisponibile, int nrLocuriVandute, String artist);
     Spectacol updateSpectacol(Long id, String data, String locatie, int nrLocuriDisponibile, int nrLocuriVandute, String artist);
     Spectacol deleteSpectacol(Long id);
       List<Spectacol> getAllSpectacole();
         Spectacol getSpectacol(Long id);

}
