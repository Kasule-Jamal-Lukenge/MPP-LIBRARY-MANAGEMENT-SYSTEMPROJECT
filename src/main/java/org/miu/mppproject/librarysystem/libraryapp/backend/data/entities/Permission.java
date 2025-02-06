package org.miu.mppproject.librarysystem.libraryapp.backend.data.entities;

public final class Permission extends BaseEntity {

    private final String actionName;


    public Permission(String actionName) {
        super("PM-");
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }


}

