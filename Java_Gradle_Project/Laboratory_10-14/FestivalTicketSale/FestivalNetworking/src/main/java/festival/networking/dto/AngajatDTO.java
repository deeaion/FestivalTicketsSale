package festival.networking.dto;

import java.io.Serializable;

public class AngajatDTO implements Serializable {
    private String token;
    private String pass;

    public AngajatDTO(String token, String pass) {
        this.token = token;
        this.pass = pass;
    }

    @Override
    public String toString() {
        return "AngajatDTO{" +
                "token='" + token + '\'' +
                ", pass='" + pass + '\'' +
                '}';
    }

    public String getToken() {
        return token;
    }

    public String getPass() {
        return pass;
    }
}
