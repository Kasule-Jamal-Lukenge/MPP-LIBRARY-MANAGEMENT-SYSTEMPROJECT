package org.miu.mppproject.librarysystem.libraryapp.data.entities;

public abstract class Person extends BaseEntity {

    private final ContactInfo contactInfo;


    public Person(ContactInfo contactInfo, String prefix) {
        super(prefix);
        this.contactInfo = contactInfo;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

}
