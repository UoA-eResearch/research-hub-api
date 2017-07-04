package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nz.ac.auckland.cer.model.categories.RoleType;

import javax.persistence.*;


@Entity
@JsonFilter(ContentRole.ENTITY_NAME)
public class ContentRole {

    public static final String ENTITY_NAME = "ContentRole";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    @JoinColumn(name = "content_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Content content;

    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "role_type_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private RoleType roleType;

    public ContentRole() {

    }

    public ContentRole(Content content, Person person, RoleType roleType) {
        this.roleType = roleType;
        this.content = content;
        this.person = person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
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
