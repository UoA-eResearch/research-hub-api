package nz.ac.auckland.cer.model;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String summary;
    private String imageUri;

    @ManyToOne
    @JoinColumn(name = "product_category_id")
    private ProductCategory productCategory;

    @ManyToOne
    @JoinColumn(name = "provider_category_id")
    private ProviderCategory providerCategory;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "product_life_cycle", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "life_cycle_id", referencedColumnName = "id"))
    private Set<LifeCycleCategory> lifeCycleCategories;

    public Product() {

    }

    public Product(String name) {
        this.name = name;
    }

    public Product(String name, ProductCategory productCategory, ProviderCategory providerCategory, String summary, String imageUri, Set<LifeCycleCategory> lifeCycleCategories) {
        this.name = name;
        this.productCategory = productCategory;
        this.providerCategory = providerCategory;
        this.summary = summary;
        this.imageUri = imageUri;
        this.lifeCycleCategories = lifeCycleCategories;
    }

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

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

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

    public Set<LifeCycleCategory> getLifeCycleCategories() {
        return lifeCycleCategories;
    }

    public void setLifecycleCategories(Set<LifeCycleCategory> lifecycleCategories) {
        this.lifeCycleCategories = lifecycleCategories;
    }
}
