package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
@JsonFilter(ContentSubtype.ENTITY_NAME)
public class ContentSubtype extends Category {

    public static final String ENTITY_NAME = "ContentSubtype";

    @ManyToMany(mappedBy = "contentSubtypes")
    private Set<Content> contentItems;

    public ContentSubtype() {
        super();
    }

    public ContentSubtype(String name) {
        super(name);
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}