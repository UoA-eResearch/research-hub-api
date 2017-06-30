package nz.ac.auckland.cer.model;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonFilter(Person.ENTITY_NAME)
public class Person {
    public static final String ENTITY_NAME = "Person";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String firstName;
    private String lastName;
    private String email;
    private String directoryUrl;

    // One to many
    @JsonFilter("contentRoles")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonIgnoreProperties(value = "person", allowSetters=true)
    private Set<ContentRole> contentRoles;

    public Person() {

    }

    public Person(String firstName, String lastName, String email, String directoryUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.directoryUrl = directoryUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDirectoryUrl() {
        return directoryUrl;
    }

    public void setDirectoryUrl(String directoryUrl) {
        this.directoryUrl = directoryUrl;
    }

    public Set<ContentRole> getContentRoles() {
        return contentRoles;
    }

    public void setContentRoles(Set<ContentRole> contentRoles) {
        this.contentRoles = contentRoles;
    }
}
