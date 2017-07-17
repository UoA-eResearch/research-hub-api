package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.*;
import java.util.Set;


@Entity
@JsonFilter(ResearchPhase.ENTITY_NAME)
public class ResearchPhase extends Category {

    public static final String ENTITY_NAME = "ResearchPhase";

    @ManyToMany(mappedBy = "researchPhases")
    private Set<Content> contentItems;

    public ResearchPhase() {
        super();
    }

    public ResearchPhase(String name) {
        super(name);
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}