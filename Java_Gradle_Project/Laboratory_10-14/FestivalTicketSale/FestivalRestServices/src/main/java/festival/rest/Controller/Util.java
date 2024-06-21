package festival.rest.Controller;

import festival.model.Spectacol;

import java.util.ArrayList;
import java.util.List;

public class Util {
    public static SpectacolDTOR convertSpectacolToSpectacolDTOR(Spectacol spectacol) {
        SpectacolDTOR spectacolDTOR = new SpectacolDTOR();
        spectacolDTOR.setData(String.valueOf(spectacol.getData()));
        spectacolDTOR.setLocatie(spectacol.getLocatie());
        spectacolDTOR.setNrLocuriDisponibile(spectacol.getNumar_locuri_disponibile());
        spectacolDTOR.setNrLocuriVandute(spectacol.getNumar_locuri_vandute());
        spectacolDTOR.setArtist(spectacol.getArtist());
        spectacolDTOR.setId(spectacol.getId());
        return spectacolDTOR;
    }
    public static List<SpectacolDTOR> convertSpectacoleToSpectacoleDTOR(List<Spectacol> spectacole) {
        List<SpectacolDTOR> spectacolDTORS = new ArrayList<>();
        for (Spectacol spectacol : spectacole) {
            spectacolDTORS.add(convertSpectacolToSpectacolDTOR(spectacol));
        }
        return spectacolDTORS;
    }
}
