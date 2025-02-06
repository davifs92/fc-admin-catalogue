package org.fullcycle.admin.catalogue.application.category.retrieve.list;

import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategorySearchQuery;
import org.fullcycle.admin.catalogue.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery aQuery) {
        return this.categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
