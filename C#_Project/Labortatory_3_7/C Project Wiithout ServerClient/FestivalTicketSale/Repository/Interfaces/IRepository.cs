using System.Collections.Generic;
using FestivalTicketSale.Model;

namespace FestivalTicketSale.Repository;

public interface IRepository<TId,TE> where TE : Entity<TId>
{
  
    IEnumerable<TE> FindAll();
    TE FindOne(TId iD);
    TE Save(TE entity);
    TE Delete(TId id);
   TE Update(TId id,TE entity);

}