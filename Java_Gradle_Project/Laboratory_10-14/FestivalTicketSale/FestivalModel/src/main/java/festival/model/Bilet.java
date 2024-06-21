package festival.model;

import java.util.Objects;

public class Bilet extends Entity<Long>{
    String nume_cumparator;
    Spectacol spectacol;
    String serie;
    int nr_locuri;

    public Bilet(String nume_cumparator, String serie, Spectacol spectacol, int nr_locuri) {
        this.nume_cumparator = nume_cumparator;
        this.spectacol = spectacol;
        this.nr_locuri = nr_locuri;
        this.serie=serie;
    }

    public String getNume_cumparator() {
        return nume_cumparator;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public void setNume_cumparator(String nume_cumparator) {
        this.nume_cumparator = nume_cumparator;
    }

    public Spectacol getSpectacol() {
        return spectacol;
    }

    public void setSpectacol(Spectacol spectacol) {
        this.spectacol = spectacol;
    }

    public int getNr_locuri() {
        return nr_locuri;
    }

    public void setNr_locuri(int nr_locuri) {
        this.nr_locuri = nr_locuri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bilet bilet)) return false;
        if (!super.equals(o)) return false;
        return getNr_locuri() == bilet.getNr_locuri() && Objects.equals(getNume_cumparator(), bilet.getNume_cumparator()) && Objects.equals(getSpectacol(), bilet.getSpectacol());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNume_cumparator(), getSpectacol(), getNr_locuri(),getSerie());
    }

    @Override
    public String toString() {
        return nume_cumparator + '\'' +
                " "+ nr_locuri;
    }
}
