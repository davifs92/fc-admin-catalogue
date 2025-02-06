package org.fullcycle.admin.catalogue.application.category.delete;

import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;

import java.util.Objects;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(String anId) {
    this.categoryGateway.deleteById(CategoryID.from(anId));
    }
}
