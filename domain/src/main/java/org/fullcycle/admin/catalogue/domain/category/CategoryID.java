package org.fullcycle.admin.catalogue.domain.category;

import org.fullcycle.admin.catalogue.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String value) {
        Objects.requireNonNull(value, "value cannot be null");
        this.value = value;
    }

    public static CategoryID unique(){
        return CategoryID.from(UUID.randomUUID());
    }

    public static CategoryID from (final String anId){
        return new CategoryID(anId);
    }

    public static CategoryID from(final UUID anId){
        return new CategoryID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
