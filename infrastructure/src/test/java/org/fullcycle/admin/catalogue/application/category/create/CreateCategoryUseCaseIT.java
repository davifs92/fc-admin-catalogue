package org.fullcycle.admin.catalogue.application.category.create;

import org.fullcycle.admin.catalogue.IntegrationTest;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        assertEquals(1, categoryRepository.count());

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.id());

       final var actualCategory
               =categoryRepository.findById(actualOutput.id().getValue()).get();

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNull(actualCategory.getDeletedAt());

    }

//    @Test
//    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){
//
//        final String expectedName = null;
//        final var expectedDescription = "The most watched category";
//        final var expectedIsActive = true;
//        final var expectedErrorMessage = "name should not be null";
//        final var expectedErrorCount = 1;
//
//
//        assertEquals(0, categoryRepository.count());
//
//        final var aCommand =
//                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
//
//        final var notification = useCase.execute(aCommand).getLeft();
//
//        assertEquals(expectedErrorMessage, notification.firstError().message());
//        assertEquals(expectedErrorCount, notification.getErrors().size());
//
//        assertEquals(0, categoryRepository.count());
//        verify(categoryGateway, times(0)).create(any());
//
//    }

    @Test
    void givenAValidInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategory() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        assertEquals(0, categoryRepository.count());


        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final var actualOutput = useCase.execute(aCommand).get();

        assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(actualOutput.id().getValue()).get();


        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertNotNull(actualCategory.getCreatedAt());
        assertNotNull(actualCategory.getUpdatedAt());
        assertNotNull(actualCategory.getDeletedAt());

    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).create(any());

        final var notification = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand).getLeft());

        assertEquals(expectedErrorMessage, notification.getMessage());

    }

}
