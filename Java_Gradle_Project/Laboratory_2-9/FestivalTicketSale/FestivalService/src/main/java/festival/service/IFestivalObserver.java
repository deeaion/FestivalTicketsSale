package festival.service;

import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.service.Exceptions.FestivalException;

public interface IFestivalObserver {
    void recievedSellingOfTicket(SpectacolDTO spectacol, int nrLocuri, String nume) throws FestivalException;
    void userLoggedIn(String username,String password) throws FestivalException;
    void userLoggedOut(String username,String password) throws FestivalException;
    void recievedSpectacole(SpectacolDTO[] spectacole) throws FestivalException;

}
