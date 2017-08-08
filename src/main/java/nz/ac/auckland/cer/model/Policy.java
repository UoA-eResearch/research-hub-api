package nz.ac.auckland.cer.model;


import com.fasterxml.jackson.annotation.JsonFilter;
import javax.persistence.*;
import java.util.Set;

@Entity
@JsonFilter(Policy.ENTITY_NAME)
public class Policy {
    public static final String ENTITY_NAME = "Policy";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String description;
    private String url;

    @ManyToMany(mappedBy = "policies")
    private Set<Content> contentItems;

    public Policy() {

    }

    public Policy(Integer id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}
