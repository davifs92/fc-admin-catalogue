package org.fullcycle.admin.catalogue.application.category.retrieve.get;

import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.fullcycle.admin.catalogue.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(String anIN) {
        final var anCategoryId = CategoryID.from(anIN);
        return this.categoryGateway.findById(CategoryID.from(anIN))
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anCategoryId));
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }}
