package org.fullcycle.admin.catalogue.infrastructure.config.usecases;

import org.fullcycle.admin.catalogue.application.category.create.CreateCategoryUseCase;
import org.fullcycle.admin.catalogue.application.category.create.DefaultCreateCategoryUseCase;
import org.fullcycle.admin.catalogue.application.category.delete.DefaultDeleteCategoryUseCase;
import org.fullcycle.admin.catalogue.application.category.delete.DeleteCategoryUseCase;
import org.fullcycle.admin.catalogue.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import org.fullcycle.admin.catalogue.application.category.retrieve.get.GetCategoryByIdUseCase;
import org.fullcycle.admin.catalogue.application.category.retrieve.list.DefaultListCategoriesUseCase;
import org.fullcycle.admin.catalogue.application.category.retrieve.list.ListCategoryUseCase;
import org.fullcycle.admin.catalogue.application.category.update.DefaultUpdateCategoryUseCase;
import org.fullcycle.admin.catalogue.application.category.update.UpdateCategoryUseCase;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase(){
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoryUseCase listCategoryUseCase(){
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }
}
