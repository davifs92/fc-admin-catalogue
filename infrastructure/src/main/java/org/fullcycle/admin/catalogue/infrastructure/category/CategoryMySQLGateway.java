package org.fullcycle.admin.catalogue.infrastructure.category;

import org.fullcycle.admin.catalogue.domain.category.Category;
import org.fullcycle.admin.catalogue.domain.category.CategoryGateway;
import org.fullcycle.admin.catalogue.domain.category.CategoryID;
import org.fullcycle.admin.catalogue.domain.category.CategorySearchQuery;
import org.fullcycle.admin.catalogue.domain.pagination.Pagination;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryJpaEntity;
import org.fullcycle.admin.catalogue.infrastructure.category.persistence.CategoryRepository;
import org.fullcycle.admin.catalogue.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.Optional;

import static org.fullcycle.admin.catalogue.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySQLGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anID) {
        final var anIdValue = anID.getValue();
       if(this.categoryRepository.existsById(anIdValue)){
           categoryRepository.deleteById(anIdValue);
       }
    }

    @Override
    public Optional<Category> findById(final CategoryID anID) {
        return this.categoryRepository.findById(anID.getValue())
                .map(CategoryJpaEntity::toAggregate);

    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    private Category save(Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

       final var specifications =  Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(str ->
                    SpecificationUtils.
                            <CategoryJpaEntity>like("name", str)
                            .or(SpecificationUtils.like("description", str))

                )
                .orElse(null);

       final var pageResult = this.categoryRepository.findAll(Specification.where(specifications), page);

       return new Pagination<>(
               pageResult.getNumber(),
               pageResult.getSize(),
               pageResult.getTotalElements(),
               pageResult.map(CategoryJpaEntity::toAggregate).toList()
       );
    }
}
