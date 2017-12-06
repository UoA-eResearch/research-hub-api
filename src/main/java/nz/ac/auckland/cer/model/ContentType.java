package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;


@Entity
@JsonFilter(ContentType.ENTITY_NAME)
public class ContentType extends Category {

    public static final String ENTITY_NAME = "ContentType";


    private String nameUiSingular;
    private String nameUiPlural;
    private Integer idUiCategory;

    @ManyToMany(mappedBy = "contentTypes")
    @JsonIgnore
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

    public String getNameUiSingular() {
        return nameUiSingular;
    }

    public void setNameUiSingular(String nameUiSingular) {
        this.nameUiSingular = nameUiSingular;
    }

    public String getNameUiPlural() {
        return nameUiPlural;
    }

    public void setNameUiPlural(String nameUiPlural) {
        this.nameUiPlural = nameUiPlural;
    }

    public Integer getIdUiCategory() {
        return idUiCategory;
    }

    public void setIdUiCategory(Integer idUiCategory) {
        this.idUiCategory = idUiCategory;
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}