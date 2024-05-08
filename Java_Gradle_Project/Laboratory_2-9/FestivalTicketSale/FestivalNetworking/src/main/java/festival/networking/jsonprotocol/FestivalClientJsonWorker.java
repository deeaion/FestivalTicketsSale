package festival.networking.jsonprotocol;

import com.google.gson.Gson;
import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.networking.dto.AngajatDTO;
import festival.networking.dto.DTOUtils;
import festival.networking.jsonprotocol.Enums.RequestType;
import festival.networking.jsonprotocol.Enums.ResponseType;
import festival.service.Exceptions.FestivalException;
import festival.service.IFestivalObserver;
import festival.service.IFestivalServices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;

public class FestivalClientJsonWorker implements Runnable, IFestivalObserver {
    private IFestivalServices server;
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private volatile boolean connected;

    public FestivalClientJsonWorker(IFestivalServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        gsonFormatter = new Gson();
        try {
            output = new PrintWriter(connection.getOutputStream());
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();}}

    @Override
    public void run() {
        while(connected){
            try {
                String requestLine=input.readLine();
                Request request=gsonFormatter.fromJson(requestLine, Request.class);
                Response response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                    if(response.getType()== ResponseType.ERROR)
                    {
                        output.flush();
                        connected=false;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private void sendResponse(Response response) {
        String responseLine=gsonFormatter.toJson(response);
        System.out.println("sending response "+responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    private Response handleRequest(Request request) {
        Response response=null;
        if(request.getType()== RequestType.LOGIN){
            System.out.println("Login request ...");
            AngajatDTO angajatDTO=request.getAngajatDTO();
            try{
                server.logIn(angajatDTO.getToken(),angajatDTO.getPass(),this);
                return JsonProtocolUtils.createUserLoggedInResponse(angajatDTO.getToken(),angajatDTO.getPass());
            }
            catch (FestivalException e){
                connected=false;
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if(request.getType()==RequestType.LOGOUT)
        {
            System.out.println("Logout request ...");
            Angajat angajatDTo= DTOUtils.getFromDTO(request.getAngajatDTO() );
            try{
                server.logOut(angajatDTo);
                connected=false;
                return okResponse;
            }
            catch (FestivalException e){
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.GET_SPECTACOLE) {
            System.out.println("GetSpectacole Request ...");
            String artist = request.getArtist();
            String data = request.getData();
            LocalDateTime localDateTime;
            try {
                localDateTime = LocalDateTime.parse(data);
            }
            catch (Exception e) {
                System.out.println("Error parsing date "+e);
            }
            try {
                SpectacolDTO[] spectacole ;
                spectacole = server.getSpectacole();
                return JsonProtocolUtils.createGetSpectacoleResponse(spectacole);
            }
            catch (FestivalException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()== RequestType.SELL_TICKET){
            System.out.println("Buy ticket request ...");
            int nrLocuri= Integer.parseInt(request.getBiletDTO().getNrLocuri());
            int spectacol=Integer.parseInt(request.getBiletDTO().getIdSpectacol());
            String nume=request.getBiletDTO().getNumeClient();
            try{
                System.out.println("Buy ticket request ..."+nume+" "+spectacol+" "+nrLocuri);
                SpectacolDTO responseMessage=server.addBilet(nume,spectacol,nrLocuri,this);
               // return JsonProtocolUtils.createBuyTicketResponse(responseMessage,nume,String.valueOf(nrLocuri));
               return okResponse;
            } catch (FestivalException e) {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        if (request.getType()==RequestType.GET_ARTISTI)
        {
            System.out.println("GetArtists Request ...");
            try {
                String[] artisti = server.getArtisti();
                return JsonProtocolUtils.createGetArtistsResponse(artisti);
            }
            catch (FestivalException e)
            {
                return JsonProtocolUtils.createErrorResponse(e.getMessage());
            }
        }
        return response;
    }



    @Override
    public void recievedSellingOfTicket(SpectacolDTO spectacol, int nrLocuri, String nume) throws FestivalException {
        Response response=JsonProtocolUtils.createRecievedSpecResponse(spectacol,nrLocuri,nume);
        System.out.println("Ticket sold "+spectacol+" "+nrLocuri);
        try{
            sendResponse(response);
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    @Override
    public void userLoggedIn(String username,String password) throws FestivalException {
        Response response=JsonProtocolUtils.createUserLoggedInResponse(username,password);
        System.out.println("User logged in "+username);
        try{
            sendResponse(response);
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    @Override
    public void userLoggedOut(String username,String password) throws FestivalException {
        Response response=JsonProtocolUtils.createUserLoggedOutResponse(username);
        System.out.println("User logged out "+username);
        try{
            sendResponse(response);
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    @Override
    public void recievedSpectacole(SpectacolDTO[] spectacole) throws FestivalException {
        Response response=JsonProtocolUtils.createGetSpectacoleResponse(spectacole);
        System.out.println("Recieved spectacole");
        try{
            sendResponse(response);
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    private static Response okResponse=JsonProtocolUtils.createOkResponse();

}
