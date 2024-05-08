package festival.persistance.Interfaces;

import festival.model.Angajat;

public interface AngajatRepository extends Repository<Long, Angajat> {
    Angajat findByEmail(String email);
    Angajat findByUsername(String username);
}
