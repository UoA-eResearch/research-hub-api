package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nz.ac.auckland.cer.model.categories.Role;

import javax.persistence.*;


@Entity
@JsonFilter(ContentRole.ENTITY_NAME)
public class ContentRole {

    public static final String ENTITY_NAME = "ContentRole";

    @ManyToOne
    @JoinColumn(name = "content_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Content content;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Role role;

    public ContentRole() {

    }

    public ContentRole(Content content, Person person, Role role) {
        this.role = role;
        this.content = content;
        this.person = person;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
