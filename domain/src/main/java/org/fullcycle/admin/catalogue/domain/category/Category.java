package org.fullcycle.admin.catalogue.domain.category;

import org.fullcycle.admin.catalogue.domain.AggregateRoot;
import org.fullcycle.admin.catalogue.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deleteAt;

    private Category(
                    final CategoryID anId,
                    final String aName,
                    final String aDescription,
                    final boolean isActive,
                    final Instant aCreatedAt,
                    final Instant anUpdatedAt,
                    final Instant aDeleteAt
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "createdAt should not be null");
        this.updatedAt = Objects.requireNonNull(anUpdatedAt, "updatedAt should not be null");
        this.deleteAt = aDeleteAt;
    }

    public static Category newCategory(final String name, final String description, final boolean isActive){
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(anId, name, description, isActive, createdAt, updatedAt, deletedAt);
    }


    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();

    }

    public Category deactivate(){
        if (getDeleteAt() == null){
            this.deleteAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate(){
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String aName,
                           final String aDescription,
                           final boolean isActive){
        this.name = aName;
        this.description = aDescription;
        if(isActive){
            activate();
        } else {
            deactivate();
        }
        this.updatedAt = Instant.now();
        return this;
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeleteAt() {
        return deleteAt;
    }

    @Override
    public Category clone() {
        try {
           return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
