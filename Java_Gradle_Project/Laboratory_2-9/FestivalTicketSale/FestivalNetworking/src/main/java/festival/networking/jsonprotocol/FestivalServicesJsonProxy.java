package festival.networking.jsonprotocol;

import com.google.gson.Gson;
import festival.model.Angajat;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FestivalServicesJsonProxy implements IFestivalServices {
    private String host;
    private int port;
    private IFestivalObserver client;
    private BufferedReader input;
    private PrintWriter output;
    private Socket connection;
    private Gson gsonFormatter;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public FestivalServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<>();

    }
    @Override
    public SpectacolDTO[] getSpectacole() throws FestivalException{
        Request request=JsonProtocolUtils.createGetSpectacoleRequest();
        sendRequest(request);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new FestivalException(err);
        }
        SpectacolDTO [] spectacole=response.getSpectacole();
        return spectacole;

    }

    @Override
    public String[] getArtisti() throws FestivalException {
        Request request=JsonProtocolUtils.createGetArtistiRequest();
        sendRequest(request);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new FestivalException(err);
        }
        String [] artisti=response.getArtisti();
        return artisti;
    }

    @Override
    public SpectacolDTO addBilet(String nume_cump, long id_spectacol, int nrLocuri,IFestivalObserver observer) throws FestivalException {
        System.out.println("Adding bilet "+nume_cump+" "+id_spectacol+" "+nrLocuri);
        Request request=JsonProtocolUtils.createAddBiletRequest(nume_cump,id_spectacol,nrLocuri);
        sendRequest(request);
        System.out.println("Request sent");

        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new FestivalException(err);
        }
        return response.getSpectacolUpdated();
    }

    @Override
    public Angajat logIn(String username, String password, IFestivalObserver employee) throws FestivalException {
        initializeConnection();
        Request request = JsonProtocolUtils.createLoginRequest(username, password);
        sendRequest(request);
        Response response = readResponse();

        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            closeConnection();
            throw new FestivalException(err);
        }
        this.client=employee;
        return response.getAngajat();

    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws FestivalException {
        String reqLine=gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new FestivalException("Error sending object "+e);
        }

    }

    private Response readResponse() {
        Response response=null;
        try{

            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() {
        try {
            gsonFormatter=new Gson();
            connection=new Socket(host,port);
            output=new PrintWriter(connection.getOutputStream());
            output.flush();
            input=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    @Override
    public boolean logOut(Angajat angajat) throws FestivalException {
        Request request = JsonProtocolUtils.createLogoutRequest(angajat);
        sendRequest(request);
        Response response = readResponse();
        closeConnection();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new FestivalException(err);
        }

        return true;
    }

    @Override
    public SpectacolDTO[] filterSpectacole(LocalDateTime date, String Artist,boolean filtered) throws FestivalException {
        Request request= JsonProtocolUtils.createGetSpectacoleFilteredRequest(date,Artist);
        sendRequest(request);
        Response response=readResponse();
        if (response.getType()== ResponseType.ERROR){
            String err=response.getErrorMessage();
            throw new FestivalException(err);
        }
        SpectacolDTO [] spectacole=response.getSpectacole();
        return spectacole;
    }
    private boolean isUpdate(Response response){
        return response.getType()== ResponseType.GOT_FESTIVAL_UPDATED || response.getType()== ResponseType.GOT_FESTIVAL_LIST_UPDATED;
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    String responseLine=input.readLine();
                    Response response=gsonFormatter.fromJson(responseLine, Response.class);
                    System.out.println("response received "+response);
                    if(response!=null)
                    {if (isUpdate(response)){
                        System.out.println("update received "+response  );
                        handleUpdate(response);
                    }else{

                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }}
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(Response response) {

        if (response.getType()== ResponseType.GOT_FESTIVAL_UPDATED){
            System.out.println("Spectacol updated "+response.getSpectacolUpdated());
            SpectacolDTO spectacol= response.getSpectacolUpdated();
            System.out.println("Spectacol updated "+spectacol);
            try {
                System.out.println("Handling update "+spectacol);
                client.recievedSellingOfTicket(spectacol, Integer.parseInt(response.getBiletDTO().getNrLocuri()),response.getBiletDTO().getNumeClient());
                return;
            } catch (FestivalException e) {
                e.printStackTrace();
            }
        }
        if (response.getType()== ResponseType.GOT_FESTIVAL_LIST_UPDATED){
            SpectacolDTO [] spectacole= response.getSpectacole();
            System.out.println("Spectacole updated "+spectacole);
            try {
                client.recievedSpectacole(spectacole );
                return;
            } catch (FestivalException e) {
                e.printStackTrace();
            }
        }
//        if (response.getType() == ResponseType.USER_LOGGED_IN){
//            Angajat angajat=response.getAngajat();
//            System.out.println("User logged in "+angajat);
//            try {
//                client.userLoggedIn(angajat.getUsername(),angajat.getPassword() );
//            } catch (FestivalException e) {
//                e.printStackTrace();
//            }
//        }
        if (response.getType()== ResponseType.ERROR){
            System.out.println("Error "+response.getErrorMessage());
        }

    }
}
