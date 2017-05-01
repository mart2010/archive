package ca.canvac.webstore.domain;

import java.io.Serializable;

public class Category implements Serializable {

    /* Private Fields */

    private String categoryId;

    private String description;

    private String name;

    /* JavaBeans Properties */

    public String getCategoryId() {
        return categoryId;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
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

    /* Public Methods */

    public String toString() {
        return String.valueOf(getCategoryId());
    }

}