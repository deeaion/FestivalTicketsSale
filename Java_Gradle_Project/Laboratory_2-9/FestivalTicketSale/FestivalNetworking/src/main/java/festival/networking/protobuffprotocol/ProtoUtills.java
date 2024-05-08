package festival.networking.protobuffprotocol;

import festival.model.SpectacolDTO;
import festival.networking.FestivalProto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class ProtoUtills {

    public static FestivalProto.LogInResponse createLogInResponse(boolean success, String token, String error) {
        return FestivalProto.LogInResponse.newBuilder()
                .setSuccess(success)
                .setToken(token)
                .setError(error)
                .build();
    }
    public static FestivalProto.FestivalRequest createFilterRequest(String artist, LocalDateTime data, Boolean filteredByDate) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FestivalProto.SpectacolDTO.Builder builder = FestivalProto.SpectacolDTO.newBuilder();
        if (artist != null) {
            builder.setArtist(artist);
        }
        if (data != null) {
            builder.setData(data.format(dtf));  // Using DateTimeFormatter to format LocalDateTime
        }
        return FestivalProto.FestivalRequest.newBuilder()
                .setRequestType(FestivalProto.FestivalRequest.RequestType.GET_SPECTACOLE)
                .setSpectacol(builder.build()).setFilteredByDate(filteredByDate)
                .build();
    }

    public static festival.model.Angajat getAngajatModel(FestivalProto.Angajat angajat) {
        return new festival.model.Angajat(angajat.getToken(),angajat.getToken(), angajat.getPassword());
    }
    public static FestivalProto.FestivalRequest createLogOutRequest(festival.model.Angajat angajat) {
        return FestivalProto.FestivalRequest.newBuilder()
                .setRequestType(FestivalProto.FestivalRequest.RequestType.LOGOUT)
                .setAngajat(createAngajat(angajat))
                .build();
    }
    public static FestivalProto.Angajat createAngajat(festival.model.Angajat angajat) {
        String token = (angajat.getEmail() != null && !angajat.getEmail().isEmpty()) ? angajat.getEmail() : angajat.getUsername();
        return FestivalProto.Angajat.newBuilder()
                .setToken(token)
                .setPassword(angajat.getPassword())
                .build();
    }
    public static festival.model.SpectacolDTO getSpectacolDTOModel(FestivalProto.SpectacolDTO spectacolDTO) {

        festival.model.SpectacolDTO spectacolDTO1=new festival.model.SpectacolDTO(spectacolDTO.getLocatie(),spectacolDTO.getIdSpec(),spectacolDTO.getData(),spectacolDTO.getArtist(),spectacolDTO.getNrLocuriVandute(),spectacolDTO.getNrLocuriDisponibile());
        return new festival.model.SpectacolDTO(spectacolDTO.getLocatie(),spectacolDTO.getIdSpec(), spectacolDTO.getData(), spectacolDTO.getArtist(), spectacolDTO.getNrLocuriVandute(), spectacolDTO.getNrLocuriDisponibile());
    }
    public static FestivalProto.SpectacolDTO createSpectacolDTOFromSpec(festival.model.Spectacol spectacol) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return FestivalProto.SpectacolDTO.newBuilder()
                .setData(sdf.format(spectacol.getData()))
                .setNrLocuriVandute(String.valueOf(spectacol.getNumar_locuri_vandute()))
                .setNrLocuriDisponibile(String.valueOf(spectacol.getNumar_locuri_disponibile()))
                .setArtist(spectacol.getArtist())
                .setLocatie(spectacol.getLocatie())
                .setIdSpec(String.valueOf(spectacol.getId()))
                .build();
    }

    public static FestivalProto.Bilet createBilet(festival.model.Bilet bilet) {
        return FestivalProto.Bilet.newBuilder()
                .setNumeCump(bilet.getNume_cumparator())
                .setSerie(bilet.getSerie())
                .setSpectacol(createSpectacolDTOFromSpec(bilet.getSpectacol()))
                .setNrLocuri(String.valueOf(bilet.getNr_locuri()))
                .build();
    }

    public static FestivalProto.FestivalResponse getSpectacoleDTOs(List<festival.model.Spectacol> spectacole) {
        List<FestivalProto.SpectacolDTO> spectacoleDTO = new ArrayList<>();
        for (festival.model.Spectacol spectacol : spectacole) {
            spectacoleDTO.add(createSpectacolDTOFromSpec(spectacol));
        }

        FestivalProto.FestivalResponse response = FestivalProto.FestivalResponse.newBuilder()
                .addAllSpectacole(spectacoleDTO)
                .build();
        return response;
    }

    public static FestivalProto.FestivalRequest createAddBiletRequest(String numeCump, long idSpectacol, int nrLocuri) {
        return FestivalProto.FestivalRequest.newBuilder()
                .setRequestType(FestivalProto.FestivalRequest.RequestType.SELL_TICKET)
                .setBilet(FestivalProto.Bilet.newBuilder()
                        .setNumeCump(numeCump)
                        .setNrLocuri(String.valueOf(nrLocuri))
                        .setSpectacol(FestivalProto.SpectacolDTO.newBuilder()
                                .setIdSpec(String.valueOf(idSpectacol))
                                .build())
                        .build())
                .build();
    }

    public static FestivalProto.FestivalResponse createOkResponse() {
        return FestivalProto.FestivalResponse.newBuilder()
                .setResponseType(FestivalProto.FestivalResponse.ResponseType.OK)
                .build();
    }

    public static FestivalProto.SpectacolDTO createSpectacolDTOFromSpectDTO(SpectacolDTO spectacolDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return FestivalProto.SpectacolDTO.newBuilder()
                .setData(sdf.format(spectacolDTO.getData()))
                .setNrLocuriVandute(String.valueOf(spectacolDTO.getNrLocuriVandute()))
                .setNrLocuriDisponibile(String.valueOf(spectacolDTO.getNrLocuriDisp()))
                .setArtist(spectacolDTO.getArtist())
                .setLocatie(spectacolDTO.getLocatie())
                .setIdSpec(String.valueOf(spectacolDTO.getId_spectacol()))
                .build();
    }
    public static FestivalProto.FestivalResponse createGetSpectacoleResponse(SpectacolDTO[] spectacole) {
        List<FestivalProto.SpectacolDTO> spectacoleDTO = new ArrayList<>();
        for (SpectacolDTO spectacol : spectacole) {
            spectacoleDTO.add(createSpectacolDTOFromSpectDTO(spectacol));
        }

        return FestivalProto.FestivalResponse.newBuilder()
                .setResponseType(FestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_LIST_UPDATED).addAllSpectacole(spectacoleDTO).build();
    }

    public static FestivalProto.FestivalResponse createUserLoggedInResponse(String username, String password) {
        return FestivalProto.FestivalResponse.newBuilder()
                .setResponseType(FestivalProto.FestivalResponse.ResponseType.USER_LOGGED_IN)
                .setAngajat(FestivalProto.Angajat.newBuilder()
                        .setToken(username)
                        .setPassword(password)
                        .build())
                .build();
    }

    public static FestivalProto.FestivalResponse createUserLoggedOutResponse(String username) {
        return FestivalProto.FestivalResponse.newBuilder()
                .setResponseType(FestivalProto.FestivalResponse.ResponseType.USER_LOGGED_OUT)
                .setAngajat(FestivalProto.Angajat.newBuilder()
                        .setToken(username)
                        .build())
                .build();
    }
//imp
    public static FestivalProto.FestivalResponse createRecievedSpecResponse(SpectacolDTO spectacol, int nrLocuri, String nume) {
        return FestivalProto.FestivalResponse.newBuilder()
                .setResponseType(FestivalProto.FestivalResponse.ResponseType.GOT_FESTIVAL_UPDATED)
                .setSpectacol(FestivalProto.SpectacolDTO.newBuilder()
                        .setData(spectacol.getData().toString())
                        .setNrLocuriVandute(String.valueOf(spectacol.getNrLocuriVandute()))
                        .setNrLocuriDisponibile(String.valueOf(spectacol.getNrLocuriDisp()))
                        .setArtist(spectacol.getArtist())
                        .setLocatie(spectacol.getLocatie())
                        .setIdSpec(String.valueOf(spectacol.getId_spectacol()))
                        .build())
                .setBilet(FestivalProto.Bilet.newBuilder()
                        .setNumeCump(nume)
                        .setNrLocuri(String.valueOf(nrLocuri))
                        .setSpectacol(FestivalProto.SpectacolDTO.newBuilder()
                                .setIdSpec(String.valueOf(spectacol.getId_spectacol()))
                                .build())
                        .build())
                .build();
    }
}
