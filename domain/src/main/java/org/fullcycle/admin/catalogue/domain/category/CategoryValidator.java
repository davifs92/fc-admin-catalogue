package org.fullcycle.admin.catalogue.domain.category;

import org.fullcycle.admin.catalogue.domain.validation.Error;
import org.fullcycle.admin.catalogue.domain.validation.ValidationHandler;
import org.fullcycle.admin.catalogue.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private Category category;

    public static final int MIN_LENGTH = 3;
    public static final int MAX_LENGTH = 255;

    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if(name == null){
            this.validation().append(new Error( "name should not be null"));
            return;
        }

        if(name.isBlank()){
            this.validation().append(new Error( "name should not be empty"));
            return;
        }
        if(name.length() > MAX_LENGTH || name.trim().length() < MIN_LENGTH){
            this.validation().append(new Error("name must be between 3 and 255 characters"));
            return;
        }
    }
}
