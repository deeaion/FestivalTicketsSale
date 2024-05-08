package org.example.Service;

import org.example.Model.Angajat;
import org.example.Model.Bilet;
import org.example.Model.Spectacol;

import java.time.LocalDateTime;
import java.lang.Iterable;

public interface FestivalService {
    Iterable<Spectacol> getSpectacole();
    Iterable<Angajat> getAngajati();
    Iterable<Bilet> getBilete();
    Iterable<Bilet> getBileteSpectacol(Spectacol spectacol);
    Iterable<Spectacol> getSpectacoleByDay(LocalDateTime date);
    Iterable<String> getArtisti();
    Iterable<Spectacol> getSpectacoleArtist(String artist);
    boolean addSpectacol(String locatie,LocalDateTime data,int nrLocuriDisp,int nrLocuriVandute,String artist);
    boolean addAngajat(String nume,String prenume,String email,String username,String password);
    String addBilet(String nume_cump,Spectacol spectacol,int nrLocuri);
    boolean verifyLogInInformation(String username,String password);
    Iterable<Spectacol> filterSpectacole(LocalDateTime date,String Artist);


}
