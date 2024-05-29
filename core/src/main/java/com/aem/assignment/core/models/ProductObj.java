package com.aem.assignment.core.models;

/**
 * Pojo class created for entity used during learning of multi field.
 */
public class ProductObj {
    private String productTitle;
    private String productPrice;
    private String productImage;

    public ProductObj(String productTitle, String productPrice, String productImage) {
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.productImage = productImage;
    }
//===============Getters and Setters for the properties of ProductObj class===================//

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
