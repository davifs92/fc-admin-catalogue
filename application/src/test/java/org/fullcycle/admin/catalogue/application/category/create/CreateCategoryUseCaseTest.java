package org.fullcycle.admin.catalogue.application.category.create;

import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;
    @Mock
    private CategoryGateway gateway;

    @Test
    void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(gateway, times(1))
                .create(argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName()) &&
                                    Objects.equals(expectedDescription, aCategory.getDescription()) &&
                                    Objects.equals(expectedIsActive, aCategory.isActive()) &&
                                    Objects.nonNull(aCategory.getId()) &&
                                    Objects.nonNull(aCategory.getCreatedAt()) &&
                                    Objects.nonNull(aCategory.getUpdatedAt()) &&
                                    Objects.isNull(aCategory.getDeleteAt());
                        }
                ));


    }
    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){

        final String expectedName = null;
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "name should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var notification = useCase.execute(aCommand).getLeft();

        assertEquals(expectedErrorMessage, notification.firstError().message());
        assertEquals(expectedErrorCount, notification.getErrors().size());

        verify(gateway, times(0) ).create(any());

    }

    @Test
    void givenAInvalidCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(gateway.create(any())).thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(gateway, times(1))
                .create(argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName()) &&
                                    Objects.equals(expectedDescription, aCategory.getDescription()) &&
                                    Objects.equals(expectedIsActive, aCategory.isActive()) &&
                                    Objects.nonNull(aCategory.getId()) &&
                                    Objects.nonNull(aCategory.getCreatedAt()) &&
                                    Objects.nonNull(aCategory.getUpdatedAt()) &&
                                    Objects.nonNull(aCategory.getDeleteAt());
                        }
                ));


    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(gateway.create(any())).thenThrow(new IllegalStateException("Gateway error"));

         final var notification = assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand).getLeft());

        assertEquals(expectedErrorMessage, notification.getMessage());
     //   assertEquals(expectedErrorCount, notification.);


       // verify(gateway, times(0) ).create(any());

          }
}
