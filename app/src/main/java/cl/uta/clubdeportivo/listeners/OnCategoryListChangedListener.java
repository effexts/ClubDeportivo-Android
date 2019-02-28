package cl.uta.clubdeportivo.listeners;


import cl.uta.clubdeportivo.api.models.category.Category;

import java.util.List;

/**

 */
public interface OnCategoryListChangedListener {
    void onListChanged(List<Category> categories);
}
