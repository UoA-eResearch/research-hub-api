package nz.ac.auckland.cer.model;

import javax.persistence.*;

@Entity
public class Product {
    private int id;
    private String name;
    private String summary;
    private String imageUri;

    private ProductCategory productCategory;
    private ProviderCategory providerCategory;

    public Product() {

    }

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, ProductCategory productCategory, ProviderCategory providerCategory, String summary, String imageUri) {
        this.name = name;
        this.productCategory = productCategory;
        this.providerCategory = providerCategory;
        this.summary = summary;
        this.imageUri = imageUri;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @ManyToOne
    @JoinColumn(name = "provider_category_id")
    public ProviderCategory getProviderCategory() {
        return providerCategory;
    }

    public void setProviderCategory(ProviderCategory providerCategory) {
        this.providerCategory = providerCategory;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
