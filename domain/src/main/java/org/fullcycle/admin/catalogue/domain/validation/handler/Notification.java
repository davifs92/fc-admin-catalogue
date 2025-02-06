package org.fullcycle.admin.catalogue.domain.validation.handler;

import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.fullcycle.admin.catalogue.domain.validation.Error;
import org.fullcycle.admin.catalogue.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private List<Error> errors;


    public Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create() {
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error anError){
        return (Notification) new Notification(new ArrayList<>()).append(anError);
    }

    public static Notification create(final Throwable t){
        return create(new Error(t.getMessage()));
    }

    @Override
    public ValidationHandler append(final Error anError) {
       this.errors.add(anError);
       return this;
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        this.errors.addAll(handler.getErrors());
        return this;
    }

    @Override
    public ValidationHandler validate(Validation aValidation) {
        try {
            aValidation.validate();
        } catch(final DomainException ex){
            this.errors.addAll(ex.getErrors());
        } catch (final Throwable t) {
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
