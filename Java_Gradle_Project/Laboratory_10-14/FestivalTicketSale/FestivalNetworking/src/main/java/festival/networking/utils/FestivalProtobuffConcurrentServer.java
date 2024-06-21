package festival.networking.utils;

import festival.networking.protobuffprotocol.FestivalClientProtoWorker;
import festival.networking.utils.AbstractServer;
import festival.service.IFestivalServices;

import java.net.Socket;

public class FestivalProtobuffConcurrentServer extends AbsConcurrentServer {
    private IFestivalServices festivalServer;
    public FestivalProtobuffConcurrentServer(int port, IFestivalServices festivalServices) {
        super(port);
        this.festivalServer=festivalServices;
        System.out.println("FestivalProtobuffConcurrentServer");
    }


    @Override
    protected Thread createWorker(Socket client) {
        System.out.println("Processing request");
        FestivalClientProtoWorker worker = new FestivalClientProtoWorker(festivalServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
