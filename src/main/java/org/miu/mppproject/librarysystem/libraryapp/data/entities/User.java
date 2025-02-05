package org.miu.mppproject.librarysystem.libraryapp.data.entities;


public final class User extends Person {

    private final String username;
    private final Role assignedRole;


    public User(String username, Role role, ContactInfo contactInfo) {
        super(contactInfo, "USR-");
        this.username = username;
        this.assignedRole = role;
    }


    public String getUsername() {
        return username;
    }

    public Role getAssignedRole() {
        return assignedRole;
    }
}
