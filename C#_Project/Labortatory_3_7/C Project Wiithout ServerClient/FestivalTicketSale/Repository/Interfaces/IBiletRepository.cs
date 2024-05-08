using System.Collections.Generic;
using FestivalTicketSale.Model;

namespace FestivalTicketSale.Repository.Interfaces;

public interface IBiletRepository : IRepository<long,Bilet>
{
    IEnumerable<Bilet> FindBySpectacol(Spectacol spectacol);
    IEnumerable<Bilet> FindByName(string name);
    Bilet FindBySeries(string series);
}