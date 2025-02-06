package org.fullcycle.admin.catalogue.application.category.update;

import org.fullcycle.admin.catalogue.IntegrationTest;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseTestIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);


        categoryRepository.save(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        var aUpdatedCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertEquals(expectedName, aUpdatedCategory.getName());
        assertEquals(expectedDescription, aUpdatedCategory.getDescription());
        assertEquals(expectedIsActive, aUpdatedCategory.isActive());
        assertEquals(expectedId.getValue(), aUpdatedCategory.getId());
       // assertEquals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()));

    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory = Category.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "name should not be null";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);


        categoryRepository.save(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());


        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(categoryGateway, times(0) ).update(any());
    }

    @Test
    void givenAValidInactiveCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var aCategory = Category.newCategory("Film", null, true);

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeleteAt());

        final var expectedId = aCategory.getId();
        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        categoryRepository.save(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

             final var actualOutput = useCase.execute(aCommand);
             final var aUpdatedCategory = categoryRepository.findById(expectedId.getValue()).get();

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());


        assertEquals(expectedName, aUpdatedCategory.getName());
        assertEquals(expectedDescription, aUpdatedCategory.getDescription());
        assertEquals(expectedIsActive, aUpdatedCategory.isActive());
        assertEquals(expectedId.getValue(), aUpdatedCategory.getId());
        // assertEquals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()));

    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {

        final var aCategory = Category.newCategory("Film", null, true);

        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;
        final var expectedId = aCategory.getId();


        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        categoryRepository.save(CategoryJpaEntity.from(aCategory));
        assertEquals(1, categoryRepository.count());

        doThrow(new IllegalStateException("Gateway error")).when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());


    }

    @Test
    void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;
        final var expectedErrorCount = 1;
        final var aCategory = Category.newCategory("Film", null, true);

        assertTrue(aCategory.isActive());
        assertNull(aCategory.getDeleteAt());

        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Category with ID " + expectedId.getValue() + " was not found";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive);

        final var actualException =  assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);
        assertEquals(actualException.getErrors().size(), expectedErrorCount);

        verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId.getValue())));


    }

}
