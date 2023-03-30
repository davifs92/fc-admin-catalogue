package org.fullcycle.admin.catalogue.domain.category;

import org.fullcycle.admin.catalogue.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateACategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var actualcategory =
        Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualcategory);
        Assertions.assertNotNull(actualcategory.getId());
        Assertions.assertEquals(expectedName, actualcategory.getName());
        Assertions.assertEquals(expectedDescription, actualcategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualcategory.isActive());
        Assertions.assertNotNull(actualcategory.getCreatedAt());
        Assertions.assertNotNull(actualcategory.getUpdatedAt());
        Assertions.assertNull(actualcategory.getDeleteAt());

    }
}
