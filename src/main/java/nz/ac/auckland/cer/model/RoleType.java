package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@JsonFilter(RoleType.ENTITY_NAME)
public class RoleType extends Category {

    public static final String ENTITY_NAME = "RoleType";

    @OneToMany(mappedBy="roleType")
    @JsonIgnore
    private Set<ContentRole> contentRoles;

    public RoleType() {
        super();
    }

    public RoleType(int id) {
        super(id);
    }

    public RoleType(String name) {
        super(name);
    }

    public Set<ContentRole> getContentRoles() {
        return contentRoles;
    }

    public void setContentRoles(Set<ContentRole> contentRoles) {
        this.contentRoles = contentRoles;
    }
}
