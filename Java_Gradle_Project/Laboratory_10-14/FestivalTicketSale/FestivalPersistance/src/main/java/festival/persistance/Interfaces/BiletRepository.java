package festival.persistance.Interfaces;

import festival.model.Bilet;
import festival.model.Spectacol;
import festival.persistance.Interfaces.Repository;

public interface BiletRepository extends Repository<Long, Bilet> {
    Iterable<Bilet> findBySpectacol(Spectacol spectacol);
    Iterable<Bilet> findByName(String name);
    Bilet findBySeries(String series);
}
