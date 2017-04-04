package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nz.ac.auckland.cer.model.categories.*;
import nz.ac.auckland.cer.model.content.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonFilter("Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String summary;
    private String imageUri;

    @ManyToOne
    @JoinColumn(name = "product_type_id")
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "provider_id")
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Provider provider;

    @JsonFilter("lifeCycleStages")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_life_cycle", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "life_cycle_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Set<LifeCycle> lifeCycleStages;

    @JsonFilter("eligibleGroups")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_eligibility", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "eligibility_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Set<Eligibility> eligibleGroups;

    @JsonFilter("serviceTypes")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_service_type", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "service_type_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Set<ServiceType> serviceTypes;

    @JsonFilter("programmes")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_programme", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "programme_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Set<Programme> programmes;

    @JsonFilter("studyLevels")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_study_level", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "study_level_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Set<StudyLevel> studyLevels;

    @JsonFilter("costs")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "product_cost", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "cost_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "products", allowSetters=true)
    private Set<Cost> costs;

    // Content
    @JsonFilter("requirements")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Requirement> requirements;

    @JsonFilter("features")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Feature> features;

    @JsonFilter("limitations")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Limitation> limitations;

    @JsonFilter("considerations")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Consideration> considerations;

    @JsonFilter("support")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Support> support;

    @JsonFilter("references")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Reference> references;

    @JsonFilter("contacts")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = "product", allowSetters=true)
    private Set<Contact> contacts;


    public Product()
    {

    }

    public Product(String name, String summary, String imageUri, ProductType productType, Provider provider) {
        this.name = name;
        this.summary = summary;
        this.imageUri = imageUri;
        this.productType = productType;
        this.provider = provider;
    }

    // To create a servie
    public Product(String name, String summary, String imageUri, ProductType productType, Provider provider, Set<LifeCycle> lifeCycleStages, Set<Eligibility> eligibleGroups, Set<ServiceType> serviceTypes, Set<Cost> costs, Set<Requirement> requirements, Set<Feature> features, Set<Limitation> limitations, Set<Consideration> considerations, Set<Support> support, Set<Reference> references, Set<Contact> contacts) {
        this.name = name;
        this.summary = summary;
        this.imageUri = imageUri;
        this.productType = productType;
        this.provider = provider;
        this.lifeCycleStages = lifeCycleStages;
        this.eligibleGroups = eligibleGroups;
        this.serviceTypes = serviceTypes;
        this.costs = costs;
        this.requirements = requirements;
        this.features = features;
        this.limitations = limitations;
        this.considerations = considerations;
        this.support = support;
        this.references = references;
        this.contacts = contacts;
    }

    //To create an educational resource
    public Product(String name, String summary, String imageUri, ProductType productType, Provider provider, Set<LifeCycle> lifeCycleStages, Set<Eligibility> eligibleGroups, Set<Programme> programmes, Set<StudyLevel> studyLevels, Set<Cost> costs, Set<Contact> contacts) {
        this.name = name;
        this.summary = summary;
        this.imageUri = imageUri;
        this.productType = productType;
        this.provider = provider;
        this.lifeCycleStages = lifeCycleStages;
        this.eligibleGroups = eligibleGroups;
        this.programmes = programmes;
        this.studyLevels = studyLevels;
        this.costs = costs;
        this.contacts = contacts;
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

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public Set<LifeCycle> getLifeCycleStages() {
        return lifeCycleStages;
    }

    public void setLifeCycleStages(Set<LifeCycle> lifeCycleStages) {
        this.lifeCycleStages = lifeCycleStages;
    }

    public Set<Eligibility> getEligibleGroups() {
        return eligibleGroups;
    }

    public void setEligibleGroups(Set<Eligibility> eligibleGroups) {
        this.eligibleGroups = eligibleGroups;
    }

    public Set<ServiceType> getServiceTypes() {
        return serviceTypes;
    }

    public void setServiceTypes(Set<ServiceType> serviceTypes) {
        this.serviceTypes = serviceTypes;
    }

    public Set<Programme> getProgrammes() {
        return programmes;
    }

    public void setProgrammes(Set<Programme> programmes) {
        this.programmes = programmes;
    }

    public Set<StudyLevel> getStudyLevels() {
        return studyLevels;
    }

    public void setStudyLevels(Set<StudyLevel> studyLevels) {
        this.studyLevels = studyLevels;
    }

    public Set<Cost> getCosts() {
        return costs;
    }

    public void setCosts(Set<Cost> costs) {
        this.costs = costs;
    }

    public Set<Requirement> getRequirements() {
        return requirements;
    }

    public void setRequirements(Set<Requirement> requirements) {
        this.requirements = requirements;
    }

    public Set<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(Set<Feature> features) {
        this.features = features;
    }

    public Set<Limitation> getLimitations() {
        return limitations;
    }

    public void setLimitations(Set<Limitation> limitations) {
        this.limitations = limitations;
    }

    public Set<Consideration> getConsiderations() {
        return considerations;
    }

    public void setConsiderations(Set<Consideration> considerations) {
        this.considerations = considerations;
    }

    public Set<Support> getSupport() {
        return support;
    }

    public void setSupport(Set<Support> support) {
        this.support = support;
    }

    public Set<Reference> getReferences() {
        return references;
    }

    public void setReferences(Set<Reference> references) {
        this.references = references;
    }

    public Set<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(Set<Contact> contacts) {
        this.contacts = contacts;
    }
}
