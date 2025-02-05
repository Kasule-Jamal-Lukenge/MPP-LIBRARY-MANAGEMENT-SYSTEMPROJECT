package org.miu.mppproject.librarysystem.libraryapp.data.entities;

import java.util.List;

public final class Role extends BaseEntity {

    private final String name;
    private final List<Permission> rolePermissions;

    public Role(String name, List<Permission> rolePermissions) {
        super("RL-");
        this.name = name;
        this.rolePermissions = List.copyOf(rolePermissions);
    }


    public String getName() {
        return name;
    }

    public List<Permission> getRolePermissions() {
        return rolePermissions;
    }

}
