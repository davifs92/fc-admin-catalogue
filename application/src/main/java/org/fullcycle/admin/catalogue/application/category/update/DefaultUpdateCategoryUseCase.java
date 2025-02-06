package org.fullcycle.admin.catalogue.application.category.update;

import io.vavr.API;
import io.vavr.control.Either;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.fullcycle.admin.catalogue.domain.validation.Error;
import org.fullcycle.admin.catalogue.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Left;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
       final var aName =  aCommand.name();
       final var aDescription = aCommand.description();
       final var anId = CategoryID.from(aCommand.id());
       final var isActive = aCommand.isActive();

      final var aCategory =  this.categoryGateway.findById(anId).orElseThrow(
               notFound(anId));

      final var notification = Notification.create();

       aCategory.update(aName, aDescription, isActive)
               .validate(notification);

       return notification.hasErrors() ? Left(notification) : update(aCategory);

    }

    private Either<Notification, UpdateCategoryOutput> update(Category aCategory) {
        return API.Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId.getValue())));
    }
}
