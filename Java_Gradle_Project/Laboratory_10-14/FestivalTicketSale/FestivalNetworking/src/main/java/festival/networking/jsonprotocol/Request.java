package festival.networking.jsonprotocol;

import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.networking.dto.AngajatDTO;
import festival.networking.dto.BiletDTO;
import festival.networking.jsonprotocol.Enums.RequestType;

import java.io.Serializable;
import java.util.Arrays;

public class Request  {
    private RequestType type;
    private AngajatDTO angajatDTO;
    private BiletDTO biletDTO;
    private String artist;
    private String data;
    private Angajat angajat;
    private SpectacolDTO spectacol;
    private SpectacolDTO[] spectacole;
    public Request()   {

    }
    public Request(RequestType type, Angajat angajat) {
        this.type = type;
        this.angajat = angajat;
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public SpectacolDTO getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(SpectacolDTO spectacol) {
        this.spectacol = spectacol;
    }

    public void setSpectacole(SpectacolDTO[] spectacole) {
        this.spectacole = spectacole;
    }

    //request la login
    public Request(RequestType type, AngajatDTO angajatDTO) {
        this.type = type;
        this.angajatDTO = angajatDTO;
    }
    //request la cumparare bilet
    public Request(RequestType type, BiletDTO biletDTO) {
        this.type = type;
        this.biletDTO = biletDTO;
    }
//request la afisare spectacole
    public Request(RequestType type, String locatie, String data, Spectacol[] spectacole) {
        this.type = type;
        this.artist = locatie;
        this.data = data;
    }
//    public Request(RequestType type, Spectacol[] spectacole) {
//        this.type = type;
//        this.spectacole = spectacole;
//    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public AngajatDTO getAngajatDTO() {
        return angajatDTO;
    }

    public void setAngajatDTO(AngajatDTO angajatDTO) {
        this.angajatDTO = angajatDTO;
    }

    public BiletDTO getBiletDTO() {
        return biletDTO;
    }

    public void setBiletDTO(BiletDTO biletDTO) {
        this.biletDTO = biletDTO;
    }

//    public Spectacol[] getSpectacole() {
//        return spectacole;
//    }

//    public void setSpectacole(Spectacol[] spectacole) {
//        this.spectacole = spectacole;
//    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", angajatDTO=" + angajatDTO +
                ", biletDTO=" + biletDTO +
                ", locatie='" + artist + '\'' +
                ", data='" + data + '\'' +
                ", spectacole=" + Arrays.toString(spectacole) +
                '}';
    }
}
