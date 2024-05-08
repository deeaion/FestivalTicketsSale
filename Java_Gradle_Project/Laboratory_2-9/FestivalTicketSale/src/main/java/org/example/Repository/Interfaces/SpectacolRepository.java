package org.example.Repository.Interfaces;

import org.example.Model.Spectacol;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SpectacolRepository extends Repository<Long, Spectacol> {
    Iterable<Spectacol> findByArtist(String artist);

    Iterable<Spectacol> findByDate(LocalDateTime date);
    Iterable<Spectacol> findByDateandArtist(String artist,LocalDateTime date);
    Iterable<String> getArtisti();
}
