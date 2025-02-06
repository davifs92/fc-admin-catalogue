package org.fullcycle.admin.catalogue.application.category.update;

import io.vavr.control.Either;
import org.fullcycle.admin.catalogue.application.UseCase;
import org.fullcycle.admin.catalogue.domain.validation.handler.Notification;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>>
{
}
