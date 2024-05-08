using System;
using System.Collections.Generic;
using FestivalTicketSale.Model;
using FestivalTicketSale.Repository.Exceptions;
using FestivalTicketSale.Repository.Interfaces;

namespace FestivalTicketSale.Service;

public class FestivalService :IFestivalService
{
    IAngajatRepository angajatRepository;
    IBiletRepository biletRepository;
    ISpectacolRepository spectacolRepository;
    public FestivalService(IAngajatRepository angajatRepository, IBiletRepository biletRepository, ISpectacolRepository spectacolRepository) {
        this.angajatRepository = angajatRepository;
        this.biletRepository = biletRepository;
        this.spectacolRepository = spectacolRepository;
    }
    public IEnumerable<Spectacol> GetSpectacole()
    {
        return spectacolRepository.FindAll();
    }

    public IEnumerable<Angajat> GetAngajati()
    {
        return angajatRepository.FindAll();
    }

    public IEnumerable<Spectacol> GetSpectacoleByDay(DateTime date)
    {
        return spectacolRepository.FindByDate(date);
    }

    public IEnumerable<string> GetArtisti()
    {
        return spectacolRepository.GetArtisti();
    }

    public IEnumerable<Spectacol> GetSpectacoleArtist(string artist)
    {
        return spectacolRepository.FindByArtist(artist);
    }

    public string AddBilet(string numeCump, Spectacol spectacol, int nrLocuri)
    {
        spectacol.NumarLocuriVandute += nrLocuri;
        spectacol.NumarLocuriDisponibile -= nrLocuri;
        spectacolRepository.Update(spectacol.Id, spectacol);
        Bilet bilet = new Bilet(numeCump, "dummy", spectacol, nrLocuri);
        Bilet biletWithId = biletRepository.Save(bilet);
        string series = GenerateSeries(biletWithId.Id, numeCump, spectacol.Data, spectacol.Id);
        biletWithId.Serie = series;
        biletRepository.Update(biletWithId.Id, biletWithId);
        return series;
    }

    private string GetInitials(string buyer)
    {
        string[] names = buyer.Split(' ');
        string initials = "";
        foreach (string name in names)
        {
            initials += name[0];
        }
        return initials;
    }

    private int GetNumberOfDigits(long number)
    {
        int count = 0;
        while (number > 0)
        {
            number /= 10;
            count++;
        }
        return count;
    }

    private string GetNumberAsString(long number)
    {
        int nrDigits = GetNumberOfDigits(number);
        string nr = "";
        for (int i = 0; i < 4 - nrDigits; i++)
        {
            nr += "0";
        }
        nr += number.ToString();
        return nr;
    }

    private string GenerateSeries(long id, string buyer, DateTime date, long id_spectacol)
    {
        string series = "XY";
        series += id_spectacol.ToString();
        series += GetInitials(buyer);
        series += GetNumberAsString(id);
        series += date.Day.ToString("D2");
        series += date.Month.ToString("D2");
        return series;
    }



    public bool VerifyLogInInformation(string username, string password)
    {
        Angajat angajat=angajatRepository.FindByEmail(username);
        if(angajat==null)
        {
            angajat=angajatRepository.FindByUsername(username);
            if(angajat==null)
            {
                throw new RepositoryException("Angajat nu exista cu username/email respectiv!");
//                return false;
            }
        }
        if(angajat.Password== password)
        {
            return true;
        }
        return false;
    }
    
    public IEnumerable<Spectacol> FilterSpectacole(DateTime date, string artist, bool filterByDate = false)
    {
        if (!filterByDate && (string.IsNullOrEmpty(artist) || string.Equals(artist, "None", StringComparison.OrdinalIgnoreCase)))
        {
            return spectacolRepository.FindAll();
        }
        if (string.Equals(artist, "None", StringComparison.OrdinalIgnoreCase) || string.IsNullOrEmpty(artist))
        {
            return filterByDate ? spectacolRepository.FindByDate(date) : spectacolRepository.FindByDateandArtist(artist, date);
        }
        if (!filterByDate)
        {
            return spectacolRepository.FindByArtist(artist);
        }
        return spectacolRepository.FindByDateandArtist(artist, date);
    }

}