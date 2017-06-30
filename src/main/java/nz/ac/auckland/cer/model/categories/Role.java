package nz.ac.auckland.cer.model.categories;

import com.fasterxml.jackson.annotation.JsonFilter;
import nz.ac.auckland.cer.model.ContentRole;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@JsonFilter(Role.ENTITY_NAME)
public class Role extends Category {

    public static final String ENTITY_NAME = "Role";

    @OneToMany(mappedBy="role")
    private Set<ContentRole> contentRoles;

    public Role() {
        super();
    }

    public Role(String name) {
        super(name);
    }

    public Set<ContentRole> getContentRoles() {
        return contentRoles;
    }

    public void setContentRoles(Set<ContentRole> contentRoles) {
        this.contentRoles = contentRoles;
    }
}
