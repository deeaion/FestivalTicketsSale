package festival.networking.jsonprotocol;

import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.networking.dto.AngajatDTO;
import festival.networking.dto.BiletDTO;
import festival.networking.jsonprotocol.Enums.RequestType;
import festival.networking.jsonprotocol.Enums.ResponseType;
import festival.networking.dto.DTOUtils;
import java.time.LocalDateTime;

public class JsonProtocolUtils {
    public static Response createRecievedSpecResponse(SpectacolDTO spectacol, int nrLocuri, String nume) {
        Response response= new Response();
        response.setType(ResponseType.GOT_FESTIVAL_UPDATED);
        response.setSpectacolUpdated(spectacol);
        response.setBiletDTO(new BiletDTO(spectacol.getId_spectacol(),nume,String.valueOf(nrLocuri)));
        return response;
    }

    public static Response createUserLoggedInResponse(String username,String password) {
        Response response=new Response();
        response.setType(ResponseType.USER_LOGGED_IN);
        response.setAngajatDTO(new AngajatDTO(username,password));
        response.setAngajat(DTOUtils.getFromDTO(new AngajatDTO(username,password)));
        return response;
    }

    public static Response createUserLoggedOutResponse(String username) {
        Response response=new Response();
        response.setType(ResponseType.USER_LOGGED_OUT);
        response.setAngajatDTO(new AngajatDTO(username,""));
        return response;
    }

    public static Response createOkResponse() {
        Response response=new Response();
        response.setType(ResponseType.OK);
        return response;
    }

    public static Response createErrorResponse(String message) {
        Response response=new Response();
        response.setType(ResponseType.ERROR);
        response.setErrorMessage(message);
        return response;
    }

    public static Response createGetSpectacoleResponse(SpectacolDTO[] spectacole) {
        Response response=new Response();
        response.setType(ResponseType.GET_FESTIVAL_LIST);
        response.setSpectacole(spectacole);
        return response;
    }
////////////////nu l am implementat in C#////////////////////////
    public static Response createBuyTicketResponse(SpectacolDTO responseMessage, String client,String locuri) {
        Response response=new Response();
        response.setType(ResponseType.GOT_FESTIVAL_UPDATED);
        response.setSpectacolUpdated(responseMessage);
        response.setBiletDTO(new BiletDTO(responseMessage.getId_spectacol(),client,locuri)  );
        System.out.println("Response: "+response);
        return response;
    }

    public static Request createLoginRequest(String username, String password) {
        Request request=new Request();
        request.setType(RequestType.LOGIN);
        request.setAngajatDTO(new AngajatDTO(username,password));
        request.setAngajat(DTOUtils.getFromDTO(new AngajatDTO(username,password)));
        return request;
    }

    public static Request createLogoutRequest(Angajat angajat) {
        Request request=new Request();
        request.setType(RequestType.LOGOUT);
        request.setAngajatDTO(DTOUtils.getDTO(angajat));
        return request;
    }

    public static Request createGetSpectacoleRequest() {
        Request request=new Request();
        request.setType(RequestType.GET_SPECTACOLE);
        return request;
    }

    public static Request createAddBiletRequest(String numeCump, long idSpectacol, int nrLocuri) {
        Request request=new Request();
        request.setType(RequestType.SELL_TICKET);
        request.setBiletDTO(new BiletDTO(String.valueOf(idSpectacol),numeCump,String.valueOf(nrLocuri)));
        return request;
    }

    public static Request createGetSpectacoleFilteredRequest(LocalDateTime date, String artist) {
        Request request=new Request();
        request.setType(RequestType.GET_SPECTACOLE);
        if(date!=null)
            request.setData(date.toString());
        request.setArtist(artist);
        return request;
    }

    public static Request createGetArtistiRequest() {
        Request request=new Request();
        request.setType(RequestType.GET_ARTISTI);
        return request;
    }

    public static Response createGetArtistsResponse(String[] artisti) {
        Response response=new Response();
        response.setType(ResponseType.GET_ARTISTI);
        response.setArtisti(artisti);
        return response;
    }
}
