package org.fullcycle.admin.catalogue.application;

import org.fullcycle.admin.catalogue.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIN);
}
