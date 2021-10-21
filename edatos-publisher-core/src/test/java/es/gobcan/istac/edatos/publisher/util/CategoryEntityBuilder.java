package es.gobcan.istac.edatos.publisher.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import es.gobcan.istac.edatos.publisher.core.domain.CategoryEntity;
import es.gobcan.istac.edatos.publisher.core.domain.vo.InternationalStringVO;

public class CategoryEntityBuilder {
    private Long id;
    private InternationalStringVO name;
    private Integer index;
    private CategoryEntity parent;
    private final Set<CategoryEntity> children = new HashSet<>();

    public static CategoryEntityBuilder categoryBuilder() {
        return new CategoryEntityBuilder();
    }

    public CategoryEntityBuilder setId(Long id) {
        this.id = id;
        return this;
    }

    public CategoryEntityBuilder setName(InternationalStringVO name) {
        this.name = name;
        return this;
    }

    public CategoryEntityBuilder setName(String locale, String label) {
        this.name = InternationalStringUtils.createIntStr(locale, label);
        return this;
    }

    public CategoryEntityBuilder setName(String label) {
        return setName("es", label);
    }

    public CategoryEntityBuilder addChild(CategoryEntity child) {
        this.children.add(child);
        return this;
    }

    public CategoryEntityBuilder addChildren(CategoryEntity... children) {
        this.children.addAll(Arrays.asList(children));
        return this;
    }

    public CategoryEntityBuilder setChildren(List<CategoryEntity> children) {
        this.children.clear();
        this.children.addAll(children);
        return this;
    }

    public CategoryEntityBuilder setIndex(Integer index) {
        this.index = index;
        return this;
    }

    public CategoryEntityBuilder setParent(CategoryEntity parent) {
        this.parent = parent;
        return this;
    }

    public CategoryEntity build() {
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        category.setName(name);
        category.setIndex(index == null ? 1 : index);
        category.setParent(parent);
        category.setChildren(children);
        return category;
    }
}
