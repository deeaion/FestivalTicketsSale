package festival.rest.Controller;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class SpectacolDTOR {
    private String data;
    private String locatie;
    private int nrLocuriDisponibile;
    private int nrLocuriVandute;
    private String artist;
    private Long id;

    public SpectacolDTOR(String data, String locatie, int nrLocuriDisponibile, int nrLocuriVandute, String artist, Long id) {
        this.data = data;
        this.locatie = locatie;
        this.nrLocuriDisponibile = nrLocuriDisponibile;
        this.nrLocuriVandute = nrLocuriVandute;
        this.artist = artist;
        this.id = id;
    }

    public SpectacolDTOR() {
    }

    @Override
    public String toString() {
        return "SpectacolDTOR{" +
                "data='" + data + '\'' +
                ", locatie='" + locatie + '\'' +
                ", nrLocuriDisponibile=" + nrLocuriDisponibile +
                ", nrLocuriVandute=" + nrLocuriVandute +
                ", artist='" + artist + '\'' +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpectacolDTOR that)) return false;
        return getNrLocuriDisponibile() == that.getNrLocuriDisponibile() && getNrLocuriVandute() == that.getNrLocuriVandute() && Objects.equals(getData(), that.getData()) && Objects.equals(getLocatie(), that.getLocatie()) && Objects.equals(getArtist(), that.getArtist()) && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getLocatie(), getNrLocuriDisponibile(), getNrLocuriVandute(), getArtist(), getId());
    }
}
