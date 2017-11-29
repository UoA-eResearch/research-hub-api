package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@JsonFilter(Content.ENTITY_NAME)
@SqlResultSetMapping(
    name="ListItem",
    classes={
            @ConstructorResult(
                    targetClass=ListItem.class,
                    columns={
                            @ColumnResult(name="type"),
                            @ColumnResult(name="id", type=Integer.class),
                            @ColumnResult(name="title"),
                            @ColumnResult(name="subtitle"),
                            @ColumnResult(name="image"),
                            @ColumnResult(name="url"),
                            @ColumnResult(name="relevance", type=Float.class)
                    }
            )
    }
)
public class Content {



    public static final String ENTITY_NAME = "Content";
    public static final String[] DETAILS = new String[] {
            "webpages", "keywords", "orgUnits", "researchPhases", "people", "policies",
            "similarContentItems", "actionableInfo", "additionalInfo", "action", "description", "guideCategories"
    };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String summary;
    private String description;
    private String actionableInfo;
    private String additionalInfo;
    private String action;
    private String image;
    private String keywords;

    @Column(name = "created", insertable = false, updatable = false) // Makes sure that the database updates the value automatically
    private LocalDateTime created;

    @Column(name = "updated", insertable = false, updatable = false) // Makes sure that the database updates the value automatically
    private LocalDateTime updated;

    private LocalDateTime audited;

    @ManyToOne
    @JoinColumn(name = "action_type_id")
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private ActionType actionType;

    // One to many
    @JsonFilter("webpages")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
    @JsonIgnoreProperties(value = "content", allowSetters=true)
    private Set<Webpage> webpages;

    //Many to many
    @JsonFilter("contentTypes")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_content_type", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "content_type_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private Set<ContentType> contentTypes;

    @JsonFilter("policies")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_policy", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "policy_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private Set<Policy> policies;

    @JsonFilter("similarContentItems")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_to_content", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "similar_content_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private Set<Content> similarContentItems;

    @JsonFilter("orgUnits")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_org_unit", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "org_unit_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private Set<OrgUnit> orgUnits;

    @JsonFilter("researchPhases")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_research_phase", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "research_phase_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private Set<ResearchPhase> researchPhases;

    @JsonFilter("people")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "person_content_role", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Set<Person> people;

    // Guides
    @JsonFilter("guideCategories")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
    @JsonIgnoreProperties(value = {"content"}, allowSetters = true)
    private Set<GuideCategory> guideCategories;

    public Content()
    {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionableInfo() {
        return actionableInfo;
    }

    public void setActionableInfo(String actionableInfo) {
        this.actionableInfo = actionableInfo;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public LocalDateTime getAudited() {
        return audited;
    }

    public void setAudited(LocalDateTime audited) {
        this.audited = audited;
    }

    public Set<Webpage> getWebpages() {
        return webpages;
    }

    public void setWebpages(Set<Webpage> webpages) {
        this.webpages = webpages;
    }

    public Set<ContentType> getContentTypes() {
        return contentTypes;
    }

    public void setContentTypes(Set<ContentType> contentTypes) {
        this.contentTypes = contentTypes;
    }

    public Set<OrgUnit> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(Set<OrgUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public Set<ResearchPhase> getResearchPhases() {
        return researchPhases;
    }

    public void setResearchPhases(Set<ResearchPhase> researchPhases) {
        this.researchPhases = researchPhases;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }

    public Set<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(Set<Policy> policies) {
        this.policies = policies;
    }

    public Set<Content> getSimilarContentItems() {
        return similarContentItems;
    }

    public void setSimilarContentItems(Set<Content> similarContentItems) {
        this.similarContentItems = similarContentItems;
    }

    public Set<GuideCategory> getGuideCategories() {
        return guideCategories;
    }

    public void setGuideCategories(Set<GuideCategory> guideCategories) {
        this.guideCategories = guideCategories;
    }
}
