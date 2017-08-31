package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;


@Entity
@JsonFilter(GuideCategory.ENTITY_NAME)
public class GuideCategory {
    public static final String ENTITY_NAME = "GuideCategory";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    // Parent
    @ManyToOne
    @JoinColumn(name="content_id")
    @JsonIgnoreProperties(value = {"contentItems", "similarContentItems"}, allowSetters=true)
    private Content content;

    private String name;
    private String summary;
    private String description;
    private String additionalInfo;
    private String icon;
    private int displayOrder;

    @JsonFilter("contentItems")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "guide_category_content", joinColumns = @JoinColumn(name = "guide_category_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"))
//    @JsonIgnoreProperties(value = {"contentItems", "similarContentItems"}, allowSetters=true)
    private Set<Content> contentItems;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}
