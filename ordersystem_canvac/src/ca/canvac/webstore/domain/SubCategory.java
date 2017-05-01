package ca.canvac.webstore.domain;

import java.io.Serializable;


public class SubCategory implements Serializable {
    private String categoryId;
    private String description;
    private String name;

    /* Private Fields */

    private String subCategoryId;

    public String getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    /* JavaBeans Properties */

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    /* Public Methods*/

    public String toString() {
        return getName();
    }

}
