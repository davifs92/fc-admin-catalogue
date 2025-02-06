package org.fullcycle.admin.catalogue.application.category.create;

import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput from(final Category aCategory){
        return new CreateCategoryOutput(aCategory.getId());
    }
}
