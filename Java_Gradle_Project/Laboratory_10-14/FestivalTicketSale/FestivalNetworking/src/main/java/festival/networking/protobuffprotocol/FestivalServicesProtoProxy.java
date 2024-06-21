package festival.networking.protobuffprotocol;

import festival.model.Angajat;
import festival.model.SpectacolDTO;
import festival.networking.FestivalProto;
import festival.networking.jsonprotocol.Enums.ResponseType;
import festival.networking.jsonprotocol.JsonProtocolUtils;
import festival.networking.jsonprotocol.Request;
import festival.networking.jsonprotocol.Response;
import festival.service.Exceptions.FestivalException;
import festival.service.IFestivalObserver;
import festival.service.IFestivalServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FestivalServicesProtoProxy  implements IFestivalServices {
    private String host;
    private int port;

    private IFestivalObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<FestivalProto.FestivalResponse> qresponses;
    private volatile boolean finished;

    public FestivalServicesProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<FestivalProto.FestivalResponse>();
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
    private void sendRequest(FestivalProto.FestivalRequest request)throws FestivalException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new FestivalException("Error sending object "+e);
        }

    }
    private FestivalProto.FestivalResponse readResponse() throws FestivalException{
        FestivalProto.FestivalResponse response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws FestivalException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    FestivalProto.FestivalResponse response= FestivalProto.FestivalResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getResponseType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }
    }

    private void handleUpdate(FestivalProto.FestivalResponse response) {
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED){
            System.out.println("Spectacol updated "+response.getSpectacol());
            SpectacolDTO spectacol= ProtoUtills.getSpectacolDTOModel(response.getSpectacol());
            System.out.println("Spectacol updated "+spectacol);
            try {
                System.out.println("Handling update "+spectacol);
                client.recievedSellingOfTicket(spectacol, 0,"");
                return;
            } catch (FestivalException e) {
                e.printStackTrace();
            }
        }
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED){
            SpectacolDTO [] spectacole= response.getSpectacoleList().stream().map(ProtoUtills::getSpectacolDTOModel).toArray(SpectacolDTO[]::new);
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
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED){
            System.out.println("Error "+response.getError());
        }
    }

    private boolean isUpdateResponse(FestivalProto.FestivalResponse.ResponseType type){

        switch (type){
            case GOT_FESTIVAL_UPDATED: return true;
        }
        return false;
    }
    @Override
    public SpectacolDTO[] getSpectacole() throws FestivalException {
           FestivalProto.FestivalRequest request= FestivalProto.FestivalRequest.newBuilder()
                    .setRequestType(FestivalProto.FestivalRequest.RequestType.GET_SPECTACOLE).setFilteredByDate(false).build();

        sendRequest(request);
        FestivalProto.FestivalResponse response=readResponse();
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.ERROR){
            String err=response.getError();
            throw new FestivalException(err);
        }
        SpectacolDTO [] spectacole=response.getSpectacoleList().stream().map(ProtoUtills::getSpectacolDTOModel).toArray(SpectacolDTO[]::new);
        return spectacole;
    }

    @Override
    public String[] getArtisti() throws FestivalException {
        FestivalProto.FestivalRequest request= FestivalProto.FestivalRequest.newBuilder()
                .setRequestType(FestivalProto.FestivalRequest.RequestType.GET_ARTISTI)
                .build();
        sendRequest(request);
        FestivalProto.FestivalResponse response=readResponse();
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.ERROR){
            String err=response.getError();
            throw new FestivalException(err);
        }
        String [] artisti=response.getArtistList().toArray(new String[0]);
        return artisti;
    }

    @Override
    public SpectacolDTO addBilet(String nume_cump, long id_spectacol, int nrLocuri, IFestivalObserver observer) throws FestivalException {
        sendRequest(ProtoUtills.createAddBiletRequest(nume_cump,id_spectacol,nrLocuri));
        System.out.println("Request sent");

        FestivalProto.FestivalResponse response=readResponse();
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.ERROR){
            String err=response.getError();
            throw new FestivalException(err);
        }
        return ProtoUtills.getSpectacolDTOModel(response.getSpectacol());
    }

    @Override
    public Angajat logIn(String username, String password, IFestivalObserver employee) throws FestivalException {
        initializeConnection();
        Request request = JsonProtocolUtils.createLoginRequest(username, password);
        FestivalProto.FestivalRequest festivalRequest = FestivalProto.FestivalRequest.newBuilder()
                .setRequestType(FestivalProto.FestivalRequest.RequestType.LOGIN)
                .setAngajat(FestivalProto.Angajat.newBuilder()
                        .setToken(username)
                        .setPassword(password)
                        .build())
                .build();
        sendRequest(festivalRequest);
        FestivalProto.FestivalResponse response = readResponse();

        if (response.getResponseType() == FestivalProto.FestivalResponse.ResponseType.ERROR) {
            String err = response.getError();
            closeConnection();
            throw new FestivalException(err);
        }
        this.client=employee;
        return ProtoUtills.getAngajatModel(response.getAngajat());
    }

    @Override
    public boolean logOut(Angajat angajat) throws FestivalException {
        sendRequest(ProtoUtills.createLogOutRequest(angajat)  );
        FestivalProto.FestivalResponse response = readResponse();

        closeConnection();
        if (response.getResponseType() == FestivalProto.FestivalResponse.ResponseType.ERROR) {
            String err = response.getError();
            throw new FestivalException(err);
        }

        return true;
    }

    @Override
    public festival.model.SpectacolDTO[] filterSpectacole(LocalDateTime date, String Artist,boolean filtered) throws FestivalException {
        Request request= JsonProtocolUtils.createGetSpectacoleFilteredRequest(date,Artist);
        sendRequest(ProtoUtills.createFilterRequest(Artist,date,filtered));
        FestivalProto.FestivalResponse response=readResponse();
        if (response.getResponseType()== FestivalProto.FestivalResponse.ResponseType.ERROR){
            String err=response.getError();
            throw new FestivalException(err);
        }
        for(FestivalProto.SpectacolDTO spectacolDTO:response.getSpectacoleList()){
            festival.model.SpectacolDTO spectacol=ProtoUtills.getSpectacolDTOModel(spectacolDTO);
        }
        festival.model.SpectacolDTO [] spectacole=response.getSpectacoleList().stream().map(ProtoUtills::getSpectacolDTOModel).toArray(SpectacolDTO[]::new);
        return spectacole;
    }
}
