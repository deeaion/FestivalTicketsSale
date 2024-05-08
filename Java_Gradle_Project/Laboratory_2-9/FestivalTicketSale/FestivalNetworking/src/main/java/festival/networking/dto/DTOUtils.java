package festival.networking.dto;

import festival.model.Angajat;

public class DTOUtils {
    public static Angajat getFromDTO(AngajatDTO dto) {
        return new Angajat("","",dto.getToken(),dto.getToken(),dto.getPass());
    }
    public static AngajatDTO getDTO(Angajat angajat) {
        return new AngajatDTO( angajat.getUsername(), angajat.getPassword());
    }

}
