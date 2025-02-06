package org.fullcycle.admin.catalogue.domain.exceptions;

import org.fullcycle.admin.catalogue.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException{

    private final List<Error> errors;

    private DomainException(final List<Error> anErrors){
        super("");
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anError){
        return new DomainException(anError);
    }

    public static DomainException with(final Error anError){
        return new DomainException(List.of(anError));
    }

    public List<Error> getErrors() {
        return errors;
    }
}
