package org.miu.mppproject.librarysystem.libraryapp.data.entities;


public class Author extends Person {
    private final String shortBio;


    public Author(String shortBio, ContactInfo contactInfo) {
        super(contactInfo, "ATH-");
        this.shortBio = shortBio;
    }


    public String getShortBio() {
        return shortBio;
    }


}
