package festival.model;

import java.util.Objects;

public class SpectacolDTO {
    private String locatie;
    private String id_spectacol;
    private String data;
    private String artist;
    private String nrLocuriVandute;
    private String nrLocuriDisp;

    public SpectacolDTO(String locatie, String id_spectacol, String data, String artist, String nrLocuriVandute, String nrLocuriDisp) {
        this.locatie = locatie;
        this.id_spectacol = id_spectacol;
        this.data = data;
        this.artist = artist;
        this.nrLocuriVandute = nrLocuriVandute;
        this.nrLocuriDisp = nrLocuriDisp;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    public String getId_spectacol() {
        return id_spectacol;
    }

    public void setId_spectacol(String id_spectacol) {
        this.id_spectacol = id_spectacol;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getNrLocuriVandute() {
        return nrLocuriVandute;
    }

    public void setNrLocuriVandute(String nrLocuriVandute) {
        this.nrLocuriVandute = nrLocuriVandute;
    }

    public String getNrLocuriDisp() {
        return nrLocuriDisp;
    }

    public void setNrLocuriDisp(String nrLocuriDisp) {
        this.nrLocuriDisp = nrLocuriDisp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpectacolDTO that)) return false;
        return Objects.equals(getLocatie(), that.getLocatie()) && Objects.equals(getId_spectacol(), that.getId_spectacol()) && Objects.equals(getData(), that.getData()) && Objects.equals(getArtist(), that.getArtist()) && Objects.equals(getNrLocuriVandute(), that.getNrLocuriVandute()) && Objects.equals(getNrLocuriDisp(), that.getNrLocuriDisp());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocatie(), getId_spectacol(), getData(), getArtist(), getNrLocuriVandute(), getNrLocuriDisp());
    }

    @Override
    public String toString() {
        return "SpectacolDTO{" +
                "locatie='" + locatie + '\'' +
                ", id_spectacol='" + id_spectacol + '\'' +
                ", data='" + data + '\'' +
                ", artist='" + artist + '\'' +
                ", nrLocuriVandute='" + nrLocuriVandute + '\'' +
                ", nrLocuriDisp='" + nrLocuriDisp + '\'' +
                '}';
    }
}
