package org.example.Repository.Interfaces;

import org.example.Model.Bilet;
import org.example.Model.Spectacol;

public interface BiletRepository extends Repository<Long, Bilet>{
    Iterable<Bilet> findBySpectacol(Spectacol spectacol);
    Iterable<Bilet> findByName(String name);
    Bilet findBySeries(String series);
}
