package org.example.Service;

import org.example.Model.Angajat;
import org.example.Model.Bilet;
import org.example.Model.Spectacol;
import org.example.Repository.Exceptions.RepositoryException;
import org.example.Repository.Interfaces.AngajatRepository;
import org.example.Repository.Interfaces.BiletRepository;
import org.example.Repository.Interfaces.Repository;
import org.example.Repository.Interfaces.SpectacolRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class FestivalServiceImplementation implements FestivalService{
    AngajatRepository angajatRepository;
    BiletRepository biletRepository;
    SpectacolRepository spectacolRepository;

    public FestivalServiceImplementation(AngajatRepository angajatRepository, BiletRepository biletRepository, SpectacolRepository spectacolRepository) {
        this.angajatRepository = angajatRepository;
        this.biletRepository = biletRepository;
        this.spectacolRepository = spectacolRepository;
    }


    @Override
    public Iterable<Spectacol> getSpectacole() {
        return spectacolRepository.findAll();
    }

    @Override
    public Iterable<Angajat> getAngajati() {
        return angajatRepository.findAll();
    }

    @Override
    public Iterable<Bilet> getBilete() {
        return biletRepository.findAll();
    }

    @Override
    public Iterable<Bilet> getBileteSpectacol(Spectacol spectacol) {
        return biletRepository.findBySpectacol(spectacol);
    }

    @Override
    public Iterable<Spectacol> getSpectacoleByDay(LocalDateTime date) {
        return spectacolRepository.findByDate(date);
    }

    @Override
    public Iterable<String> getArtisti() {
        return spectacolRepository.getArtisti();
    }

    @Override
    public Iterable<Spectacol> getSpectacoleArtist(String artist) {
        return spectacolRepository.findByArtist(artist);
    }

    @Override
    public boolean addSpectacol(String locatie, LocalDateTime data, int nrLocuriDisp, int nrLocuriVandute, String artist) {
        Spectacol spectacol=new Spectacol(locatie,data,nrLocuriDisp,nrLocuriVandute,artist);
        spectacolRepository.save(spectacol);
        return true;

    }

    @Override
    public boolean addAngajat(String nume, String prenume, String email, String username, String password) {
        Angajat angajat=new Angajat(nume,prenume,email,username,password);
        angajatRepository.save(angajat);
        return true;
    }
    private String getInitials(String buyer)
    {
        String[] names=buyer.split(" ");
        String initials="";
        for(String name:names)
        {
            initials+=name.charAt(0);
        }
        return initials;
    }
    private int getNumberOfDigits(long number)
    {
    int count=0;
    while(number>0){
        number/=10;
        count++;
    }
    return count;
    }
    private String getNumberAsString(long number)
    {
        int nrDigits=getNumberOfDigits(number);
        String nr="";
        for(int i=0;i<4-nrDigits;i++)
        {
            nr+="0";
        }
        nr+=Long.toString(number);
        return nr;
    }
    private String generateSeries(long id,String buyer,LocalDateTime date,Long id_spectacol)
    {
        String series="XY";
        series+=Long.toString(id_spectacol);
        series+=getInitials(buyer);
        series+=getNumberAsString(id);
        series+=Integer.toString(date.getDayOfMonth());
        series+=Integer.toString(date.getMonthValue());
        return series;
    }

    @Override
    public String addBilet(String nume_cump, Spectacol spectacol, int nrLocuri) {
        spectacol.setNumar_locuri_vandute(spectacol.getNumar_locuri_vandute()+nrLocuri);
        spectacol.setNumar_locuri_disponibile(spectacol.getNumar_locuri_disponibile()-nrLocuri);
        spectacolRepository.update(spectacol.getId(),spectacol);
        Bilet bilet=new Bilet(nume_cump,"dummy",spectacol,nrLocuri);
        Bilet biletWithId=biletRepository.save(bilet);
        String series=generateSeries(biletWithId.getId(),nume_cump,spectacol.getData(),spectacol.getId());
        biletWithId.setSerie(series);
        biletRepository.update(biletWithId.getId(),biletWithId);
        return series;
    }

    @Override
    public boolean verifyLogInInformation(String username, String password) {
        Angajat angajat=angajatRepository.findByEmail(username);
        if(angajat==null)
        {
            angajat=angajatRepository.findByUsername(username);
            if(angajat==null)
            {
                throw new RepositoryException("Angajat nu exista cu username/email respectiv!");
//                return false;
            }
        }
        if(Objects.equals(angajat.getPassword(), password))
        {
            return true;
        }
        return false;
    }

    @Override
    public Iterable<Spectacol> filterSpectacole(LocalDateTime date, String Artist) {
        if(date==null&&(Artist==null||Artist.equals("None")))
        {
            return spectacolRepository.findAll();
        }
        if(Objects.equals(Artist, "None") || Artist==null)
        {
            return spectacolRepository.findByDate(date);
        }
        if(date==null)
        {
            return spectacolRepository.findByArtist(Artist);
        }
        return spectacolRepository.findByDateandArtist(Artist,date);
    }
}
