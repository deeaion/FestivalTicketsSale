package festival.model.Validators.Interfaces;


import festival.model.Validators.Enums.ValidatorStrategy;

public interface Factory {
    Validator createValidator(ValidatorStrategy validatorStrategy);
}
