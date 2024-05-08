using FestivalTicketSale.Model;

namespace FestivalTicketSale.Repository.Interfaces;

public interface IAngajatRepository : IRepository<long,Angajat>
{
    Angajat FindByEmail(string email);
    Angajat FindByUsername(string username);
}