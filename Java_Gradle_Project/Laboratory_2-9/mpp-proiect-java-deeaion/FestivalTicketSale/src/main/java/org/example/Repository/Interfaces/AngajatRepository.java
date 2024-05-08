package org.example.Repository.Interfaces;

import org.example.Model.Angajat;

public interface AngajatRepository extends Repository<Long, Angajat> {
    Angajat findByEmail(String email);
    Angajat findByUsername(String username);
}
