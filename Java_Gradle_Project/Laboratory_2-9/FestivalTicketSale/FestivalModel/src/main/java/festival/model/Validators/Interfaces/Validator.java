package festival.model.Validators.Interfaces;


import festival.model.Exceptions.ValidatorException;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
