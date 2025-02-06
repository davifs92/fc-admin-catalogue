package org.fullcycle.admin.catalogue.application.category.delete;

import org.fullcycle.admin.catalogue.IntegrationTest;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@IntegrationTest
public class DeleteCategoryUseCaseTestIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository repository;

    @SpyBean
    private CategoryGateway gateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOK(){
        final var aCategory = Category.newCategory("Film", "The most watched category", true);

        final var expectedId = aCategory.getId();

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));
        assertEquals(1, repository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));
        assertEquals(0, repository.count());


    }

    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK(){

        final var aCategory = Category.newCategory("Film", "The most watched category", true);

        final var expectedId = CategoryID.from("123");

        assertEquals(0, repository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));


    }
    @Test
    public void givenAValidId_wheGatewayThrowsException_shouldReturnException(){

        final var aCategory = Category.newCategory("Film", "The most watched category", true);

        final var expectedId = aCategory.getId();

        repository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        doThrow(new IllegalStateException("Gateway error")).when(gateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    }
}
