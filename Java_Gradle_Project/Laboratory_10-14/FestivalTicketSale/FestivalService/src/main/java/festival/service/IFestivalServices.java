package festival.service;

import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.service.Exceptions.FestivalException;

import java.time.LocalDateTime;

public interface IFestivalServices {
    SpectacolDTO[] getSpectacole() throws FestivalException;
    String[] getArtisti() throws FestivalException;
    SpectacolDTO addBilet(String nume_cump,long id_spectacol,int nrLocuri,IFestivalObserver observer) throws FestivalException;
    Angajat logIn(String username,String password,IFestivalObserver employee) throws FestivalException;
    boolean logOut(Angajat angajat) throws FestivalException;
    SpectacolDTO[] filterSpectacole(LocalDateTime date, String Artist,boolean filteredByDate) throws FestivalException;
}
