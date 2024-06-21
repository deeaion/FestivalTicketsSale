package festival.networking.dto;

import java.io.Serializable;

public class BiletDTO implements Serializable {

    private String idSpectacol;
    private String numeClient;
    private String nrLocuri;

    public BiletDTO(String idSpectacol, String numeClient, String nrLocuri) {

        this.idSpectacol = idSpectacol;
        this.numeClient = numeClient;
        this.nrLocuri = nrLocuri;
    }





    public String getIdSpectacol() {
        return idSpectacol;
    }

    public void setIdSpectacol(String idSpectacol) {
        this.idSpectacol = idSpectacol;
    }

    public String getNumeClient() {
        return numeClient;
    }

    public void setNumeClient(String numeClient) {
        this.numeClient = numeClient;
    }

    public String getNrLocuri() {
        return nrLocuri;
    }

    public void setNrLocuri(String nrLocuri) {
        this.nrLocuri = nrLocuri;
    }

    @Override
    public String toString() {
        return "BiletDTO{" +

                ", idSpectacol='" + idSpectacol + '\'' +
                ", numeClient='" + numeClient + '\'' +
                ", nrLocuri='" + nrLocuri + '\'' +
                '}';
    }
}
