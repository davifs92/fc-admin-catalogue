package org.fullcycle.admin.catalogue.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler){
        this.handler = aHandler;
    }

    public abstract void validate();

    protected ValidationHandler validation(){
        return this.handler;
    }
}
