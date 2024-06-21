package festival.networking.utils;

import festival.networking.jsonprotocol.FestivalClientJsonWorker;
import festival.service.IFestivalServices;

import java.net.Socket;

public class FestivalJsonConcurrentServer extends AbsConcurrentServer{
    private IFestivalServices festtivalServer;


    public FestivalJsonConcurrentServer(int port, IFestivalServices festivalServer) {

        super(port);
        this.festtivalServer = festivalServer;
        System.out.println("Festival- FestivalJsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        FestivalClientJsonWorker worker = new FestivalClientJsonWorker(festtivalServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
