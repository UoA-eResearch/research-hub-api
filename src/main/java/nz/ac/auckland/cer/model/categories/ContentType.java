package nz.ac.auckland.cer.model.categories;

import com.fasterxml.jackson.annotation.JsonFilter;
import nz.ac.auckland.cer.model.Content;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@JsonFilter(ContentType.ENTITY_NAME)
public class ContentType extends Category {

    public static final String ENTITY_NAME = "ContentType";

    @OneToMany(mappedBy="contentType")
    private Set<Content> contentItems;

    public ContentType() {
        super();
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
