package org.fullcycle.admin.catalogue.application.category.retrieve.list;

import org.fullcycle.admin.catalogue.application.UseCase;
import org.fullcycle.admin.catalogue.domain.category.CategorySearchQuery;
import org.fullcycle.admin.catalogue.domain.pagination.Pagination;

public abstract class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
