package festival.networking.jsonprotocol;

import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.networking.dto.AngajatDTO;
import festival.networking.dto.BiletDTO;
import festival.networking.jsonprotocol.Enums.ResponseType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class Response implements Serializable {
    private ResponseType type;
    private String errorMessage;
    private AngajatDTO angajatDTO;
    private SpectacolDTO[] spectacole;
    private BiletDTO biletDTO;
    private Angajat angajat;
    private String[] artisti;
    private SpectacolDTO spectacolUpdated;

    public SpectacolDTO getSpectacolUpdated() {
        return spectacolUpdated;
    }

    public void setSpectacolUpdated(SpectacolDTO spectacolUpdated) {
        this.spectacolUpdated = spectacolUpdated;
    }

    private String locatie;
    private String data;
    public Response(){}
    public Response(ResponseType type, String errorMessage) {
        this.type = type;
        this.errorMessage = errorMessage;
    }
    public Response(ResponseType type, AngajatDTO angajatDTO) {
        this.type = type;
        this.angajatDTO = angajatDTO;
    }
    public Response(ResponseType type, SpectacolDTO[] spectacole,String locatie,String data) {
        this.type = type;
        this.spectacole = spectacole;
        this.locatie=locatie;
        this.data=data;
    }
    public Response(ResponseType type, BiletDTO biletDTO) {
        this.type = type;
        this.biletDTO = biletDTO;
    }

    public Response(ResponseType type, SpectacolDTO spectacolUpdated,BiletDTO biletDTO) {
        this.type = type;
        this.spectacolUpdated = spectacolUpdated;
        this.biletDTO=biletDTO;
    }
    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", angajatDTO=" + angajatDTO +
                ", spectacole=" + Arrays.toString(spectacole) +
                ", biletDTO=" + biletDTO +
                ", locatie='" + locatie + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public AngajatDTO getAngajatDTO() {
        return angajatDTO;
    }

    public void setAngajatDTO(AngajatDTO angajatDTO) {
        this.angajatDTO = angajatDTO;
    }

    public SpectacolDTO[] getSpectacole() {
        return spectacole;
    }

    public void setSpectacole(SpectacolDTO[] spectacole) {
        this.spectacole = spectacole;
    }

    public BiletDTO getBiletDTO() {
        return biletDTO;
    }

    public void setBiletDTO(BiletDTO biletDTO) {
        this.biletDTO = biletDTO;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response response)) return false;
        return Objects.equals(type, response.type) && Objects.equals(errorMessage, response.errorMessage) && Objects.equals(angajatDTO, response.angajatDTO) && Arrays.equals(spectacole, response.spectacole) && Objects.equals(biletDTO, response.biletDTO) && Objects.equals(locatie, response.locatie) && Objects.equals(data, response.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(type, errorMessage, angajatDTO, biletDTO, locatie, data);
        result = 31 * result + Arrays.hashCode(spectacole);
        return result;
    }

    public Angajat getAngajat() {
        return angajat;
    }

    public void setAngajat(Angajat angajat) {
        this.angajat = angajat;
    }

    public String[] getArtisti() {
        return artisti;
    }

    public void setArtisti(String[] artisti) {
        this.artisti = artisti;
    }
}
