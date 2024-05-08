package festival.model.Validators;


import festival.model.Exceptions.ValidatorException;
import festival.model.Spectacol;
import festival.model.Validators.Interfaces.Validator;

public class SpectacolValidator implements Validator<Spectacol> {

    @Override
    public void validate(Spectacol entity) throws ValidatorException {
        String errors="";
        if(entity.getLocatie().isEmpty())
        {
            errors+="Locatia cannot be empty!\n";
        }
        if(entity.getNumar_locuri_disponibile()<0)
        {
            errors+="A negative number of available seats is not valid!\n";
        }
        if(entity.getNumar_locuri_vandute()<0)
        {
            errors+="A negative number of sold seats is not valid!\n";
        }
        if(entity.getArtist().isEmpty())
        {
            errors+="Artist cannot be empty!\n";
        }
        if(!errors.isEmpty())
        {
            throw new ValidatorException(errors);
        }
        }
}
