package org.miu.mppproject.librarysystem.libraryapp.frontend.model;

public class User {

    private final String role;


    public User(String role, ContactInfo contactInfo) {
        this.role = role;
        this.contactInfo = contactInfo;
    }

    private final ContactInfo contactInfo;


    public String getRole() {
        return role;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }
}
