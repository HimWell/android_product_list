package com.example.thebatproducts.Models;

public class Product {

    private String productId;
    private String productName;
    private String productShortDescription;
    private String productLongDescription;
    private String productPrice;
    private String productSpecs;
    private String productImageURL;

    public Product() {
    }

    public Product(String productId, String productName, String productShortDescription, String productLongDescription, String productPrice, String productSpecs, String productImageURL) {
        this.productId = productId;
        this.productName = productName;
        this.productShortDescription = productShortDescription;
        this.productLongDescription = productLongDescription;
        this.productPrice = productPrice;
        this.productSpecs = productSpecs;
        this.productImageURL = productImageURL;
    }

    public Product(String productName, String productShortDescription, String productLongDescription, String productPrice, String productSpecs, String productImageURL) {
        this.productName = productName;
        this.productShortDescription = productShortDescription;
        this.productLongDescription = productLongDescription;
        this.productPrice = productPrice;
        this.productSpecs = productSpecs;
        this.productImageURL = productImageURL;
    }

    public Product(String productName, String productShortDescription, String productPrice) {
        this.productName = productName;
        this.productLongDescription = productShortDescription;
        this.productPrice = productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductShortDescription() {
        return productShortDescription;
    }

    public void setProductShortDescription(String productShortDescription) {
        this.productShortDescription = productShortDescription;
    }

    public String getProductLongDescription() {
        return productLongDescription;
    }

    public void setProductLongDescription(String productLongDescription) {
        this.productLongDescription = productLongDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSpecs() {
        return productSpecs;
    }

    public void setProductSpecs(String productSpecs) {
        this.productSpecs = productSpecs;
    }

    public String getProductImageURL() {
        return productImageURL;
    }

    public void setProductImageURL(String productImageURL) {
        this.productImageURL = productImageURL;
    }
}
