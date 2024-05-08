package festival.server;

import festival.model.Angajat;
import festival.model.Bilet;
import festival.model.Spectacol;
import festival.model.SpectacolDTO;
import festival.persistance.Interfaces.AngajatRepository;
import festival.persistance.Interfaces.BiletRepository;
import festival.persistance.Interfaces.SpectacolRepository;
import festival.service.Exceptions.FestivalException;
import festival.service.IFestivalObserver;
import festival.service.IFestivalServices;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FestivalServicesImpl implements IFestivalServices {
    private AngajatRepository angajatRepository;
    private SpectacolRepository spectacolRepository;
    private Angajat angajatLogat;
    private BiletRepository biletRepository;
    private Map<String,IFestivalObserver> loggedClients;
    public FestivalServicesImpl(AngajatRepository angajatRepository, SpectacolRepository spectacolRepository, BiletRepository biletRepository) {
        this.angajatRepository = angajatRepository;
        this.spectacolRepository = spectacolRepository;
        this.biletRepository = biletRepository;
        loggedClients=new ConcurrentHashMap<>();
    }
    private SpectacolDTO convertSpectacolToDTO(Spectacol spectacol)
    {
        return new SpectacolDTO(spectacol.getLocatie(),String.valueOf(spectacol.getId()),spectacol.getData().toString(),spectacol.getArtist(),String.valueOf(spectacol.getNumar_locuri_vandute()),String.valueOf(spectacol.getNumar_locuri_disponibile()));
    }
    private Spectacol convertDTOToSpectacol(SpectacolDTO spectacolDTO)
    {
        return new Spectacol(spectacolDTO.getLocatie(),LocalDateTime.parse(spectacolDTO.getData()),Integer.parseInt(spectacolDTO.getNrLocuriDisp()),Integer.parseInt(spectacolDTO.getNrLocuriVandute()),spectacolDTO.getArtist());
    }
    @Override
    public SpectacolDTO[] getSpectacole() {
        List<Spectacol> spectacole= (List<Spectacol>) spectacolRepository.findAll();
        return spectacole.stream().map(this::convertSpectacolToDTO).toArray(SpectacolDTO[]::new);
    }

    @Override
    public String[] getArtisti() throws FestivalException {
        List<String> strings= (List<String>) spectacolRepository.getArtisti();
        return strings.toArray(new String[0]);
    }

    private String getInitials(String buyer)
    {
        String[] names=buyer.split(" ");
        String initials="";
        for(String name:names)
        {
            initials+=name.charAt(0);
        }
        return initials;
    }
    private int getNumberOfDigits(long number)
    {
        int count=0;
        while(number>0){
            number/=10;
            count++;
        }
        return count;
    }
    private String getNumberAsString(long number)
    {
        int nrDigits=getNumberOfDigits(number);
        String nr="";
        for(int i=0;i<4-nrDigits;i++)
        {
            nr+="0";
        }
        nr+=Long.toString(number);
        return nr;
    }
    private String generateSeries(long id,String buyer,LocalDateTime date,Long id_spectacol)
    {
        String series="XY";
        series+=Long.toString(id_spectacol);
        series+=getInitials(buyer);
        series+=getNumberAsString(id);
        series+=Integer.toString(date.getDayOfMonth());
        series+=Integer.toString(date.getMonthValue());
        return series;
    }

    @Override
    public synchronized SpectacolDTO addBilet(String nume_cump, long id_spectacol, int nrLocuri,IFestivalObserver observer) throws FestivalException {
        Spectacol spectacol=spectacolRepository.findOne(id_spectacol);
        spectacol.setNumar_locuri_vandute(spectacol.getNumar_locuri_vandute()+nrLocuri);
        spectacol.setNumar_locuri_disponibile(spectacol.getNumar_locuri_disponibile()-nrLocuri);
        spectacolRepository.update(spectacol.getId(),spectacol);
        Bilet bilet=new Bilet(nume_cump,"dummy",spectacol,nrLocuri);
        Bilet biletWithId=biletRepository.save(bilet);
        String series=generateSeries(biletWithId.getId(),nume_cump,spectacol.getData(),spectacol.getId());
        biletWithId.setSerie(series);
        biletRepository.update(biletWithId.getId(),biletWithId);
        SpectacolDTO spectacolDTO=convertSpectacolToDTO(spectacol);
        notifyOfSoldTicket(spectacolDTO,nrLocuri,nume_cump,observer);
        return convertSpectacolToDTO(spectacol);
    }
    private final int defaultThreadsNo=5;
    private void notifyOfSoldTicket(SpectacolDTO spectacol, int nrLocuri,String nume,IFestivalObserver thisObserver)

    {ExecutorService executorService= Executors.newFixedThreadPool(defaultThreadsNo);
        for (IFestivalObserver observer : loggedClients.values()) {
//            if(observer!=thisObserver)
            executorService.execute(() -> {
                try {
                    System.out.println("Notifying from "+angajatLogat.getId()+" sold ticket to client.");
                    observer.recievedSellingOfTicket(spectacol, nrLocuri,nume);

                } catch (FestivalException e) {
                    System.out.println("Error notifying observer "+e);
                }
            });
        }
        executorService.shutdown();
    }

    @Override
    public synchronized Angajat logIn(String username, String password, IFestivalObserver employee) throws FestivalException {
        Angajat angajat=angajatRepository.findByEmail(username);
        if(angajat==null)
        {
            angajat=angajatRepository.findByUsername(username);
            if(angajat==null)
            {
                throw new FestivalException("Angajat nu exista cu username/email respectiv!");
//                return false;
            }
        }
        if(Objects.equals(angajat.getPassword(), password))
        {
            if(loggedClients.get(angajat.getUsername())!=null)
            {
                throw new FestivalException("Angajatul este deja logat!");
            }
            else
            {
                this.angajatLogat=angajat;
                loggedClients.put(angajat.getUsername(),employee);
                //maybe notify here
                return angajat;
            }
        }
        else
        {
            throw new FestivalException("Parola incorecta!");
        }
    }

    @Override
    public synchronized boolean logOut(Angajat angajat) throws FestivalException {
        IFestivalObserver client=loggedClients.remove(angajat.getUsername());

        if(client==null)
        {
            throw new FestivalException("Angajatul nu este logat!");
        }
        return true;
    }

    @Override
    public synchronized SpectacolDTO[] filterSpectacole(LocalDateTime date, String Artist,boolean filteredByDate) {
        List<Spectacol> spectacols;
        if(date==null&&(Artist==null||Artist.equals("None")))
        {
            spectacols= (List<Spectacol>) spectacolRepository.findAll();
            return spectacols.stream().map(this::convertSpectacolToDTO).toArray(SpectacolDTO[]::new);
        }
        if(Objects.equals(Artist, "None") || Artist==null)
        {
            spectacols= (List<Spectacol>) spectacolRepository.findByDate(date);
            return spectacols.stream().map(this::convertSpectacolToDTO).toArray(SpectacolDTO[]::new);
        }
        if(date==null)
        {
            spectacols= (List<Spectacol>) spectacolRepository.findByArtist(Artist);
            return spectacols.stream().map(this::convertSpectacolToDTO).toArray(SpectacolDTO[]::new);
        }
        spectacols= (List<Spectacol>) spectacolRepository.findByDateandArtist(Artist,date);
        return spectacols.stream().map(this::convertSpectacolToDTO).toArray(SpectacolDTO[]::new);
    }
}
