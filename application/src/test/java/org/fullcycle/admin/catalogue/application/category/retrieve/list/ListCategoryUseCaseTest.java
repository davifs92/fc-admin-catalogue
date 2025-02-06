package org.fullcycle.admin.catalogue.application.category.retrieve.list;

import org.fullcycle.admin.catalogue.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategorySearchQuery;
import org.fullcycle.admin.catalogue.domain.pagination.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListCategoryUseCaseTest {
    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway gateway;

    @BeforeEach
    void cleanUp(){
        Mockito.reset(gateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories(){
        final var categories = of(
                Category.newCategory("Movies", null, true),
                Category.newCategory("Series", null, true)
        );
        final var expectedPage = 0 ;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =  new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedItemsCount = 2;

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        when(gateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());


    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories(){

        final var categories = List.<Category>of();

        final var expectedPage = 0 ;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery =  new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedItemsCount = 0;

        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        when(gateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);


        assertEquals(expectedItemsCount, actualResult.items().size());
        assertEquals(expectedResult, actualResult);
        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(categories.size(), actualResult.total());

    }
    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException(){

        final var expectedPage = 0 ;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =  new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);


        when(gateway.findAll(eq(aQuery))).thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = assertThrows(IllegalStateException.class, ()-> useCase.execute(aQuery));

        assertEquals(expectedErrorMessage, actualException.getMessage());


    }
}
