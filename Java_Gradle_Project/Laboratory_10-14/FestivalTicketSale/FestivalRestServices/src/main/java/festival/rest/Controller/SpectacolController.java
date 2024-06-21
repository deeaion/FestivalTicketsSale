package festival.rest.Controller;

import festival.model.Spectacol;
import festival.rest.Services.ISpectacolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/spectacol-management/spectacole")
public class SpectacolController {
    @Autowired
    private ISpectacolService serviceSpectacol;
    @RequestMapping(method= RequestMethod.POST)
    SpectacolDTOR addSpectacol(@RequestBody SpectacolDTOR spectacolDTO)
    {
        return Util.convertSpectacolToSpectacolDTOR(serviceSpectacol.addSpectacol(spectacolDTO.getData(), spectacolDTO.getLocatie(), spectacolDTO.getNrLocuriDisponibile(), spectacolDTO.getNrLocuriVandute(), spectacolDTO.getArtist() ) );
    }
    @RequestMapping(method= RequestMethod.PUT)
    SpectacolDTOR updateSpectacol(@RequestBody SpectacolDTOR spectacolDTO)
    {
        return Util.convertSpectacolToSpectacolDTOR(serviceSpectacol.updateSpectacol(spectacolDTO.getId(), spectacolDTO.getData(), spectacolDTO.getLocatie(), spectacolDTO.getNrLocuriDisponibile(), spectacolDTO.getNrLocuriVandute(), spectacolDTO.getArtist() ) );
    }
    @RequestMapping(value="/{id}",method= RequestMethod.DELETE)
    SpectacolDTOR deleteSpectacol(@PathVariable Long id){
        return Util.convertSpectacolToSpectacolDTOR(serviceSpectacol.deleteSpectacol(id));
    }
    @RequestMapping(method= RequestMethod.GET)
    List<SpectacolDTOR> getAllSpectacole(){
        return Util.convertSpectacoleToSpectacoleDTOR( serviceSpectacol.getAllSpectacole());
    }
    @RequestMapping(value="/{id}",method= RequestMethod.GET)
    SpectacolDTOR getSpectacol(@PathVariable Long id)
    {
        return Util.convertSpectacolToSpectacolDTOR(serviceSpectacol.getSpectacol(id));
    }
}
