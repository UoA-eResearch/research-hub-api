package nz.ac.auckland.cer.model;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Set;

@Entity
@JsonFilter(Person.ENTITY_NAME)
public class Person {
    public static final String ENTITY_NAME = "Person";
    public static final String[] DETAILS = new String[] {
        "orgUnits",
        "contentRoles"
    };

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String jobTitle;
    private String directoryUrl;
    private String image;

    @JsonFilter("orgUnits")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "person_org_unit", joinColumns = @JoinColumn(name = "person_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "org_unit_id", referencedColumnName = "id"))
    @JsonIgnoreProperties(value = {"people", "contentItems"}, allowSetters = true)
    @OrderBy("name ASC")
    private Set<OrgUnit> orgUnits;

    // One to many
    @JsonFilter("contentRoles")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
    @JsonIgnoreProperties(value = {"person"}, allowSetters = true)
    private Set<ContentRole> contentRoles;

    public Person() {

    }

    public Person(Integer id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDirectoryUrl() {
        return directoryUrl;
    }

    public void setDirectoryUrl(String directoryUrl) {
        this.directoryUrl = directoryUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<OrgUnit> getOrgUnits() {
        return orgUnits;
    }

    public void setOrgUnits(Set<OrgUnit> orgUnits) {
        this.orgUnits = orgUnits;
    }

    public Set<ContentRole> getContentRoles() {
        return contentRoles;
    }

    public void setContentRoles(Set<ContentRole> contentRoles) {
        this.contentRoles = contentRoles;
    }
}
