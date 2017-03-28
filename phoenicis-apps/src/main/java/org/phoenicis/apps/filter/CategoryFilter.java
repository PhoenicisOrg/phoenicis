package org.phoenicis.apps.filter;

import org.phoenicis.apps.dto.ApplicationDTO;
import org.phoenicis.apps.dto.CategoryDTO;

/**
 * Created by marc on 28.03.17.
 */
public class CategoryFilter implements AppsFilter {
    private final CategoryDTO category;

    public CategoryFilter(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean applies(ApplicationDTO applicationDTO) {
        return this.category.getApplications().contains(applicationDTO);
    }
}
