package org.fullcycle.admin.catalogue.infrastructure.category.persistence;

import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidName_whenCallSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Movies", "The most watched category", true);
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());

    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Movies", "The most watched category", true);
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());

    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Movies", "The most watched category", true);
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException =
                assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = assertInstanceOf(PropertyValueException.class, actualException.getCause());

        assertEquals(expectedPropertyName, actualCause.getPropertyName());
        assertEquals(expectedMessage, actualCause.getMessage());

    }
}
