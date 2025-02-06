package org.miu.mppproject.librarysystem.libraryapp.frontend.model;

public class ContactInfo {

    private final String fullname;
    private final String phonenumber;
    private final String address;

    public ContactInfo(String fullname, String phonenumber, String address) {
        this.fullname = fullname;
        this.phonenumber = phonenumber;
        this.address = address;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getAddress() {
        return address;
    }
}
