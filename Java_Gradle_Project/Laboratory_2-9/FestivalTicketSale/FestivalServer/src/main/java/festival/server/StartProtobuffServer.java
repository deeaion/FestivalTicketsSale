package festival.server;

import festival.model.Angajat;
import festival.model.Validators.AngajatValidator;
import festival.model.Validators.Enums.ValidatorStrategy;
import festival.model.Validators.SpectacolValidator;
import festival.model.Validators.ValidatorFactory;
import festival.networking.utils.FestivalProtobuffConcurrentServer;
import festival.networking.utils.AbstractServer;
import festival.persistance.DBImplementation.DBAngajatHibRepository;
import festival.persistance.DBImplementation.DBAngajatRepository;
import festival.persistance.DBImplementation.DBBiletRepository;
import festival.persistance.DBImplementation.DBSpectacolRepository;
import festival.persistance.Interfaces.AngajatRepository;
import festival.persistance.Interfaces.BiletRepository;
import festival.persistance.Interfaces.SpectacolRepository;
import festival.service.IFestivalServices;


import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {


        Properties serverProps = new Properties();
        try {
            serverProps.load(StartProtobuffServer.class.getResourceAsStream("/festivalserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }
        ValidatorFactory validatorFactory = new ValidatorFactory();
        AngajatValidator angajatValidator = (AngajatValidator) validatorFactory.createValidator(ValidatorStrategy.Angajat);
        SpectacolValidator spectacolValidator = (SpectacolValidator) validatorFactory.createValidator(ValidatorStrategy.Spectacol);
        BiletRepository biletRepository = new DBBiletRepository(serverProps);

        AngajatRepository angajatRepository = new DBAngajatRepository(serverProps, angajatValidator);
//        AngajatRepository angajatRepository = new DBAngajatHibRepository(serverProps, angajatValidator);
        angajatRepository.findAll().forEach(System.out::println);
        angajatRepository.save(new Angajat("test","test", "test", "test", "test"));
        angajatRepository.findAll().forEach(System.out::println);

        SpectacolRepository spectacolRepository = new DBSpectacolRepository(serverProps, spectacolValidator);

        IFestivalServices festivalServices = new FestivalServicesImpl(angajatRepository, spectacolRepository, biletRepository);

        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new FestivalProtobuffConcurrentServer(chatServerPort, festivalServices);
        try {
            server.start();

        } catch (java.rmi.ServerException e) {
            throw new RuntimeException(e);
        }

    }

    }
