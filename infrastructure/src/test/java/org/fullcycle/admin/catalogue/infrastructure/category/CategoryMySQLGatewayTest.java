package org.fullcycle.admin.catalogue.infrastructure.category;

import org.fullcycle.admin.catalogue.MySQLGatewayTest;
import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.category.CategorySearchQuery;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjectedDependencies(){
        assertNotNull(categoryRepository);
        assertNotNull(categoryGateway);

    }

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory(){
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(aCategory);
        assertEquals(1, categoryRepository.count());
        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertNull(actualCategory.getDeleteAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertNull(actualEntity.getDeletedAt());
        assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated(){
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory("Film", null, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        final var aUpdatedCategory = aCategory.clone()
                .update(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = categoryGateway.update(aUpdatedCategory);

        assertEquals(1, categoryRepository.count());

        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertNull(actualCategory.getDeleteAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));

        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        assertEquals(expectedDescription, actualEntity.getDescription());
        assertEquals(expectedName, actualEntity.getName());
        assertEquals(expectedIsActive, actualEntity.isActive());
        assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        assertNull(actualEntity.getDeletedAt());
        assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){
        final var aCategory = Category.newCategory("Movies", null, true);
        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());

        categoryGateway.deleteById(aCategory.getId());

        assertEquals(0, categoryRepository.count());


    }

    @Test
    public void givenInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory(){

        assertEquals(0, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("invalid"));

        assertEquals(0, categoryRepository.count());

    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenFindById_shouldReturnCategory(){
        final var expectedName = "Movies";
        final var expectedDescription = "The most watched category";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        assertEquals(1, categoryRepository.count());


        final var actualCategory = categoryGateway.findById(aCategory.getId()).get();

        assertEquals(1, categoryRepository.count());

        assertEquals(expectedDescription, actualCategory.getDescription());
        assertEquals(expectedName, actualCategory.getName());
        assertEquals(expectedIsActive, actualCategory.isActive());
        assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        assertEquals(aCategory.getId(), actualCategory.getId());
        assertNull(actualCategory.getDeleteAt());
        assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());

    }

    @Test
    public void givenValidCategoryIdNotStored_whenFindById_shouldReturnEmpty(){
        assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.findById(CategoryID.from("1234"));

        assertFalse(actualCategory.isPresent());

    }
    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("movies", null, true);
        final var series = Category.newCategory("series", null, true);
        final var documentary = Category.newCategory("documentary", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentary.getId(), actualResult.items().get(0).getId());


    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, categoryRepository.count());


        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(0, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated(){
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("movies", null, true);
        final var series = Category.newCategory("series", null, true);
        final var documentary = Category.newCategory("documentary", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        var actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentary.getId(), actualResult.items().get(0).getId());

        // Page 1
        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc");
        actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(movies.getId(), actualResult.items().get(0).getId());

        // Page 2
        expectedPage = 2;
        query = new CategorySearchQuery(2, 1, "", "name", "asc");
        actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(series.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("movies", null, true);
        final var series = Category.newCategory("series", null, true);
        final var documentary = Category.newCategory("documentary", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "doc", "name", "asc");
        final var actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(documentary.getId(), actualResult.items().get(0).getId());


    }

    @Test
    public void givenPrePersistedCategoriesAndMostWatchedAsTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginated(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("movies", "The most watched", true);
        final var series = Category.newCategory("series", "A watchable category", true);
        final var documentary = Category.newCategory("documentary", "Least watched category", true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentary)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "most watched", "name", "asc");
        final var actualResult =  categoryGateway.findAll(query);

        assertEquals(expectedPage, actualResult.currentPage());
        assertEquals(expectedPerPage, actualResult.perPage());
        assertEquals(expectedTotal, actualResult.total());
        assertEquals(expectedPerPage, actualResult.items().size());
        assertEquals(movies.getId(), actualResult.items().get(0).getId());


    }


}
