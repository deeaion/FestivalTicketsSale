using System;
using System.Collections.Generic;
using FestivalTicketSale.Model;

namespace FestivalTicketSale.Service;

public interface IFestivalService
{
    IEnumerable<Spectacol> GetSpectacole();
    IEnumerable<Angajat> GetAngajati();
    IEnumerable<Spectacol> GetSpectacoleByDay(DateTime date);
    IEnumerable<string> GetArtisti();
    IEnumerable<Spectacol> GetSpectacoleArtist(string artist);
    String AddBilet(String numeCump,Spectacol spectacol,int nrLocuri);
    bool VerifyLogInInformation(string username, string password);
    IEnumerable<Spectacol> FilterSpectacole(DateTime date, string artist,bool filterByDate=false);
}