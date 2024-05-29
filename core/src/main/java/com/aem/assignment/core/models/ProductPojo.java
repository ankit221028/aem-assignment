package com.aem.assignment.core.models;

/**
 * Pojo class created for entity used during learning of multi field.
 */
public class ProductPojo {
    private String title;
    private String description;
    private String imagePath;

    private Integer id;

    public ProductPojo(String title, String description, String imagePath, Integer id) {
        this.title = title;
        this.description = description;
        this.imagePath = imagePath;
        this.id = id;
    }


}
