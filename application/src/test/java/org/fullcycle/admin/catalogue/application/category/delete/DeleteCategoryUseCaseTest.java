package org.fullcycle.admin.catalogue.application.category.delete;

import net.bytebuddy.build.ToStringPlugin;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){
        final var aCategory = Category.newCategory("Film", "The most watched category", true);

        final var expectedId = aCategory.getId();

        Mockito.doNothing()
                .when(gateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(eq(expectedId));



    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK(){

        final var aCategory = Category.newCategory("Film", "The most watched category", true);

        final var expectedId = CategoryID.from("123");

        Mockito.doNothing()
                .when(gateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(eq(expectedId));

    }
    @Test
    public void givenAValidId_wheGatewayThrowsException_shouldReturnException(){

        final var aCategory = Category.newCategory("Film", "The most watched category", true);

        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(gateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        verify(gateway, times(1)).deleteById(eq(expectedId));

    }


}
