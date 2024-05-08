package festival.networking.protobuffprotocol;

import festival.model.Angajat;
import festival.model.SpectacolDTO;
import festival.networking.FestivalProto;
import festival.networking.dto.AngajatDTO;
import festival.networking.dto.DTOUtils;
import festival.networking.jsonprotocol.Enums.RequestType;
import festival.networking.jsonprotocol.JsonProtocolUtils;
import festival.networking.jsonprotocol.Response;
import festival.service.Exceptions.FestivalException;
import festival.service.IFestivalObserver;
import festival.service.IFestivalServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Arrays;

public class FestivalClientProtoWorker implements Runnable, IFestivalObserver{
    private IFestivalServices server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;

    public FestivalClientProtoWorker(IFestivalServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
            input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {

        while(connected){
            try {
                // Object request=input.readObject();
                System.out.println("Waiting requests ...");
                FestivalProto.FestivalRequest request=FestivalProto.FestivalRequest.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                FestivalProto.FestivalResponse response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
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


    private FestivalProto.FestivalResponse handleRequest(FestivalProto.FestivalRequest request) {
        FestivalProto.FestivalResponse response=null;
        if(request.getRequestType()== FestivalProto.FestivalRequest.RequestType.LOGIN){
            System.out.println("Login request ...");
            FestivalProto.Angajat angajatDTO=request.getAngajat();
            try{
                server.logIn(angajatDTO.getToken(),angajatDTO.getPassword(),this);
                return okResponse;
            }
            catch (FestivalException e){
                connected=false;
                return FestivalProto.FestivalResponse.newBuilder().setResponseType(FestivalProto.FestivalResponse.ResponseType.ERROR).setError(e.getMessage()).build();
            }
        }
        if(request.getRequestType()== FestivalProto.FestivalRequest.RequestType.LOGOUT){
            System.out.println("Logout request ...");
            FestivalProto.Angajat angajatDTo= request.getAngajat();
            Angajat angajat= ProtoUtills.getAngajatModel(angajatDTo);
            try{
                server.logOut(angajat);
                connected=false;
                return okResponse;
            }
            catch (FestivalException e){
                return FestivalProto.FestivalResponse.newBuilder().setResponseType(FestivalProto.FestivalResponse.ResponseType.ERROR).setError(e.getMessage()).build();
            }
        }

        if (request.getRequestType()== FestivalProto.FestivalRequest.RequestType.GET_SPECTACOLE) {
            System.out.println("GetSpectacole Request ...");
            String artist = request.getSpectacol().getArtist();
            String data = request.getSpectacol().getData();
            LocalDateTime localDateTime;
            try {
                localDateTime = LocalDateTime.parse(data);
            }
            catch (Exception e) {
                localDateTime = null;
            }
            try {
                SpectacolDTO[] spectacole = server.filterSpectacole(localDateTime, artist, request.getFilteredByDate());
                return ProtoUtills.createGetSpectacoleResponse(spectacole);
            }
            catch (FestivalException e)
            {
                return FestivalProto.FestivalResponse.newBuilder().setResponseType(FestivalProto.FestivalResponse.ResponseType.ERROR).setError(e.getMessage()).build();
            }
        }

        if (request.getRequestType()== FestivalProto.FestivalRequest.RequestType.SELL_TICKET){
            System.out.println("Buy ticket request ...");
            int nrLocuri= Integer.parseInt(request.getBilet().getNrLocuri());
            int spectacol=Integer.parseInt(request.getBilet().getSpectacol().getIdSpec());

            String nume=request.getBilet().getNumeCump();
            try{
                System.out.println("Buy ticket request ..."+nume+" "+spectacol+" "+nrLocuri);
                SpectacolDTO responseMessage=server.addBilet(nume,spectacol,nrLocuri,this);
                // return JsonProtocolUtils.createBuyTicketResponse(responseMessage,nume,String.valueOf(nrLocuri));
                return okResponse;
            } catch (FestivalException e) {
                return FestivalProto.FestivalResponse.newBuilder().setResponseType(FestivalProto.FestivalResponse.ResponseType.ERROR).setError(e.getMessage()).build();
            }
        }

        if (request.getRequestType()== FestivalProto.FestivalRequest.RequestType.GET_ARTISTI)
        {
            System.out.println("GetArtists Request ...");
            try {
                String[] artisti = server.getArtisti();
                return FestivalProto.FestivalResponse.newBuilder().setResponseType(FestivalProto.FestivalResponse.ResponseType.GET_ARTISTI).addAllArtist(Arrays.stream(artisti).toList()).build();
            }
            catch (FestivalException e)
            {
                return FestivalProto.FestivalResponse.newBuilder().setResponseType(FestivalProto.FestivalResponse.ResponseType.ERROR).setError(e.getMessage()).build();
            }
        }
        return response;


    }

    @Override
    public void recievedSellingOfTicket(SpectacolDTO spectacol, int nrLocuri, String nume) throws FestivalException {
        Response response=JsonProtocolUtils.createRecievedSpecResponse(spectacol,nrLocuri,nume);
        System.out.println("Ticket sold "+spectacol+" "+nrLocuri);
        try{
            sendResponse(ProtoUtills.createRecievedSpecResponse(spectacol,nrLocuri,nume));
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    @Override
    public void userLoggedIn(String username, String password) throws FestivalException {
        Response response=JsonProtocolUtils.createUserLoggedInResponse(username,password);
        System.out.println("User logged in "+username);
        try{
            sendResponse(ProtoUtills.createUserLoggedInResponse(username,password));
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    @Override
    public void userLoggedOut(String username, String password) throws FestivalException {
        Response response=JsonProtocolUtils.createUserLoggedOutResponse(username);
        System.out.println("User logged out "+username);
        try{
            sendResponse(ProtoUtills.createUserLoggedOutResponse(username));
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }

    @Override
    public void recievedSpectacole(SpectacolDTO[] spectacole) throws FestivalException {
        Response response=JsonProtocolUtils.createGetSpectacoleResponse(spectacole);
        System.out.println("Recieved spectacole");
        try{
            sendResponse(ProtoUtills.createGetSpectacoleResponse(spectacole));
        } catch (Exception e) {
            throw new FestivalException("Sending response error "+e);
        }
    }
    private static FestivalProto.FestivalResponse okResponse=ProtoUtills.createOkResponse();

    private void sendResponse(FestivalProto.FestivalResponse response) {
        try {
            System.out.println("Sending response "+response);
            response.writeDelimitedTo(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
}}}
