package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nz.ac.auckland.cer.model.categories.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@JsonFilter(Content.ENTITY_NAME)
public class Content {
    public static final String ENTITY_NAME = "Content";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String summary;
    private String description;
    private String actionableInfo;
    private String additionalInfo;
    private String image;

    @Column(name = "created", insertable = false, updatable = false) // Makes sure that the database updates the value automatically
    private LocalDateTime created;

    @Column(name = "updated", insertable = false, updatable = false) // Makes sure that the database updates the value automatically
    private LocalDateTime updated;

    private LocalDateTime audited;

    // Many to one
    @ManyToOne
    @JoinColumn(name = "content_type_id")
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private ContentType contentType;

    //Many to many
    @JsonFilter("researchPhases")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_research_phase", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "research_phase_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentItems", allowSetters=true)
    private Set<ResearchPhase> researchPhases;

    @JsonFilter("people")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "content_role", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Set<Person> people;

    // One to many
    @JsonFilter("externalUrls")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "content")
    @JsonIgnoreProperties(value = "content", allowSetters=true)
    private Set<ExternalUrl> externalUrls;


    public Content()
    {

    }

    public Content(ContentType contentType, String title, String summary, String description, String actionableInfo, String additionalInfo, String image) {
        this.contentType = contentType;
        this.title = title;
        this.summary = summary;
        this.description = description;
        this.actionableInfo = actionableInfo;
        this.additionalInfo = additionalInfo;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Set<ResearchPhase> getResearchPhases() {
        return researchPhases;
    }

    public void setResearchPhases(Set<ResearchPhase> researchPhases) {
        this.researchPhases = researchPhases;
    }

    public Set<ExternalUrl> getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(Set<ExternalUrl> externalUrls) {
        this.externalUrls = externalUrls;
    }

    public Set<Person> getPeople() {
        return people;
    }

    public void setPeople(Set<Person> people) {
        this.people = people;
    }
}
