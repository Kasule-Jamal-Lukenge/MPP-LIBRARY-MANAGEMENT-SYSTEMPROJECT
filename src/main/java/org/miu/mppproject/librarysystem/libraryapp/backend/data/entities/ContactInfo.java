package org.miu.mppproject.librarysystem.libraryapp.backend.data.entities;

public final class ContactInfo extends BaseEntity {


    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Address address;


    public ContactInfo(String firstName, String lastName, String phoneNumber, Address address) {
        super("CI-");
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getAddress() {
        return address;
    }
}
