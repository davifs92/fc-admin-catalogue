package org.fullcycle.admin.catalogue.application.category.create;

import io.vavr.control.Either;
import org.fullcycle.admin.catalogue.application.UseCase;
import org.fullcycle.admin.catalogue.domain.validation.handler.Notification;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
