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


    private String nameUi;
    private Integer idUi;

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

    public String getNameUi() {
        return nameUi;
    }

    public void setNameUi(String nameUi) {
        this.nameUi = nameUi;
    }

    public Integer getIdUi() {
        return idUi;
    }

    public void setIdUi(Integer idUi) {
        this.idUi = idUi;
    }

    public Set<Content> getContentItems() {
        return contentItems;
    }

    public void setContentItems(Set<Content> contentItems) {
        this.contentItems = contentItems;
    }
}