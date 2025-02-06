package org.fullcycle.admin.catalogue.domain.category;

import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.exceptions.DomainException;
import org.fullcycle.admin.catalogue.domain.validation.handler.ThrowsValidationHandler;
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

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldThrownAnException() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "name should not be null";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualcategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "name should not be empty";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualcategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Fi ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualcategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final String expectedName = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla sem lacus, malesuada vel felis vestibulum, semper fermentum ex. Maecenas quis sapien at tellus consectetur ultricies. Etiam quis lacus bibendum, placerat neque eu, commodo augue. Curabitur metus nisl, placerat nec velit eget, venenatis malesuada enim. Nulla risus lorem, rhoncus ac lorem tincidunt, porttitor commodo augue. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Praesent nec scelerisque nunc. Cras et consequat massa. Nulla facilisi. Pellentesque aliquet tellus sit amet varius posuere. Nulla pellentesque magna eget felis malesuada blandit. Duis ultricies neque in augue sodales, non faucibus sem congue. Cras justo dolor, varius ut vestibulum eu, egestas at tortor. Nullam ultrices risus et ligula vestibulum tempor. Morbi iaculis ornare erat, vitae vehicula purus iaculis in.\n" +
                "\n";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "name must be between 3 and 255 characters";
        final var expectedDescription = "Most watched category";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualcategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAEmptyDescription_whenCallNewCategory_thenShouldReceiveOk() {
        final var expectedName = "Movies";
        final var expectedDescription = " ";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualcategory);
        Assertions.assertNotNull(actualcategory.getId());
        Assertions.assertEquals(expectedName, actualcategory.getName());
        Assertions.assertEquals(expectedDescription, actualcategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualcategory.isActive());
        Assertions.assertNotNull(actualcategory.getCreatedAt());
        Assertions.assertNotNull(actualcategory.getUpdatedAt());
        Assertions.assertNull(actualcategory.getDeleteAt());

    }

    @Test
    public void givenAValidFalseIsActive_whenCallNewCategory_thenShouldReceiveOk() {
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = false;
        final var actualcategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualcategory);
        Assertions.assertNotNull(actualcategory.getId());
        Assertions.assertEquals(expectedName, actualcategory.getName());
        Assertions.assertEquals(expectedDescription, actualcategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualcategory.isActive());
        Assertions.assertNotNull(actualcategory.getCreatedAt());
        Assertions.assertNotNull(actualcategory.getUpdatedAt());
        Assertions.assertNotNull(actualcategory.getDeleteAt());

    }

    @Test
    public void givenAValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactive(){
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = false;
        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, true);

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertNull(aCategory.getDeleteAt());
        Assertions.assertTrue(aCategory.isActive());

       final var actualcategory =  aCategory.deactivate();


        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualcategory.getId());
        Assertions.assertEquals(expectedName, actualcategory.getName());
        Assertions.assertEquals(expectedDescription, actualcategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualcategory.isActive());
        Assertions.assertNotNull(actualcategory.getCreatedAt());
        Assertions.assertTrue(actualcategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualcategory.getDeleteAt());


    }
    @Test
    public void givenAValidInactiveCategory_whenCallActivate_thenReturnCategoryActive(){
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, true);

        final var updatedAt = aCategory.getUpdatedAt();

        Assertions.assertNull(aCategory.getDeleteAt());
        Assertions.assertTrue(aCategory.isActive());

        final var actualcategory =  aCategory.activate();


        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualcategory.getId());
        Assertions.assertEquals(expectedName, actualcategory.getName());
        Assertions.assertEquals(expectedDescription, actualcategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualcategory.isActive());
        Assertions.assertNotNull(actualcategory.getCreatedAt());
        Assertions.assertTrue(actualcategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualcategory.getDeleteAt());


    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdate(){
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory("Film", "The category", expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));

        final var createdAt = actualcategory.getCreatedAt();
        final var updatedAt = actualcategory.getUpdatedAt();

       final var updatedCategory = actualcategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> updatedCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeleteAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated(){
        final var expectedName = "Movies";
        final var expectedDescription = "Most watched";
        final var expectedIsActive = false;
        final var actualcategory =
                Category.newCategory("Film", "The category", true);

        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));
        Assertions.assertTrue(actualcategory.isActive());
        Assertions.assertNull(actualcategory.getDeleteAt());

        final var createdAt = actualcategory.getCreatedAt();
        final var updatedAt = actualcategory.getUpdatedAt();

        final var updatedCategory = actualcategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> updatedCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(updatedCategory.getDeleteAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated(){
        final String expectedName = null;
        final var expectedDescription = "Most watched";
        final var expectedIsActive = true;
        final var actualcategory =
                Category.newCategory("Film", "The category", true);

        Assertions.assertDoesNotThrow(() -> actualcategory.validate(new ThrowsValidationHandler()));

        final var createdAt = actualcategory.getCreatedAt();
        final var updatedAt = actualcategory.getUpdatedAt();

        final var updatedCategory = actualcategory.update(expectedName, expectedDescription, expectedIsActive);

      //  Assertions.assertDoesNotThrow(() -> updatedCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(updatedCategory.getId());
        Assertions.assertEquals(expectedName, updatedCategory.getName());
        Assertions.assertEquals(expectedDescription, updatedCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, updatedCategory.isActive());
        Assertions.assertEquals(createdAt, updatedCategory.getCreatedAt());
        Assertions.assertTrue(updatedCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(updatedCategory.getDeleteAt());
    }

}
