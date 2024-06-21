package festival.model.Validators;


import festival.model.Exceptions.ValidatorException;
import festival.model.Validators.Enums.ValidatorStrategy;
import festival.model.Validators.Interfaces.Factory;
import festival.model.Validators.Interfaces.Validator;

public class ValidatorFactory implements Factory {
    private static ValidatorFactory instance;
    @Override
    public Validator createValidator(ValidatorStrategy validatorStrategy) {
        switch (validatorStrategy)
        {
            case Angajat -> {return new AngajatValidator();}
            case Spectacol -> {return new SpectacolValidator();}
            default -> throw new ValidatorException("Invalid strategy for creating the validator!");
            }

    }
    public static ValidatorFactory getInstance()
    {
        if(instance==null)
        {
            instance=new ValidatorFactory();
        }
        return instance;
    }
}
