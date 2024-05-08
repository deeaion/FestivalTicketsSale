package org.example.Model.Validators.Interfaces;

import org.example.Repository.Exceptions.ValidatorException;

public interface Validator<T> {
    void validate(T entity) throws ValidatorException;
}
