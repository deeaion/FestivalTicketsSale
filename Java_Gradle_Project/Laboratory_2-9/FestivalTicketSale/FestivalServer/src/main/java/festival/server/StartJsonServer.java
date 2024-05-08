package festival.server;

import festival.model.Validators.AngajatValidator;
import festival.model.Validators.Enums.ValidatorStrategy;
import festival.model.Validators.SpectacolValidator;
import festival.model.Validators.ValidatorFactory;
import festival.networking.utils.AbstractServer;
import festival.networking.utils.FestivalJsonConcurrentServer;
import festival.persistance.DBImplementation.DBAngajatHibRepository;
import festival.persistance.DBImplementation.DBAngajatRepository;
import festival.persistance.DBImplementation.DBBiletRepository;
import festival.persistance.DBImplementation.DBSpectacolRepository;
import festival.persistance.Interfaces.AngajatRepository;
import festival.persistance.Interfaces.BiletRepository;
import festival.persistance.Interfaces.SpectacolRepository;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort=55555;
    private static void printUsage(){
        System.out.println("Usage: java -jar server.jar[<port>]");
    }
    public static void main(String [] args)
    {
        Properties serverProps=new Properties();
        try{
            serverProps.load(StartJsonServer.class.getResourceAsStream("/festivalserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        }
        catch (IOException e)
        {
            System.err.println("Cannot find festivalserver.properties "+e);
            return;
        }
        ValidatorFactory validatorFactory=new ValidatorFactory();
        AngajatValidator angajatValidator= (AngajatValidator) validatorFactory.createValidator(ValidatorStrategy.Angajat);
        SpectacolValidator spectacolValidator= (SpectacolValidator) validatorFactory.createValidator(ValidatorStrategy.Spectacol);
        BiletRepository biletRepository=new DBBiletRepository(serverProps);
        AngajatRepository angajatRepository=new DBAngajatHibRepository(serverProps,angajatValidator);
        angajatRepository.findAll().forEach(System.out::println);
//        AngajatRepository angajatRepository=new DBAngajatRepository(serverProps,angajatValidator);
        SpectacolRepository spectacolRepository=new DBSpectacolRepository(serverProps,spectacolValidator);
        int chatServerPort=defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("festival.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong  Port Number"+nef.getMessage());
            System.err.println("Using defaultPort"+defaultPort);
        }
        System.out.println("Starting server on port: "+chatServerPort);
        AbstractServer server = new FestivalJsonConcurrentServer(chatServerPort, new FestivalServicesImpl(angajatRepository,spectacolRepository,biletRepository));
        try
        {
            server.start();
        }
        catch (ServerException e)
        {
            System.err.println("Error starting the server" + e.getMessage());
        }

    }
}
