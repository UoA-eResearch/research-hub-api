package nz.ac.auckland.cer.model.keys;


import nz.ac.auckland.cer.model.Content;
import nz.ac.auckland.cer.model.Person;
import nz.ac.auckland.cer.model.RoleType;

import java.io.Serializable;


public class ContentRoleKey implements Serializable {
    private Content content;
    private Person person;
    private RoleType roleType;
}
