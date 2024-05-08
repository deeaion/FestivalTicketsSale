using System;
using System.Collections.Generic;
using FestivalTicketSale.Model;

namespace FestivalTicketSale.Repository.Interfaces;

public interface ISpectacolRepository : IRepository<long,Spectacol>
{
    IEnumerable<Spectacol> FindByArtist(string artist);
    IEnumerable<Spectacol> FindByDate(DateTime date);
    IEnumerable<string> GetArtisti();
    IEnumerable<Spectacol> FindByDateandArtist(string artist, DateTime dateTime);
}