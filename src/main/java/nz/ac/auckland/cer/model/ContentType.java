package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
@JsonFilter(ContentType.ENTITY_NAME)
public class ContentType extends Category {

    public static final String ENTITY_NAME = "ContentType";

    @ManyToMany(mappedBy = "contentTypes")
    private Set<Content> contentItems;

    public ContentType() {
        super();
    }

    public ContentType(Integer id) {
        super(id);
    }

    public ContentType(String name) {
        super(name);
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}