package org.fullcycle.admin.catalogue.application.category.retrieve.get;

import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;

import java.time.Instant;

public record CategoryOutput(String name,
                             CategoryID id,
                             String description,
                             boolean isActive,
                             Instant createdAt,
                             Instant updatedAt,
                             Instant deletedAt) {

    public static CategoryOutput from (final Category aCategory){
        return new CategoryOutput(
                aCategory.getName(),
                aCategory.getId(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeleteAt()
        );

    }
}
