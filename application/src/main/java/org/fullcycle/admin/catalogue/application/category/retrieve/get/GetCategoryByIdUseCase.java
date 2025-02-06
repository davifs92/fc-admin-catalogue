package org.fullcycle.admin.catalogue.application.category.retrieve.get;

import org.fullcycle.admin.catalogue.application.UseCase;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.fullcycle.admin.catalogue.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public abstract class GetCategoryByIdUseCase extends UseCase<String, CategoryOutput> {


}
