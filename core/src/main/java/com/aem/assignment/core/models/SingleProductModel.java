package com.aem.assignment.core.models;

import com.aem.assignment.core.entities.ProductEntity;

/**
 * The interface for the model which is used to get the single product from the product_id provided in the URL as suffix.
 */
public interface SingleProductModel {
    /**
     * Retrieves the product entity based on the product ID extracted from the URL.
     *
     * @return The product entity retrieved based on the product ID.
     */
    public ProductEntity getProductEntity();
}
