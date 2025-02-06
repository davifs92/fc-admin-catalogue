package org.fullcycle.admin.catalogue.application.category.update;

import org.fullcycle.admin.catalogue.application.category.create.CreateCategoryCommand;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;


    @BeforeEach
    void cleanUp(){
        Mockito.reset(categoryGateway);
    }

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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(
                Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
        Objects.equals(expectedName, aUpdatedCategory.getName()) &&
                Objects.equals(expectedDescription, aUpdatedCategory.getDescription()) &&
                Objects.equals(expectedIsActive, aUpdatedCategory.isActive()) &&
                Objects.equals(expectedId, aUpdatedCategory.getId()) &&
                Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt()) &&
                aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()) &&
                Objects.isNull(aUpdatedCategory.getDeleteAt())
                ));

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


        when(categoryGateway.findById(eq(expectedId))).thenReturn(
                Optional.of(aCategory.clone()));


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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(
                Optional.of(aCategory.clone()));

        when(categoryGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        assertNotNull(actualOutput);
        assertNotNull(actualOutput.get().id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));

        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
                Objects.equals(expectedName, aUpdatedCategory.getName()) &&
                        Objects.equals(expectedDescription, aUpdatedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, aUpdatedCategory.isActive()) &&
                        Objects.equals(expectedId, aUpdatedCategory.getId()) &&
                        Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt()) &&
                        aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()) &&
                        Objects.nonNull(aUpdatedCategory.getDeleteAt())
        ));

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

        when(categoryGateway.findById(eq(expectedId))).thenReturn(
                Optional.of(aCategory.clone()));

        when(categoryGateway.update(any())).thenThrow(new IllegalStateException("Gateway error"));

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());


        verify(categoryGateway, times(1)).update(argThat(aUpdatedCategory ->
                Objects.equals(expectedName, aUpdatedCategory.getName()) &&
                        Objects.equals(expectedDescription, aUpdatedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, aUpdatedCategory.isActive()) &&
                        Objects.equals(expectedId, aUpdatedCategory.getId()) &&
                        Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt()) &&
                        aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()) &&
                        Objects.isNull(aUpdatedCategory.getDeleteAt())
        ));



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

        when(categoryGateway.findById(eq(CategoryID.from(expectedId.getValue())))).thenReturn(
                Optional.empty());

       final var actualException =  assertThrows(DomainException.class, () -> useCase.execute(aCommand));

       assertEquals(actualException.getErrors().get(0).message(), expectedErrorMessage);
       assertEquals(actualException.getErrors().size(), expectedErrorCount);

        verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId.getValue())));

        verify(categoryGateway, times(0)).update(argThat(aUpdatedCategory ->
                Objects.equals(expectedName, aUpdatedCategory.getName()) &&
                        Objects.equals(expectedDescription, aUpdatedCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, aUpdatedCategory.isActive()) &&
                        Objects.equals(expectedId, aUpdatedCategory.getId()) &&
                        Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt()) &&
                        aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()) &&
                        Objects.nonNull(aUpdatedCategory.getDeleteAt())
        ));

    }



}
