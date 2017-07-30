package nz.ac.auckland.cer.model;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import nz.ac.auckland.cer.model.keys.ContentRoleKey;

import javax.persistence.*;


@Entity
@JsonFilter(ContentRole.ENTITY_NAME)
@Table(name = "person_content_role")
@IdClass(ContentRoleKey.class)
public class ContentRole {

    public static final String ENTITY_NAME = "ContentRole";

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Content content;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    @JsonIgnoreProperties(value = "contentRoles", allowSetters=true)
    private Person person;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
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
